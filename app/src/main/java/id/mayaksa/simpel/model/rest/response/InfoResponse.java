package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InfoResponse {

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
        private List<InfoItem> items;

        @SerializedName("last_page")
        private int lastPage;

        @SerializedName("next_page_url")
        private String nextPageUrl;

        @SerializedName("prev_page_url")
        private String prevPageUrl;

        @SerializedName("total")
        private int total;

        public int getCurrentPage() { return currentPage; }
        public List<InfoItem> getItems() { return items; }
        public int getLastPage() { return lastPage; }
        public String getNextPageUrl() { return nextPageUrl; }
        public String getPrevPageUrl() { return prevPageUrl; }
        public int getTotal() { return total; }
    }

    public static class InfoItem {
        @SerializedName("id_info")
        private int idInfo;

        @SerializedName("id_user")
        private int idUser;

        @SerializedName("judul")
        private String judul;

        @SerializedName("deskripsi")
        private String deskripsi;

        @SerializedName("gambar_url")
        private String gambarUrl;

        @SerializedName("is_published")
        private boolean isPublished;

        @SerializedName("published_at")
        private String publishedAt;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user")
        private UserItem user;

        public int getIdInfo() { return idInfo; }
        public int getIdUser() { return idUser; }
        public String getJudul() { return judul; }
        public String getDeskripsi() { return deskripsi; }
        public String getGambarUrl() { return gambarUrl; }
        public boolean isPublished() { return isPublished; }
        public String getPublishedAt() { return publishedAt; }
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
        private boolean isActive;

        @SerializedName("email_verified")
        private boolean emailVerified;

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
        public boolean isActive() { return isActive; }
        public boolean isEmailVerified() { return emailVerified; }
        public String getProfilePhoto() { return profilePhoto; }
        public String getDeletedAt() { return deletedAt; }
        public String getCreatedAt() { return createdAt; }
        public String getUpdatedAt() { return updatedAt; }
    }
}
