package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;

public class UpdateLaporanResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private LaporanResponse.LaporanItem data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public LaporanResponse.LaporanItem getData() {
        return data;
    }
}
