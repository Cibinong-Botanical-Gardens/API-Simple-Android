package id.mayaksa.simpel.model;

public class Report {

    String created_at;
    int image;
    String title;
    String text;
    String latitude;
    String longitude;
    int profile_image;
    String name;
    String institute;
    String role;

    public Report(String created_at, int image, String title, String text, String latitude, String longitude, int profile_image, String name, String institute, String role) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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

    public int getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(int profile_image) {
        this.profile_image = profile_image;
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
