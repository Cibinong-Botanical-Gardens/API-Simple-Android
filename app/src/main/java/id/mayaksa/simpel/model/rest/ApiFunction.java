package id.mayaksa.simpel.model.rest;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.mayaksa.simpel.model.Report;
import id.mayaksa.simpel.model.User;
import id.mayaksa.simpel.model.rest.response.AuthResponse;
import id.mayaksa.simpel.model.rest.response.InfoResponse;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import id.mayaksa.simpel.model.rest.response.LogbookResponse;
import id.mayaksa.simpel.utils.Directs;
import id.mayaksa.simpel.utils.SharedPreferences;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiFunction {

    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onFailure(String message);
    }

    public static void LoginRequest(Context context, String email, String password) {
        Call<AuthResponse> call = ApiClient.getApiService().loginRequest(email, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse result = response.body();
                    if (result.isSuccess()) {
                        String token = result.getData().getToken();
                        User user = result.getData().getUser();
                        SharedPreferences.saveUser(context, token, user);
                        Toast.makeText((Activity) context, result.getMessage(), Toast.LENGTH_SHORT).show();
                        Directs.mainDirect(context, true);
                    } else {
                        Toast.makeText((Activity) context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText((Activity) context, "Login gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText((Activity) context, "Gagal terhubung ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void RegisterRequest(Context context, String firstName, String lastName, String email, String password, String phone, String role) {
        Call<AuthResponse> call = ApiClient.getApiService().registerRequest(firstName, lastName, email, password, phone, role);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText((Activity) context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText((Activity) context, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText((Activity) context, "Registrasi gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText((Activity) context, "Gagal terhubung ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void CreateLaporanRequest(Context context, String token, RequestBody jenisLaporan, RequestBody judul, RequestBody deskripsi, RequestBody prioritas, RequestBody latitude, RequestBody longitude, RequestBody isKoleksi, MultipartBody.Part fotoBefore) {
        Call<LaporanResponse> call = ApiClient.getApiService().createLaporanRequest("Bearer " + token, jenisLaporan, judul, deskripsi, prioritas, latitude, longitude, isKoleksi, fotoBefore);
        call.enqueue(new Callback<LaporanResponse>() {
            @Override
            public void onResponse(Call<LaporanResponse> call, Response<LaporanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LaporanResponse result = response.body();
                    if (result.isSuccess()) {
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                        ((Activity) context).finish();
                    } else {
                        Toast.makeText(context, "Gagal: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 422) {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        StringBuilder message = new StringBuilder();
                        if (jsonObject.has("message")) {
                            message.append(jsonObject.getString("message")).append("\n");
                        }
                        if (jsonObject.has("errors")) {
                            JSONObject errors = jsonObject.getJSONObject("errors");
                            Iterator<String> keys = errors.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                message.append("- ").append(errors.getJSONArray(key).get(0)).append("\n");
                            }
                        }
                        Toast.makeText(context, message.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Gagal Validasi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Gagal mengirim laporan: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LaporanResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void LogoutRequest(Context context, String token) {
        ApiClient.getApiService().logoutRequest("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.clearUser(context);
                    Directs.splashDirect(context, true);
                }
            }
            @Override public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    /**
     * GET /api/v1/laporan — mengambil daftar laporan dengan callback pattern.
     * Mengembalikan list kosong (bukan null) jika data API kosong.
     */
    public static void GetLaporanRequest(String token, ApiCallback<List<LaporanResponse.LaporanItem>> callback) {
        ApiClient.getApiService().getLaporanRequest("Bearer " + token).enqueue(new Callback<LaporanResponse>() {
            @Override
            public void onResponse(Call<LaporanResponse> call, Response<LaporanResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LaporanResponse result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        List<LaporanResponse.LaporanItem> items = result.getData().getItems();
                        // Kirim list kosong jika null (API belum ada data)
                        callback.onSuccess(items != null ? items : new ArrayList<>());
                    } else {
                        callback.onFailure(result.getMessage() != null ? result.getMessage() : "Gagal ambil laporan");
                    }
                } else {
                    callback.onFailure("Gagal terhubung ke server (kode: " + response.code() + ")");
                }
            }
            @Override
            public void onFailure(Call<LaporanResponse> call, Throwable t) {
                callback.onFailure("Tidak dapat terhubung ke server: " + t.getMessage());
            }
        });
    }

    /**
     * GET /api/v1/logbook — mengambil daftar logbook dengan callback pattern.
     * Mengembalikan list kosong (bukan null) jika data API kosong.
     */
    public static void GetLogbookRequest(String token, ApiCallback<List<LogbookResponse.LogbookItem>> callback) {
        ApiClient.getApiService().getLogbookRequest("Bearer " + token).enqueue(new Callback<LogbookResponse>() {
            @Override
            public void onResponse(Call<LogbookResponse> call, Response<LogbookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LogbookResponse result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        List<LogbookResponse.LogbookItem> items = result.getData().getItems();
                        // Kirim list kosong jika null (API belum ada data)
                        callback.onSuccess(items != null ? items : new ArrayList<>());
                    } else {
                        callback.onFailure(result.getMessage() != null ? result.getMessage() : "Gagal ambil logbook");
                    }
                } else {
                    callback.onFailure("Gagal terhubung ke server (kode: " + response.code() + ")");
                }
            }
            @Override
            public void onFailure(Call<LogbookResponse> call, Throwable t) {
                callback.onFailure("Tidak dapat terhubung ke server: " + t.getMessage());
            }
        });
    }

    /**
     * GET /api/v1/artikel — mengambil daftar artikel dengan callback pattern.
     * Mengembalikan list kosong (bukan null) jika data API kosong.
     */
    public static void GetArtikelRequest(String token, ApiCallback<List<InfoResponse.InfoItem>> callback) {
        ApiClient.getApiService().getArtikelRequest("Bearer " + token).enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InfoResponse result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        List<InfoResponse.InfoItem> items = result.getData().getItems();
                        // Kirim list kosong jika null (API belum ada data)
                        callback.onSuccess(items != null ? items : new ArrayList<>());
                    } else {
                        callback.onFailure(result.getMessage() != null ? result.getMessage() : "Gagal ambil artikel");
                    }
                } else {
                    callback.onFailure("Gagal terhubung ke server (kode: " + response.code() + ")");
                }
            }
            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                callback.onFailure("Tidak dapat terhubung ke server: " + t.getMessage());
            }
        });
    }
}
