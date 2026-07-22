package id.mayaksa.simpel.model.rest;

import id.mayaksa.simpel.model.rest.response.AuthResponse;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //Authentication
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

//    @POST("auth/me")
//    Call<ResponseBody> currentUserRequest(@Header("Authorization") String token);
//
//    @FormUrlEncoded
//    @POST("auth/forget-password")
//    Call<ResponseBody> forgetPasswordUserRequest(@Field("email") String email);
//
//
//    @FormUrlEncoded
//    @PUT("auth/me/update-profile")
//    Call<ResponseBody> updateUserRequest(@Header("Authorization") String token,
//                                         @Field("username") String username,
//                                         @Field("name") String name,
//                                         @Field("birth_date") String birth_date,
//                                         @Field("address") String address,
//                                         @Field("nik") String nik);
//
//    // Services
//    @GET("services")
//    Call<ServiceResponse> getAllServicesUserRequest(@Header("Authorization") String token);
//
//    @GET("services/{id}")
//    Call<ResponseBody> getOneServicesUserRequest(@Header("Authorization") String token, @Path("id") int id);
//
//    // Informed Concent
//    @FormUrlEncoded
//    @POST("services/{id}/informedConcent")
//    Call<ResponseBody> createInformedConcentUserRequest(@Header("Authorization") String token,
//                                                           @Path("id") int id,
//                                                           @Field("name") String name,
//                                                           @Field("information_recipient") String information_recipient,
//                                                           @Field("patient_relationship") String patient_relationship,
//                                                           @Field("email") String email,
//                                                           @Field("birth_date") String birth_date,
//                                                           @Field("number_id_people") String number_id_people,
//                                                           @Field("address") String address,
//                                                           @Field("gender") String gender,
//                                                           @Field("category") String category,
//                                                           @Field("is_agree") int is_agree);
//
//    // Transaction
//    @POST("services/transaction")
//    Call<ResponseBody> createTransactionUserRequest(@Header("Authorization") String token,
//                                                    @Body RequestBody transaction);
//
////    @Field("property") String property,
////    @Field("location") String location,
////    @Field("service_schedule") String service_schedule,
////    @Field("total_order") int total_order,
////    @Field("payment_type") String payment_type,
////    @Field("nurse_id") int nurse_id,
////    @Field("descriptions") String descriptions,
////    @Field("transaction_details") JSONArray transaction_details
//
//    @GET("transaction/list")
//    Call<HistoryResponse> getAllHistoryUserRequest(@Header("Authorization") String token);
//
//    @GET("services/transaction/{id}")
//    Call<ResponseBody> transactionListByIdUserRequest(@Header("Authorization") String token,
//                                                       @Path("id") int id);
//
//    @GET("nurses/nearby")
//    Call<ResponseBody> getNurseNerbyUserRequest(@Header("Authorization") String token,
//                                                @Query("latitude") Double latitude,
//                                                @Query("longitude") Double longitude,
//                                                @Query("radius") String radius,
//                                                @Query("gender") String gender);

}
