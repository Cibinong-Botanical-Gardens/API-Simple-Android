package id.mayaksa.simpel.ui.activity;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityReportBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.utils.SharedPreferences;
import id.mayaksa.simpel.utils.TimestampOverlay;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ReportActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityReportBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private GoogleMap mMap;

    private String photoPath;
    private double lat;
    private double lon;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Ambil data yang dikirim dari MainActivity
        photoPath = getIntent().getStringExtra(MainActivity.EXTRA_PHOTO_PATH);
        lat = getIntent().getDoubleExtra(MainActivity.EXTRA_LAT, 0);
        lon = getIntent().getDoubleExtra(MainActivity.EXTRA_LON, 0);
        address = getIntent().getStringExtra(MainActivity.EXTRA_ADDRESS);
        if (address == null) address = "Alamat tidak tersedia";

        if (photoPath == null) {
            Toast.makeText(this, "Path foto kosong", Toast.LENGTH_LONG).show();
            return;
        }

        initDropdown();
        setupListeners();
        processCapturedPhoto();
    }

    private void initDropdown() {
        // Jenis Laporan
        String[] reportTypes = {"konservasi", "kesehatan pohon", "lainnya"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, reportTypes);
        binding.contentReport.roleDropdown.setAdapter(typeAdapter);

        // Prioritas
        String[] priorities = {"1 (Rendah)", "2 (Sedang)", "3 (Tinggi)"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, priorities);
        binding.contentReport.priorityDropdown.setAdapter(priorityAdapter);
        binding.contentReport.priorityDropdown.setText(priorities[1], false); // Default Sedang (2)
    }

    private void setupListeners() {
        binding.btnSend.setOnClickListener(v -> sendReport());
        binding.contentReport.checkboxTree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.contentReport.treeDropdown.setEnabled(!isChecked);
            if (isChecked) binding.contentReport.treeDropdown.setText("");
        });
    }

    private void sendReport() {
        String token = SharedPreferences.loadToken(this);
        String jenis = binding.contentReport.roleDropdown.getText().toString();
        String judul = binding.contentReport.title.getText().toString();
        String deskripsi = binding.contentReport.desc.getText().toString();
        String identitasPohon = binding.contentReport.treeDropdown.getText().toString();
        
        // Prioritas (ambil angka depan saja)
        String priorityText = binding.contentReport.priorityDropdown.getText().toString();
        String prioritas = priorityText.split(" ")[0];

        // isKoleksi: 1 jika ada identitas pohon, 0 jika checkbox "tidak tahu" dicentang
        String isKoleksi = binding.contentReport.checkboxTree.isChecked() ? "0" : "1";

        if (judul.isEmpty() || deskripsi.isEmpty() || jenis.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field wajib", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lat == 0.0 || lon == 0.0) {
            Toast.makeText(this, "Lokasi tidak valid. Pastikan GPS aktif.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Jika ada identitas pohon, gabungkan ke deskripsi atau judul jika perlu
        if (!identitasPohon.isEmpty()) {
            deskripsi = "Identitas Pohon: " + identitasPohon + "\n\n" + deskripsi;
        }

        File file = new File(photoPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto_before", file.getName(), requestFile);

        RequestBody rbJenis = RequestBody.create(MediaType.parse("text/plain"), jenis.toLowerCase());
        RequestBody rbJudul = RequestBody.create(MediaType.parse("text/plain"), judul);
        RequestBody rbDesc = RequestBody.create(MediaType.parse("text/plain"), deskripsi);
        RequestBody rbPrioritas = RequestBody.create(MediaType.parse("text/plain"), prioritas);
        RequestBody rbLat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat));
        RequestBody rbLon = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lon));
        RequestBody rbIsKoleksi = RequestBody.create(MediaType.parse("text/plain"), isKoleksi);

        ApiFunction.CreateLaporanRequest(this, token, rbJenis, rbJudul, rbDesc, rbPrioritas, rbLat, rbLon, rbIsKoleksi, body);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Lokasi Laporan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17f));
        mMap.getUiSettings().setAllGesturesEnabled(false); // Map statis saja di form ini
    }

    private void processCapturedPhoto() {
        executor.execute(() -> {
            try {
                Bitmap original = TimestampOverlay.loadRotatedBitmap(photoPath);

                String dateTimeStr = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
                        .format(new Date());

                Bitmap result = TimestampOverlay.drawOverlay(
                        original,
                        "Lokasi Saat Ini",
                        address,
                        lat,
                        lon,
                        dateTimeStr
                );

                TimestampOverlay.saveBitmapToFile(result, photoPath);
                Uri savedUri = TimestampOverlay.saveToGallery(this, result, "TS_" + System.currentTimeMillis());

                mainHandler.post(() -> {
                    binding.timestampImage.setImageBitmap(result);
                    Toast.makeText(this, "Foto tersimpan: " + savedUri, Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                Log.e(TAG, "Gagal memproses foto", e);
                mainHandler.post(() ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}