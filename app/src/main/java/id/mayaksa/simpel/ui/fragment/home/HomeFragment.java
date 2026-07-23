package id.mayaksa.simpel.ui.fragment.home;

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

import id.mayaksa.simpel.adapter.ObjectViewPagerInfoAdapter;
import id.mayaksa.simpel.adapter.RecyclerViewSummaryAdapter;
import id.mayaksa.simpel.databinding.FragmentHomeBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.model.rest.response.InfoResponse;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import id.mayaksa.simpel.utils.SharedPreferences;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerViewSummaryAdapter summaryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Swipe refresh listener
        binding.swipeView.setOnRefreshListener(() -> {
            loadInfo();
            loadSummary();
        });

        loadInfo();
        loadSummary();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSummary();
    }

    private void loadInfo() {
        if (binding == null) return;

        binding.progressBarInfo.setVisibility(View.VISIBLE);
        binding.tvEmptyInfo.setVisibility(View.GONE);
        binding.pagerViewInfo.setVisibility(View.GONE);

        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetArtikelRequest(token, new ApiFunction.ApiCallback<List<InfoResponse.InfoItem>>() {
            @Override
            public void onSuccess(List<InfoResponse.InfoItem> data) {
                if (getActivity() == null || binding == null) return;

                binding.progressBarInfo.setVisibility(View.GONE);
                if (binding.swipeView.isRefreshing()) {
                    binding.swipeView.setRefreshing(false);
                }

                if (data == null || data.isEmpty()) {
                    binding.tvEmptyInfo.setVisibility(View.VISIBLE);
                    binding.pagerViewInfo.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyInfo.setVisibility(View.GONE);
                    binding.pagerViewInfo.setVisibility(View.VISIBLE);

                    ObjectViewPagerInfoAdapter adapter = new ObjectViewPagerInfoAdapter(requireContext(), data);
                    binding.pagerViewInfo.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;

                binding.progressBarInfo.setVisibility(View.GONE);
                if (binding.swipeView.isRefreshing()) {
                    binding.swipeView.setRefreshing(false);
                }

                binding.tvEmptyInfo.setText("Gagal memuat artikel");
                binding.tvEmptyInfo.setVisibility(View.VISIBLE);
                binding.pagerViewInfo.setVisibility(View.GONE);

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSummary() {
        if (binding == null) return;

        binding.progressBarLaporan.setVisibility(View.VISIBLE);
        binding.tvEmptyLaporan.setVisibility(View.GONE);
        binding.pagerViewSummary.setVisibility(View.GONE);

        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLaporanRequest(token, new ApiFunction.ApiCallback<List<LaporanResponse.LaporanItem>>() {
            @Override
            public void onSuccess(List<LaporanResponse.LaporanItem> data) {
                if (getActivity() == null || binding == null) return;

                binding.progressBarLaporan.setVisibility(View.GONE);
                if (binding.swipeView.isRefreshing()) {
                    binding.swipeView.setRefreshing(false);
                }

                if (data == null || data.isEmpty()) {
                    binding.tvEmptyLaporan.setVisibility(View.VISIBLE);
                    binding.pagerViewSummary.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyLaporan.setVisibility(View.GONE);
                    binding.pagerViewSummary.setVisibility(View.VISIBLE);

                    if (summaryAdapter == null) {
                        summaryAdapter = new RecyclerViewSummaryAdapter(requireContext(), data);
                        binding.pagerViewSummary.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                        binding.pagerViewSummary.setAdapter(summaryAdapter);
                    } else {
                        summaryAdapter.setData(data);
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;

                binding.progressBarLaporan.setVisibility(View.GONE);
                if (binding.swipeView.isRefreshing()) {
                    binding.swipeView.setRefreshing(false);
                }

                binding.tvEmptyLaporan.setText("Gagal memuat laporan");
                binding.tvEmptyLaporan.setVisibility(View.VISIBLE);
                binding.pagerViewSummary.setVisibility(View.GONE);

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        // Reset adapter agar onViewCreated berikutnya selalu attach adapter baru ke RecyclerView baru
        summaryAdapter = null;
    }
}
