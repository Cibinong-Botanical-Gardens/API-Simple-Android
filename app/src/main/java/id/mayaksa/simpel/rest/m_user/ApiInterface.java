//package id.mayaksa.simpel.rest.m_user;
//
////import com.humic.android.homecare.rest.user.response.HistoryResponse;
////import com.humic.android.homecare.rest.user.response.ServiceResponse;
//
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.GET;
//import retrofit2.http.Header;
//import retrofit2.http.POST;
//import retrofit2.http.PUT;
//import retrofit2.http.Path;
//import retrofit2.http.Query;
//
//public interface ApiInterface {
//
//    //Authentication
//    @FormUrlEncoded
//    @POST("auth/login")
//    Call<ResponseBody> loginUserRequest(@Field("username") String username,
//                                        @Field("password") String password);
//
//    @FormUrlEncoded
//    @POST("auth/register")
//    Call<ResponseBody> registerUserRequest(@Field("username") String username,
//                                           @Field("password") String password,
//                                           @Field("name") String name,
//                                           @Field("birth_date") String birth_date,
//                                           @Field("address") String address,
//                                           @Field("nik") String nik,
//                                           @Field("gender") String gender,
//                                           @Field("age") String age);
//
//    @POST("auth/me")
//    Call<ResponseBody> currentUserRequest(@Header("Authorization") String token);
//
//    @FormUrlEncoded
//    @POST("auth/forget-password")
//    Call<ResponseBody> forgetPasswordUserRequest(@Field("email") String email);
//
//    @POST("auth/logout")
//    Call<ResponseBody> logoutUserRequest(@Header("Authorization") String token);
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
//
//}
