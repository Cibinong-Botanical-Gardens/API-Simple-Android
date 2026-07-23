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
        @SerializedName("data")
        private List<LogbookItem> items;

        public List<LogbookItem> getItems() { return items; }
    }

    public static class LogbookItem {
        @SerializedName("id_tree")
        private String idTree;

        @SerializedName("area")
        private String area;

        @SerializedName("jenis_kegiatan")
        private String jenisKegiatan;

        @SerializedName("create_at")
        private String createAt;

        public String getIdTree() { return idTree; }
        public String getArea() { return area; }
        public String getJenisKegiatan() { return jenisKegiatan; }
        public String getCreateAt() { return createAt; }
    }
}
