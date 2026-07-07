package id.mayaksa.simpel.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import id.mayaksa.simpel.databinding.ActivitySplashBinding;
import id.mayaksa.simpel.utils.CheckNetwork;
import id.mayaksa.simpel.utils.Directs;
import id.mayaksa.simpel.utils.Functions;
import id.mayaksa.simpel.utils.SharedPreferences;

public class SplashActivity extends AppCompatActivity {

    Context context = this;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initComponents();
    }

    private void initComponents(){

        if(CheckNetwork.isInternetAvailable(context)){
            if(!SharedPreferences.loadToken(context).isEmpty()){
                requestCurrentUser();
            }else{
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Directs.onBoardingDirect(context,true);
                    }
                }, 3000);
            }
        }else{
            Functions.dialogNoInternet(context,true);
        }
    }

    private void requestCurrentUser(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Directs.mainDirect(context,true);
            }
        }, 3000);
    }


}