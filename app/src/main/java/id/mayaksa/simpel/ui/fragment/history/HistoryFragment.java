package id.mayaksa.simpel.ui.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadLogbook();
    }

    private void loadLogbook() {
        if (binding == null) return;

        // Indikator Loading: tampilkan ProgressBar, sembunyikan empty state & list
        binding.progressBarLogbook.setVisibility(View.VISIBLE);
        binding.tvEmptyLogbook.setVisibility(View.GONE);
        binding.report.setVisibility(View.GONE);

        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLogbookRequest(token, new ApiFunction.ApiCallback<List<LogbookResponse.LogbookItem>>() {
            @Override
            public void onSuccess(List<LogbookResponse.LogbookItem> data) {
                if (getActivity() == null || binding == null) return;

                // Sembunyikan ProgressBar saat data selesai diambil
                binding.progressBarLogbook.setVisibility(View.GONE);

                // Cek empty state
                if (data == null || data.isEmpty()) {
                    binding.tvEmptyLogbook.setVisibility(View.VISIBLE);
                    binding.report.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyLogbook.setVisibility(View.GONE);
                    binding.report.setVisibility(View.VISIBLE);

                    LogbookAdapter adapter = new LogbookAdapter(requireContext(), data);
                    binding.report.setLayoutManager(new LinearLayoutManager(requireContext()));
                    binding.report.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;

                binding.progressBarLogbook.setVisibility(View.GONE);
                binding.tvEmptyLogbook.setText("Gagal memuat data logbook");
                binding.tvEmptyLogbook.setVisibility(View.VISIBLE);
                binding.report.setVisibility(View.GONE);

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
