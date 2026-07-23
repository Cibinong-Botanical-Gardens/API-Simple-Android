package id.mayaksa.simpel.model.rest;

import id.mayaksa.simpel.model.rest.response.AuthResponse;
import id.mayaksa.simpel.model.rest.response.InfoResponse;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import id.mayaksa.simpel.model.rest.response.LogbookResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthResponse> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<AuthResponse> registerRequest(@Field("first_name") String firstName,
                                       @Field("last_name") String lastName,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("no_tlp") String phone,
                                       @Field("role") String role);

    @POST("auth/logout")
    Call<ResponseBody> logoutRequest(@Header("Authorization") String token);

    @Multipart
    @POST("laporan/")
    Call<LaporanResponse> createLaporanRequest(
            @Header("Authorization") String token,
            @Part("jenis_laporan") RequestBody jenisLaporan,
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("prioritas") RequestBody prioritas,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("is_koleksi") RequestBody isKoleksi,
            @Part MultipartBody.Part fotoBefore
    );

    @GET("laporan")
    Call<LaporanResponse> getLaporanRequest(@Header("Authorization") String token);

    @GET("logbook")
    Call<LogbookResponse> getLogbookRequest(@Header("Authorization") String token);

    @GET("artikel")
    Call<InfoResponse> getArtikelRequest(@Header("Authorization") String token);
}
