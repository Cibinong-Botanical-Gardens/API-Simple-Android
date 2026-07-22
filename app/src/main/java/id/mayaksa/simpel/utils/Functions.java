package id.mayaksa.simpel.utils;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.ApiFunction;

public class Functions {

    public static final int PERMISSION_REQUEST_CODE = 200;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isNightMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.getResources().getConfiguration().isNightModeActive();
        } else {
            int currentNightMode = context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    return false;
                case Configuration.UI_MODE_NIGHT_YES:
                    return true;
            }
        }

        return false;
    }

    public static void switchMode(Context context) {
        if(Functions.isNightMode(context)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static boolean isPermissionsAllowed(Context context) {
        int result = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(context, CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    public static void dialogWelcome(Context context, Boolean finish) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_welcome);

        Button cancel = bottomSheetDialog.findViewById(R.id.cancel);
        Button agree = bottomSheetDialog.findViewById(R.id.agree);

        assert cancel != null;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        assert agree != null;
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

        if(finish){
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void dialogLoadingLogin(Context context, String email, String pass, Boolean finish) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_loading);

        bottomSheetDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ApiFunction.LoginRequest(context, email, pass);
            }
        }, 3000);

        if(finish){
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void dialogLoadingRegister(Context context, String firstName, String lastName,String email, String password, String phone, String role, Boolean finish) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_loading);

        bottomSheetDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ApiFunction.RegisterRequest(context, firstName, lastName, email, password, phone, role);
            }
        }, 3000);

        if(finish){
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void dialogNoInternet(Context context, Boolean finish) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_no_internet);

        Button open_settings = bottomSheetDialog.findViewById(R.id.open_settings);
        Button retry = bottomSheetDialog.findViewById(R.id.retry);

        assert open_settings != null;
        open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });

        assert retry != null;
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Directs.splashDirect(context, true);
            }
        });

        bottomSheetDialog.show();

        if(finish){
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void dialogLogin(Context context, Boolean finish) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_login);

        EditText emailText = bottomSheetDialog.findViewById(R.id.email);
        EditText passText = bottomSheetDialog.findViewById(R.id.password);
        Button login = bottomSheetDialog.findViewById(R.id.login);

        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert emailText != null;
                String email = emailText.getText().toString().trim();
                assert passText != null;
                String pass = passText.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()) {
                    if (email.isEmpty()) {
                        emailText.setError("Email tidak boleh kosong");
                    }
                    if (pass.isEmpty()) {
                        passText.setError("Password tidak boleh kosong");
                    }
                }else{
                    dialogLoadingLogin(context, email, pass, true);
                }
            }
        });

        bottomSheetDialog.show();

        if(finish){
            bottomSheetDialog.setCancelable(false);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
        }
    }

}
