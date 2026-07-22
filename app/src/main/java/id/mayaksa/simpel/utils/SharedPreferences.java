package id.mayaksa.simpel.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import id.mayaksa.simpel.model.User;

public class SharedPreferences {
    // File SharedPreference Name
    public static final String SHARED_PREFS_HISTORY = "SHARED_PREFS_HISTORY";
    public static final String SHARED_PREFS_USER = "SHARED_PREFS_USER";
    public static final String SHARED_PREFS_TOKEN = "SHARED_PREFS_TOKEN";

    // Public Variable
    public static final String onBoarding = "onBoarding";
    public static final String authToken = "authToken";
    public static final String idUser = "idUser";
    public static final String namaDepan = "namaDepan";
    public static final String namaBelakang = "namaBelakang";
    public static final String email = "email";
    public static final String role = "role";


    // Delete Function
    public static void clearUser(Context context) {
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_USER, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    // Save Function
    public static void saveUser(Context context, String token, User user){
        android.content.SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_USER, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString(authToken, token);
        editor.putInt(idUser, user.getIdUser());
        String[] namaUser = user.getNamaUser().trim().split("\\s+");
        editor.putString(namaDepan, namaUser[0]);
        editor.putString(namaBelakang, namaUser[1]);
        editor.putString(email, user.getEmail());
        editor.putString(role, user.getRole());
        editor.apply();
    }

    public static void saveOnBoarding(Context context, String status){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HISTORY, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(onBoarding, status);
        editor.apply();
    }

    // Load Function
    public static String loadOnBoarding(Context context){
        final android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_HISTORY, MODE_PRIVATE);

        return sharedPreferences.getString(onBoarding, "");
    }

    public static String loadToken(Context context){
        final android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_USER, MODE_PRIVATE);

        return sharedPreferences.getString(authToken, "");
    }
}
