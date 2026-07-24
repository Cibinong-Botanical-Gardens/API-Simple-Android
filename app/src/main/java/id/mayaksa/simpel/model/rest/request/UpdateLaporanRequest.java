package id.mayaksa.simpel.model.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * POJO Request untuk endpoint POST /api/v1/laporan/{id}.
 * Backend Laravel mewajibkan field berikut saat update laporan:
 * - status (required: pending, diproses, selesai, ditolak)
 * - id_status (optional mapping: 1=pending, 2=diproses, 3=selesai, 4=ditolak)
 * - judul (required)
 * - deskripsi (required)
 * - jenis_laporan (required: konservasi, kesehatan pohon, non-konservasi, lainnya)
 * - prioritas (required: 1, 2, 3)
 * - latitude (required)
 * - longitude (required)
 * - is_koleksi (optional/nullable)
 */
public class UpdateLaporanRequest {

    @SerializedName("status")
    private String status;

    @SerializedName("id_status")
    private String idStatus;

    @SerializedName("judul")
    private String judul;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("jenis_laporan")
    private String jenisLaporan;

    @SerializedName("prioritas")
    private String prioritas;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("is_koleksi")
    private String isKoleksi;

    public UpdateLaporanRequest() {
    }

    public UpdateLaporanRequest(String status, String idStatus, String judul, String deskripsi,
                                String jenisLaporan, String prioritas,
                                String latitude, String longitude, String isKoleksi) {
        this.status = status;
        this.idStatus = idStatus;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.jenisLaporan = jenisLaporan;
        this.prioritas = prioritas;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isKoleksi = isKoleksi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJenisLaporan() {
        return jenisLaporan;
    }

    public void setJenisLaporan(String jenisLaporan) {
        this.jenisLaporan = jenisLaporan;
    }

    public String getPrioritas() {
        return prioritas;
    }

    public void setPrioritas(String prioritas) {
        this.prioritas = prioritas;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsKoleksi() {
        return isKoleksi;
    }

    public void setIsKoleksi(String isKoleksi) {
        this.isKoleksi = isKoleksi;
    }
}
