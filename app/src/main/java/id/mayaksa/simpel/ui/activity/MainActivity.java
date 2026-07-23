package id.mayaksa.simpel.ui.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityMainBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.utils.Directs;
import id.mayaksa.simpel.utils.Functions;
import id.mayaksa.simpel.utils.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    Snackbar backSnackbar;

    private ActivityMainBinding binding;

    private static final int REQ_PERMISSIONS = 100;
    private static final int REQ_CAPTURE_IMAGE = 200;

    public static final String EXTRA_PHOTO_PATH = "extra_photo_path";
    public static final String EXTRA_LAT = "extra_lat";
    public static final String EXTRA_LON = "extra_lon";
    public static final String EXTRA_ADDRESS = "extra_address";

    private FusedLocationProviderClient fusedLocationClient;

    private Location lastLocation;
    private String lastAddress = "Mencari alamat...";

    private String currentPhotoPath;
    private Uri currentPhotoUri;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_location, R.id.navigation_history, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        initComponents();
    }

    private void initComponents(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        backSnackbar = Snackbar.make(binding.getRoot(), "Press Back Button again to Exit", Snackbar.LENGTH_SHORT);

        Functions.dialogWelcome(this, false);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReport(MainActivity.this, false);
            }
        });
    }

    public Fragment getForegroundFragment(){
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    public void onBackPressed() {
        if(getForegroundFragment().getClass().getSimpleName().equals("HomeFragment")){
            if (backSnackbar.isShown()) {
                super.onBackPressed();
            } else {
                backSnackbar.show();
            }
        }else{
            super.onBackPressed();
        }
    }

    void dialogReport(Context context, Boolean finish) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_report);

        Button open_settings = bottomSheetDialog.findViewById(R.id.cancel);
        Button retry = bottomSheetDialog.findViewById(R.id.agree);

        assert open_settings != null;
        open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        assert retry != null;
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasAllPermissions()) {
                    fetchLocationThenCapture();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, REQUIRED_PERMISSIONS, REQ_PERMISSIONS);
                }
            }
        });

        bottomSheetDialog.show();

        if (!hasAllPermissions()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQ_PERMISSIONS);
        } else {
            updateLocationStatus();
        }

        if(finish){
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        }

    }

    private boolean hasAllPermissions() {
        for (String p : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSIONS) {
            if (hasAllPermissions()) {
                updateLocationStatus();
            } else {
                Toast.makeText(this, "Izin kamera & lokasi diperlukan untuk aplikasi ini", Toast.LENGTH_LONG).show();
            }
        }
    }

    /** Ambil lokasi terakhir sekadar untuk menampilkan status di layar sebelum foto diambil. */
    private void updateLocationStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                lastLocation = location;
                resolveAddressAsync(location);
            }
        });
    }

    private void resolveAddressAsync(Location location) {
        executor.execute(() -> {
            String address = reverseGeocode(location.getLatitude(), location.getLongitude());
            lastAddress = address;
        });
    }

    private String reverseGeocode(double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address a = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                if (a.getSubThoroughfare() != null) sb.append(a.getSubThoroughfare()).append(" ");
                if (a.getThoroughfare() != null) sb.append(a.getThoroughfare()).append(", ");
                if (a.getSubLocality() != null) sb.append(a.getSubLocality()).append(", ");
                if (a.getLocality() != null) sb.append(a.getLocality()).append(", ");
                if (a.getAdminArea() != null) sb.append(a.getAdminArea()).append(" ");
                if (a.getPostalCode() != null) sb.append(a.getPostalCode()).append(", ");
                if (a.getCountryName() != null) sb.append(a.getCountryName());
                return sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder error", e);
        }
        return "Alamat tidak ditemukan";
    }

    /** Pastikan kita punya lokasi terbaru sebelum membuka kamera, baru buka kamera. */
    private void fetchLocationThenCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            openCamera();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            lastLocation = location;
            if (location != null) {
                lastAddress = reverseGeocode(location.getLatitude(), location.getLongitude());
            }
            openCamera();
        }).addOnFailureListener(e -> openCamera());
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Gagal membuat file foto", Toast.LENGTH_SHORT).show();
                return;
            }
            currentPhotoUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
            startActivityForResult(takePictureIntent, REQ_CAPTURE_IMAGE);
        } else {
            Toast.makeText(this, "Tidak ada aplikasi kamera ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new java.util.Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                double lat = lastLocation != null ? lastLocation.getLatitude() : 0;
                double lon = lastLocation != null ? lastLocation.getLongitude() : 0;
                String address = lastAddress != null ? lastAddress : "Alamat tidak tersedia";

                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                intent.putExtra(EXTRA_PHOTO_PATH, currentPhotoPath);
                intent.putExtra(EXTRA_LAT, lat);
                intent.putExtra(EXTRA_LON, lon);
                intent.putExtra(EXTRA_ADDRESS, address);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Pengambilan foto dibatalkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

}