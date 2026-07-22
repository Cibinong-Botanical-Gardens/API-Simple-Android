package id.mayaksa.simpel.model.rest.response;

import com.google.gson.annotations.SerializedName;

public class LaporanResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Object data; // Kita pakai Object saja karena kita hanya butuh success & message untuk saat ini

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
