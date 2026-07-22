package id.mayaksa.simpel.model.rest;

import static id.mayaksa.simpel.model.rest.RetrofitClient.getClient;

public class ApiClient {
    public static final String BASE_URL_API =
            "https://kebunraya.fauzan.online/api/v1/";

    public static ApiInterface getApiService() {
        return getClient(BASE_URL_API).create(ApiInterface.class);
    }
//
//    public static id.mayaksa.simpel.rest.nurse.ApiInterface getNurseAPIService(){
//        return id.mayaksa.simpel.rest.RetrofitClient.getClient(BASE_URL_API).create(id.mayaksa.simpel.rest.nurse.ApiInterface.class);
//    }
}
