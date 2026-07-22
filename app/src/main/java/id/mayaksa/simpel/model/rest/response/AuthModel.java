package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;

import id.mayaksa.simpel.model.User;

public class AuthModel {

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
