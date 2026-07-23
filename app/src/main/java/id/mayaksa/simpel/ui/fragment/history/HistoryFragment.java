package id.mayaksa.simpel.ui.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import id.mayaksa.simpel.adapter.LogbookAdapter;
import id.mayaksa.simpel.databinding.FragmentHistoryBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.model.rest.response.LogbookResponse;
import id.mayaksa.simpel.utils.SharedPreferences;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        
        loadLogbook();
        
        return binding.getRoot();
    }

    private void loadLogbook() {
        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLogbookRequest(token, new ApiFunction.ApiCallback<List<LogbookResponse.LogbookItem>>() {
            @Override
            public void onSuccess(List<LogbookResponse.LogbookItem> data) {
                if (getActivity() == null) return;
                LogbookAdapter adapter = new LogbookAdapter(getActivity(), data);
                binding.report.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.report.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
