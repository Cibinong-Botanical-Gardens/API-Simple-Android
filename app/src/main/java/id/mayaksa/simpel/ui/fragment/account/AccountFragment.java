package id.mayaksa.simpel.ui.fragment.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import id.mayaksa.simpel.databinding.FragmentAccountBinding;
import id.mayaksa.simpel.utils.Directs;
import id.mayaksa.simpel.utils.Functions;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents();

        return root;
    }

    void initComponents(){
        binding.adminMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Directs.registerDirect(requireContext(),true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}