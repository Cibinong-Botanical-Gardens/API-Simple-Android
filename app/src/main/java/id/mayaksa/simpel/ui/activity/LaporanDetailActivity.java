package id.mayaksa.simpel.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityLaporanDetailBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.model.rest.request.UpdateLaporanRequest;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import id.mayaksa.simpel.model.rest.response.UpdateLaporanResponse;
import id.mayaksa.simpel.utils.SharedPreferences;

/**
 * Halaman Detail & Edit Laporan.
 * Menampilkan seluruh informasi laporan dan menyediakan form editable
 * untuk admin/root mengubah judul, deskripsi, jenis laporan, prioritas, dan status.
 *
 * Intent key:
 *   EXTRA_LAPORAN_ITEM  → LaporanResponse.LaporanItem (Serializable)
 *
 * Result:
 *   RESULT_OK + EXTRA_UPDATED_ITEM → LaporanItem yang sudah diupdate
 */
public class LaporanDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LAPORAN_ITEM = "extra_laporan_item";
    public static final String EXTRA_UPDATED_ITEM = "extra_updated_item";

    private ActivityLaporanDetailBinding binding;
    private LaporanResponse.LaporanItem laporanItem;
    private String token;

    // Dropdown choices
    private static final String[] JENIS_LAPORAN_OPTIONS = new String[]{
            "konservasi",
            "kesehatan pohon",
            "non-konservasi",
            "lainnya"
    };

    private static final String[] PRIORITAS_OPTIONS = new String[]{
            "1 (Rendah)",
            "2 (Sedang)",
            "3 (Tinggi)"
    };

    public static Intent createIntent(Context context, LaporanResponse.LaporanItem item) {
        Intent intent = new Intent(context, LaporanDetailActivity.class);
        intent.putExtra(EXTRA_LAPORAN_ITEM, item);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLaporanDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Ambil data laporan dari Intent (safe extra for API 33+)
        if (getIntent() != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                laporanItem = getIntent().getSerializableExtra(EXTRA_LAPORAN_ITEM, LaporanResponse.LaporanItem.class);
            } else {
                laporanItem = (LaporanResponse.LaporanItem) getIntent().getSerializableExtra(EXTRA_LAPORAN_ITEM);
            }
        }

        if (laporanItem == null) {
            Toast.makeText(this, "Data laporan tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        token = SharedPreferences.loadToken(this);

        initDropdowns();
        bindData();
        checkAdminRole();
    }

    /**
     * Inisialisasi adapter dropdown untuk Jenis Laporan & Prioritas.
     */
    private void initDropdowns() {
        ArrayAdapter<String> jenisAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, JENIS_LAPORAN_OPTIONS);
        binding.dropdownJenis.setAdapter(jenisAdapter);

        ArrayAdapter<String> prioritasAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, PRIORITAS_OPTIONS);
        binding.dropdownPrioritas.setAdapter(prioritasAdapter);
    }

    /**
     * Bind seluruh field laporan ke View (prefill form).
     */
    private void bindData() {
        // Status Badge
        String statusText = laporanItem.getStatus() != null ? laporanItem.getStatus() : "Pending";
        binding.tvStatus.setText(statusText);

        // Judul (prefill)
        if (laporanItem.getJudul() != null) {
            binding.etJudul.setText(laporanItem.getJudul());
        }

        // Deskripsi (prefill)
        if (laporanItem.getDeskripsi() != null) {
            binding.etDeskripsi.setText(laporanItem.getDeskripsi());
        }

        // Jenis Laporan / Kategori (prefill dropdown)
        String jenis = laporanItem.getJenisLaporan() != null ? laporanItem.getJenisLaporan() : "non-konservasi";
        binding.dropdownJenis.setText(jenis, false);

        // Prioritas (prefill dropdown)
        String prioritasRaw = laporanItem.getPrioritas();
        String selectedPrioritasText = PRIORITAS_OPTIONS[1]; // default 2 (Sedang)
        if (prioritasRaw != null) {
            if (prioritasRaw.contains("1")) {
                selectedPrioritasText = PRIORITAS_OPTIONS[0];
            } else if (prioritasRaw.contains("3")) {
                selectedPrioritasText = PRIORITAS_OPTIONS[2];
            }
        }
        binding.dropdownPrioritas.setText(selectedPrioritasText, false);

        // Tanggal (Read-only)
        String tanggal = laporanItem.getTanggal() != null
                ? laporanItem.getTanggal()
                : laporanItem.getCreatedAt();
        binding.tvTanggal.setText(tanggal != null ? tanggal : "-");

        // Pelapor (Read-only)
        if (laporanItem.getUser() != null && laporanItem.getUser().getNamaUser() != null) {
            binding.tvNamaUser.setText(laporanItem.getUser().getNamaUser());
        } else {
            binding.tvNamaUser.setText("Tidak diketahui");
        }

        // Lokasi (Read-only)
        if (laporanItem.getLatitude() != null && laporanItem.getLongitude() != null) {
            binding.tvLokasi.setText(laporanItem.getLatitude() + ", " + laporanItem.getLongitude());
        } else {
            binding.tvLokasi.setText("Tidak tersedia");
        }

        // Foto (jika ada)
        if (laporanItem.getFotoBefore() != null && !laporanItem.getFotoBefore().isEmpty()) {
            binding.cardFoto.setVisibility(View.VISIBLE);
        } else {
            binding.cardFoto.setVisibility(View.GONE);
        }
    }

    /**
     * Cek role user dari SharedPreferences.
     * Admin/root → form editable + tombol simpan tampil.
     * User biasa → form read-only + tombol simpan tersembunyi.
     */
    private void checkAdminRole() {
        android.content.SharedPreferences prefs = getSharedPreferences(
                SharedPreferences.SHARED_PREFS_USER, MODE_PRIVATE);
        String userRole = prefs.getString(SharedPreferences.role, "");

        boolean isAdmin = "admin".equalsIgnoreCase(userRole) || "root".equalsIgnoreCase(userRole) || "m_admin".equalsIgnoreCase(userRole);

        if (isAdmin) {
            binding.etJudul.setEnabled(true);
            binding.etDeskripsi.setEnabled(true);
            binding.dropdownJenis.setEnabled(true);
            binding.dropdownPrioritas.setEnabled(true);

            binding.cardUpdateStatus.setVisibility(View.VISIBLE);
            setupStatusChips();
            setupUpdateButton();
        } else {
            // User biasa: disable input form & sembunyikan section update
            binding.etJudul.setEnabled(false);
            binding.etDeskripsi.setEnabled(false);
            binding.dropdownJenis.setEnabled(false);
            binding.dropdownPrioritas.setEnabled(false);

            binding.cardUpdateStatus.setVisibility(View.GONE);
        }
    }

    /**
     * Pre-select chip status sesuai status saat ini.
     */
    private void setupStatusChips() {
        String currentStatus = laporanItem.getStatus() != null
                ? laporanItem.getStatus().toLowerCase() : "pending";

        switch (currentStatus) {
            case "diproses":
                binding.chipDiproses.setChecked(true);
                break;
            case "selesai":
                binding.chipSelesai.setChecked(true);
                break;
            case "ditolak":
                binding.chipDitolak.setChecked(true);
                break;
            default: // "pending"
                binding.chipPending.setChecked(true);
                break;
        }
    }

    private void setupUpdateButton() {
        binding.btnUpdateStatus.setOnClickListener(v -> performUpdate());
    }

    /**
     * Validasi form client-side lalu submit data lengkap ke API backend.
     */
    private void performUpdate() {
        // Reset pesan error pada form
        binding.etJudul.setError(null);
        binding.etDeskripsi.setError(null);

        String judul = binding.etJudul.getText() != null
                ? binding.etJudul.getText().toString().trim() : "";
        String deskripsi = binding.etDeskripsi.getText() != null
                ? binding.etDeskripsi.getText().toString().trim() : "";
        String jenisLaporan = binding.dropdownJenis.getText() != null
                ? binding.dropdownJenis.getText().toString().trim() : "non-konservasi";

        // Extract angka prioritas (misal "1 (Rendah)" -> "1")
        String prioritasText = binding.dropdownPrioritas.getText() != null
                ? binding.dropdownPrioritas.getText().toString().trim() : "2";
        String prioritas = prioritasText.split(" ")[0];

        // Validasi Client-Side
        if (judul.isEmpty()) {
            binding.etJudul.setError("Judul laporan wajib diisi");
            binding.etJudul.requestFocus();
            return;
        }

        if (deskripsi.isEmpty()) {
            binding.etDeskripsi.setError("Deskripsi laporan wajib diisi");
            binding.etDeskripsi.requestFocus();
            return;
        }

        // Selected Status dari ChipGroup
        int selectedChipId = binding.chipGroupStatus.getCheckedChipId();
        if (selectedChipId == View.NO_ID) {
            Snackbar.make(binding.getRoot(), "Pilih status laporan terlebih dahulu", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Chip selectedChip = binding.chipGroupStatus.findViewById(selectedChipId);
        String status = selectedChip.getText().toString().toLowerCase();

        // Map status string ke ID (biasanya backend Laravel butuh ID numerik untuk foreign key)
        String idStatus = "1"; // default pending
        if (selectedChipId == R.id.chip_diproses) idStatus = "2";
        else if (selectedChipId == R.id.chip_selesai) idStatus = "3";
        else if (selectedChipId == R.id.chip_ditolak) idStatus = "4";

        // Ambil lokasi & is_koleksi existing
        String latitude = (laporanItem.getLatitude() != null && !laporanItem.getLatitude().isEmpty())
                ? laporanItem.getLatitude() : "-6.595";
        String longitude = (laporanItem.getLongitude() != null && !laporanItem.getLongitude().isEmpty())
                ? laporanItem.getLongitude() : "106.801";
        String isKoleksi = (laporanItem.getIsKoleksi() != null && laporanItem.getIsKoleksi())
                ? "1" : "0";

        setLoadingState(true);

        // Buat request body lengkap untuk backend
        UpdateLaporanRequest request = new UpdateLaporanRequest(
                status,
                idStatus,
                judul,
                deskripsi,
                jenisLaporan,
                prioritas,
                latitude,
                longitude,
                isKoleksi
        );

        ApiFunction.UpdateLaporanRequest(token, laporanItem.getIdLaporan(), request,
                new ApiFunction.ApiCallback<UpdateLaporanResponse>() {
                    @Override
                    public void onSuccess(UpdateLaporanResponse data) {
                        setLoadingState(false);

                        Toast.makeText(LaporanDetailActivity.this,
                                data.getMessage() != null ? data.getMessage() : "Laporan berhasil diperbarui",
                                Toast.LENGTH_SHORT).show();

                        // Kirim result ke HistoryFragment agar list otomatis di-refresh
                        Intent resultIntent = new Intent();
                        if (data.getData() != null) {
                            resultIntent.putExtra(EXTRA_UPDATED_ITEM, data.getData());
                        }
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(String message) {
                        setLoadingState(false);

                        // Highlight field error jika dikembalikan dari backend
                        if (message != null) {
                            if (message.toLowerCase().contains("judul")) {
                                binding.etJudul.setError(message);
                            }
                            if (message.toLowerCase().contains("deskripsi")) {
                                binding.etDeskripsi.setError(message);
                            }
                        }

                        // Tampilkan pesan error lengkap dari backend tanpa menutup activity
                        Snackbar.make(binding.getRoot(), message != null ? message : "Gagal memperbarui laporan", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoadingState(boolean isLoading) {
        binding.progressBarUpdate.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnUpdateStatus.setEnabled(!isLoading);
        binding.etJudul.setEnabled(!isLoading);
        binding.etDeskripsi.setEnabled(!isLoading);
        binding.dropdownJenis.setEnabled(!isLoading);
        binding.dropdownPrioritas.setEnabled(!isLoading);
        binding.chipGroupStatus.setEnabled(!isLoading);

        for (int i = 0; i < binding.chipGroupStatus.getChildCount(); i++) {
            binding.chipGroupStatus.getChildAt(i).setEnabled(!isLoading);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

