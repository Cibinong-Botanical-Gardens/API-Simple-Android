package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LaporanResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private DataWrapper data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public DataWrapper getData() { return data; }

    public static class DataWrapper {
        @SerializedName("current_page")
        private int currentPage;

        @SerializedName("data")
        private List<LaporanItem> items;

        @SerializedName("last_page")
        private int lastPage;

        @SerializedName("next_page_url")
        private String nextPageUrl;

        @SerializedName("prev_page_url")
        private String prevPageUrl;

        @SerializedName("total")
        private int total;

        public int getCurrentPage() { return currentPage; }
        public List<LaporanItem> getItems() { return items; }
        public int getLastPage() { return lastPage; }
        public String getNextPageUrl() { return nextPageUrl; }
        public String getPrevPageUrl() { return prevPageUrl; }
        public int getTotal() { return total; }
    }

    public static class LaporanItem {
        @SerializedName("id_laporan")
        private int idLaporan;

        @SerializedName("id_user")
        private Integer idUser;

        @SerializedName("judul")
        private String judul;

        @SerializedName("deskripsi")
        private String deskripsi;

        @SerializedName("jenis_laporan")
        private String jenisLaporan;

        @SerializedName("prioritas")
        private String prioritas;

        @SerializedName("status")
        private String status;

        @SerializedName("foto_before")
        private String fotoBefore;

        @SerializedName("foto_after")
        private String fotoAfter;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

        @SerializedName("is_koleksi")
        private Boolean isKoleksi;

        @SerializedName("tanggal")
        private String tanggal;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user")
        private UserItem user;

        public int getIdLaporan() { return idLaporan; }
        public Integer getIdUser() { return idUser; }
        public String getJudul() { return judul; }
        public String getDeskripsi() { return deskripsi; }
        public String getJenisLaporan() { return jenisLaporan; }
        public String getPrioritas() { return prioritas; }
        public String getStatus() { return status; }
        public String getFotoBefore() { return fotoBefore; }
        public String getFotoAfter() { return fotoAfter; }
        public String getLatitude() { return latitude; }
        public String getLongitude() { return longitude; }
        public Boolean getIsKoleksi() { return isKoleksi; }
        public String getTanggal() { return tanggal; }
        public String getDeletedAt() { return deletedAt; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
        public UserItem getUser() { return user; }
    }

    public static class UserItem {
        @SerializedName("id_user")
        private int idUser;

        @SerializedName("nama_user")
        private String namaUser;

        @SerializedName("email")
        private String email;

        @SerializedName("nomor_telpon")
        private String nomorTelpon;

        @SerializedName("role")
        private String role;

        @SerializedName("is_active")
        private Boolean isActive;

        @SerializedName("email_verified")
        private Boolean emailVerified;

        @SerializedName("profile_photo")
        private String profilePhoto;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        public int getIdUser() { return idUser; }
        public String getNamaUser() { return namaUser; }
        public String getEmail() { return email; }
        public String getNomorTelpon() { return nomorTelpon; }
        public String getRole() { return role; }
        public Boolean getIsActive() { return isActive; }
        public Boolean getEmailVerified() { return emailVerified; }
        public String getProfilePhoto() { return profilePhoto; }
        public String getDeletedAt() { return deletedAt; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
    }
}
