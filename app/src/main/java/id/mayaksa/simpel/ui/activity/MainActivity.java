package id.mayaksa.simpel.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityMainBinding;
import id.mayaksa.simpel.utils.Functions;

public class MainActivity extends AppCompatActivity {

    Snackbar backSnackbar;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_location, R.id.navigation_history, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        initComponents();
    }

    private void initComponents(){
        backSnackbar = Snackbar.make(binding.getRoot(), "Press Back Button again to Exit", Snackbar.LENGTH_SHORT);

        Functions.dialogWelcome(this, false);
    }

    public Fragment getForegroundFragment(){
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    public void onBackPressed() {
        if(getForegroundFragment().getClass().getSimpleName().equals("HomeFragment")){
            if (backSnackbar.isShown()) {
                super.onBackPressed();
            } else {
                backSnackbar.show();
            }
        }else{
            super.onBackPressed();
        }
    }
}