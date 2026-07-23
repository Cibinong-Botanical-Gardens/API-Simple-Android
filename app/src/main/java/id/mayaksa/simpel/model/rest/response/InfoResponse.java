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
        @SerializedName("data")
        private List<InfoItem> items;

        public List<InfoItem> getItems() { return items; }
    }

    public static class InfoItem {
        @SerializedName("judul")
        private String judul;

        @SerializedName("deskripsi")
        private String deskripsi;

        @SerializedName("gambar_url")
        private String gambarUrl;

        public String getJudul() { return judul; }
        public String getDeskripsi() { return deskripsi; }
        public String getGambarUrl() { return gambarUrl; }
    }
}
