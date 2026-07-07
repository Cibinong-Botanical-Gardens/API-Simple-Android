package id.mayaksa.simpel.ui.activity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static id.mayaksa.simpel.utils.Functions.PERMISSION_REQUEST_CODE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.adapter.ObjectViewPagerOnBoardAdapter;
import id.mayaksa.simpel.databinding.ActivityOnBoardingBinding;
import id.mayaksa.simpel.model.OnBoarding;
import id.mayaksa.simpel.utils.Functions;

public class OnBoardingActivity extends AppCompatActivity {

    Context context = this;

    private List<ImageView> dots;
    private ArrayList<OnBoarding> models;
    private ActivityOnBoardingBinding binding;
    private ObjectViewPagerOnBoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onBoardingData();
        addDots();

        initComponents();
    }

    private void initComponents(){
        if(!Functions.isPermissionsAllowed(context)){
            dialogPermission();
        }

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Functions.dialogLogin(context, false);
            }
        });
    }

    private void dialogPermission() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_permission);

        Button cancel = bottomSheetDialog.findViewById(R.id.cancel);
        Button agree = bottomSheetDialog.findViewById(R.id.agree);

        assert cancel != null;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                dialogForced();
            }
        });

        assert agree != null;
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA},
                            PERMISSION_REQUEST_CODE);
                }
            }
        });

        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
    }

    private void dialogForced() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_forced);

        Button back = bottomSheetDialog.findViewById(R.id.back_to_allow);

        assert back != null;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                dialogPermission();
            }
        });

        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
    }

    private void onBoardingData(){
        models = new ArrayList<>();

        models.add(new OnBoarding(R.drawable.img_messaging_amico,"Welcome to Tumbang!", "Sebuah aplikasi untuk koordinasi lokasi/ pohon rawan menggunakan koordinat dan foto, menggantikan penggunaan WhatsApp untuk pin point lokasi."));
        models.add(new OnBoarding(R.drawable.img_storm,"Lapor dengan Koordinat", "Dengan adanya aplikasi ini diharapkan semua orang dapat melaporkan untuk kenyaman para pengunjung Kebun Raya Cibinong"));

        adapter = new ObjectViewPagerOnBoardAdapter(context, models);
        binding.pagerView.setAdapter(adapter);
    }

    public void addDots() {
        dots = new ArrayList<>();

        for(int i = 0; i < models.size(); i++) {
            ImageView dot = new ImageView(context);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.ic_carousel_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            binding.paginationView.addView(dot, params);

            dots.add(dot);
        }

        selectDot(0);
        binding.pagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < models.size(); i++) {
            int drawableId = (i==idx)?(R.drawable.ic_carousel_selected):(R.drawable.ic_carousel_unselected);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(context,"Permission Granted, Now you can access location data and camera.",Toast.LENGTH_LONG).show();
                    else {

                        Toast.makeText(context, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                dialogForced();
                            }
                        }

                    }
                }
                break;
        }
    }
}