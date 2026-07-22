package id.mayaksa.simpel.model;

import com.google.gson.annotations.SerializedName;

public class User {

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

    @SerializedName("profile_photo")
    private String profilePhoto;

    public int getIdUser() {
        return idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public String getEmail() {
        return email;
    }

    public String getNomorTelpon() {
        return nomorTelpon;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }
}
