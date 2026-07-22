package id.mayaksa.simpel.model.rest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.mayaksa.simpel.model.User;
import id.mayaksa.simpel.model.rest.response.AuthResponse;
import id.mayaksa.simpel.utils.Directs;
import id.mayaksa.simpel.utils.SharedPreferences;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiFunction {

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

//                        SharedPreferences.saveToken(context, token);
                        SharedPreferences.saveUser(context, token, user);

                        Toast.makeText((Activity) context, result.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText((Activity) context, "Login berhasil", Toast.LENGTH_SHORT).show();

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

    public static void RegisterRequest(Context context, String firstName, String lastName,String email, String password, String phone, String role) {
        Call<AuthResponse> call = ApiClient.getApiService().registerRequest(firstName, lastName, email, password, phone, role);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse result = response.body();

                    if (result.isSuccess()) {
                        Directs.onBoardingDirect(context, true);

                        Toast.makeText((Activity) context, result.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText((Activity) context, "Login berhasil", Toast.LENGTH_SHORT).show();
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

    public static void LogoutRequest(Context context, String token) {
        Call<ResponseBody> call = ApiClient.getApiService().logoutRequest("Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("Response", response.toString());
                Log.i("Token", SharedPreferences.loadToken(context));
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody result = response.body();

                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("success").equals("true")) {
                            Toast.makeText((Activity) context, "Anda Keluar", Toast.LENGTH_SHORT).show();
                            Directs.splashDirect(context, true);
                            SharedPreferences.clearUser(context);
                        } else {
                            String error_message = jsonRESULTS.getString("error_msg");
                            Toast.makeText((Activity) context, error_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText((Activity) context, "Login gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText((Activity) context, "Gagal terhubung ke server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}