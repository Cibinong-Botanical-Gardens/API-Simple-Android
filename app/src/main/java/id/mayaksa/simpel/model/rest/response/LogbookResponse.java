package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LogbookResponse {

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
        private List<LogbookItem> items;

        @SerializedName("last_page")
        private int lastPage;

        @SerializedName("next_page_url")
        private String nextPageUrl;

        @SerializedName("prev_page_url")
        private String prevPageUrl;

        @SerializedName("total")
        private int total;

        public int getCurrentPage() { return currentPage; }
        public List<LogbookItem> getItems() { return items; }
        public int getLastPage() { return lastPage; }
        public String getNextPageUrl() { return nextPageUrl; }
        public String getPrevPageUrl() { return prevPageUrl; }
        public int getTotal() { return total; }
    }

    public static class LogbookItem {
        @SerializedName("id")
        private Integer id;

        @SerializedName("id_tree")
        private String idTree;

        @SerializedName("id_user")
        private Integer idUser;

        @SerializedName("area")
        private String area;

        @SerializedName("jenis_kegiatan")
        private String jenisKegiatan;

        @SerializedName("judul")
        private String judul;

        @SerializedName("isi")
        private String isi;

        @SerializedName("tanggal")
        private String tanggal;

        @SerializedName("foto")
        private String foto;

        @SerializedName("status")
        private String status;

        // created_at bisa jadi "create_at" (typo di API lama) atau "created_at"
        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("create_at")
        private String createAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("user")
        private UserItem user;

        public Integer getId() { return id; }
        public String getIdTree() { return idTree; }
        public Integer getIdUser() { return idUser; }
        public String getArea() { return area; }
        public String getJenisKegiatan() { return jenisKegiatan; }
        public String getJudul() { return judul; }
        public String getIsi() { return isi; }
        public String getTanggal() { return tanggal; }
        public String getFoto() { return foto; }
        public String getStatus() { return status; }
        public String getCreatedAt() {
            // Fallback untuk typo "create_at" di response lama
            return createdAt != null ? createdAt : createAt;
        }
        public String getUpdatedAt() { return updatedAt; }
        public String getDeletedAt() { return deletedAt; }
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
