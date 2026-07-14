package id.mayaksa.simpel.ui.activity;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityReportBinding;
import id.mayaksa.simpel.utils.TimestampOverlay;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private String photoPath;
    private double lat;
    private double lon;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        EdgeToEdge.enable(this);

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

        processCapturedPhoto();
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