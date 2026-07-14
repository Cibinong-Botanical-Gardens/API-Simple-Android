package id.mayaksa.simpel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import id.mayaksa.simpel.ui.activity.MainActivity;
import id.mayaksa.simpel.ui.activity.OnBoardingActivity;
import id.mayaksa.simpel.ui.activity.PrototypeMapsActivity;
import id.mayaksa.simpel.ui.activity.RegisterActivity;
import id.mayaksa.simpel.ui.activity.ReportActivity;
import id.mayaksa.simpel.ui.activity.SplashActivity;

public class Directs {

    public static void splashDirect(Context context, Boolean finish){
        context.startActivity(new Intent(context, SplashActivity.class));
        if (finish){
            ((Activity)(context)).finish();
        }
    }

    public static void onBoardingDirect(Context context, Boolean finish){
        context.startActivity(new Intent(context, OnBoardingActivity.class));
        if (finish){
            ((Activity)(context)).finish();
        }
    }

    public static void mainDirect(Context context, Boolean finish){
        context.startActivity(new Intent(context, MainActivity.class));
        if (finish){
            ((Activity)(context)).finish();
        }
    }

    public static void registerDirect(Context context, Boolean finish){
        context.startActivity(new Intent(context, RegisterActivity.class));
        if (finish){
            ((Activity)(context)).finish();
        }
    }

    public static void reportDirect(Context context, Boolean finish){
        context.startActivity(new Intent(context, ReportActivity.class));
        if (finish){
            ((Activity)(context)).finish();
        }
    }

    public static void mapPrototype(Context context, Boolean finish){
        context.startActivity(new Intent(context, PrototypeMapsActivity.class));
        if (finish){
            ((Activity)(context)).finish();
        }
    }

}
