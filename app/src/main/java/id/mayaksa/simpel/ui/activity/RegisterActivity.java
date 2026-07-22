package id.mayaksa.simpel.ui.activity;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.databinding.ActivityMainBinding;
import id.mayaksa.simpel.databinding.ActivityRegisterBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.utils.Functions;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initComponents();
    }

    private void initComponents(){
        Context context = this;

        String[] items = {"Mayaksa - User", "Mayaksa - Admin", "DPKI - User", "DPKI - Admin", "SuperAdmin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, items);

        binding.roleDropdown.setAdapter(adapter);

        binding.passwordConfirm.setEnabled(false);

//        if(Functions.isNightMode(this)){
//            binding.logo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_plantau_logo_light,null));
//        }else{
//            binding.logo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_plantau_logo_dark,null));
//        }

        binding.firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.firstName.getText().toString().trim().isEmpty()){
                    binding.firstName.setError("Name does not exist.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!binding.firstName.getText().toString().trim().isEmpty()){
                    binding.firstName.setError(null);
                }
            }
        });

        binding.roleDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.roleDropdown.getText().toString().trim().isEmpty()){
                    binding.roleDropdown.setError("Name does not exist.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!binding.roleDropdown.getText().toString().trim().isEmpty()){
                    binding.roleDropdown.setError(null);
                }
            }
        });

        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.phone.getText().toString().trim().isEmpty()){
                    binding.phone.setError("Phone Number does not exist.");
                }else{
                    if(binding.phone.getText().toString().trim().length() < 8) {
                        binding.phone.setError("Phone number is too short.");
                    }else{
                        if(binding.phone.getText().toString().trim().charAt(0) == '0'){
                            binding.phone.setText(binding.phone.getText().toString().trim().substring(1));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.email.getText().toString().trim().isEmpty()){
                    binding.email.setError("Email does not exist.");
                }else{
                    if(!Functions.isValidEmail(binding.email.getText().toString().trim())){
                        binding.email.setError("Invalid email.");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!binding.email.getText().toString().trim().isEmpty() && Functions.isValidEmail(binding.email.getText().toString().trim())){
                    binding.email.setError(null);
                }
            }
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.password.getText().toString().trim().isEmpty()){
                    binding.password.setError("Password does not exist.");
                    binding.passwordConfirm.setEnabled(false);
                }else{
                    if(binding.password.getText().toString().trim().length() < 8){
                        binding.password.setError("Password is too short.");
                        binding.passwordConfirm.setEnabled(false);
                    }else{
                        binding.passwordConfirm.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!binding.passwordConfirm.getText().toString().trim().equals(binding.password.getText().toString().trim())){
                    binding.passwordConfirm.setError("The passwords entered are not the same.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = binding.firstName.getText().toString().trim();
                String lastName = binding.lastName.getText().toString().trim();
                String role = binding.roleDropdown.getText().toString().trim();
                String phone = binding.phone.getText().toString().trim();
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                String passwordConfirm = binding.passwordConfirm.getText().toString().trim();

                if (role.equals("Mayaksa - User")){
                    role = "m_user";
                } else if(role.equals("Mayaksa - Admin")){
                    role = "m_admin";
                } else if (role.equals("DPKI - User")){
                    role = "d_user";
                } else if (role.equals("DPKI - Admin")){
                    role = "d_admin";
                } else if (role.equals("SuperAdmin")){
                    role = "root";
                }

                if(firstName.isEmpty() || lastName.isEmpty() || role.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    if (firstName.isEmpty()) {
                        binding.firstName.setError("Name does not exist.");
                    }
                    if(binding.roleDropdown.getText().toString().trim().isEmpty()){
                        binding.roleDropdown.setError("Role does not exist.");
                    }
                    if(binding.phone.getText().toString().trim().isEmpty()){
                        binding.phone.setError("Phone Number does not exist.");
                    }
                    if(binding.email.getText().toString().trim().isEmpty()){
                        binding.email.setError("Email does not exist.");
                    }else{
                        if(!Functions.isValidEmail(binding.email.getText().toString().trim())){
                            binding.email.setError("Invalid email.");
                        }
                    }
                    if(binding.password.getText().toString().trim().isEmpty()){
                        binding.password.setError("Password does not exist.");
                    }
                    if(binding.passwordConfirm.getText().toString().trim().isEmpty()){
                        binding.passwordConfirm.setError("Password does not exist.");
                    }
                }else{
                    Functions.dialogLoadingRegister(context, firstName, lastName, email, password, phone, role, true);
                }

            }
        });

    }
}