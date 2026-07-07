package id.mayaksa.simpel.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

public class SharedPreferences {
    // File SharedPreference Name
    public static final String SHARED_PREFS_HISTORY = "SHARED_PREFS_HISTORY";
    public static final String SHARED_PREFS_USER = "SHARED_PREFS_USER";

    // Public Variable
    public static final String onBoarding = "onBoarding";
    public static final String authToken = "authToken";

    // Save Function
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
