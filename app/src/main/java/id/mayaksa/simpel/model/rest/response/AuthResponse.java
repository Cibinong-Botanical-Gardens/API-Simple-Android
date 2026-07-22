package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private AuthModel data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AuthModel getData() {
        return data;
    }
}
