package id.mayaksa.simpel.model;

import com.google.gson.annotations.SerializedName;

public class Report {

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("foto_before")
    private String image;

    @SerializedName("judul")
    private String title;

    @SerializedName("deskripsi")
    private String text;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("profile_photo")
    private String profile_image;

    @SerializedName("nama_user")
    private String name;

    @SerializedName("institute")
    private String institute;

    @SerializedName("role")
    private String role;

    public Report() {
    }

    public Report(String created_at, int imageRes, String title, String text, String latitude, String longitude, int profileImageRes, String name, String institute, String role) {
        this.created_at = created_at;
        this.image = String.valueOf(imageRes);
        this.title = title;
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profile_image = String.valueOf(profileImageRes);
        this.name = name;
        this.institute = institute;
        this.role = role;
    }

    public Report(String created_at, String image, String title, String text, String latitude, String longitude, String profile_image, String name, String institute, String role) {
        this.created_at = created_at;
        this.image = image;
        this.title = title;
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        this.profile_image = profile_image;
        this.name = name;
        this.institute = institute;
        this.role = role;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public int getImageRes() {
        try {
            return Integer.parseInt(image);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage(int imageRes) {
        this.image = String.valueOf(imageRes);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getProfile_image() {
        return profile_image;
    }

    public int getProfileImageRes() {
        try {
            return Integer.parseInt(profile_image);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setProfile_image(int profileImageRes) {
        this.profile_image = String.valueOf(profileImageRes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
