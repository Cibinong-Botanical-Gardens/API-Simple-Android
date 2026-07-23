package id.mayaksa.simpel.ui.fragment.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import id.mayaksa.simpel.adapter.LaporanAdapter;
import id.mayaksa.simpel.databinding.FragmentLocationBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import id.mayaksa.simpel.utils.SharedPreferences;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private LaporanAdapter laporanAdapter;

    // Pagination state
    private int currentPage = 1;
    private int lastPage = 1;
    private boolean isLoading = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        laporanAdapter = new LaporanAdapter(requireContext(), null);
        binding.contentLocation.report.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.contentLocation.report.setAdapter(laporanAdapter);

        // Setup infinite scroll via NestedScrollView
        // RecyclerView ada di dalam NestedScrollView, jadi scroll listener dipasang ke NestedScrollView
        binding.contentLocation.getRoot().setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    // Cek apakah sudah scroll ke bawah
                    if (scrollY >= (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        if (!isLoading && currentPage < lastPage) {
                            currentPage++;
                            loadMoreLaporan(currentPage);
                        }
                    }
                });

        // Load halaman pertama
        loadFirstPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh dari awal setiap kali tab ini ditampilkan
        resetAndReload();
    }

    private void resetAndReload() {
        currentPage = 1;
        lastPage = 1;
        if (laporanAdapter != null) laporanAdapter.setData(null);
        loadFirstPage();
    }

    private void loadFirstPage() {
        if (binding == null) return;

        binding.contentLocation.progressBarLaporan.setVisibility(View.VISIBLE);
        binding.contentLocation.tvEmptyLaporan.setVisibility(View.GONE);
        binding.contentLocation.report.setVisibility(View.GONE);

        isLoading = true;
        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLaporanPagedRequest(token, 1, new ApiFunction.ApiCallback<LaporanResponse.DataWrapper>() {
            @Override
            public void onSuccess(LaporanResponse.DataWrapper data) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;

                binding.contentLocation.progressBarLaporan.setVisibility(View.GONE);

                lastPage = data.getLastPage();
                List<LaporanResponse.LaporanItem> items = data.getItems();

                if (items == null || items.isEmpty()) {
                    binding.contentLocation.tvEmptyLaporan.setVisibility(View.VISIBLE);
                    binding.contentLocation.report.setVisibility(View.GONE);
                } else {
                    binding.contentLocation.tvEmptyLaporan.setVisibility(View.GONE);
                    binding.contentLocation.report.setVisibility(View.VISIBLE);
                    laporanAdapter.setData(items);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;

                binding.contentLocation.progressBarLaporan.setVisibility(View.GONE);
                binding.contentLocation.tvEmptyLaporan.setText("Gagal memuat laporan");
                binding.contentLocation.tvEmptyLaporan.setVisibility(View.VISIBLE);
                binding.contentLocation.report.setVisibility(View.GONE);

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreLaporan(int page) {
        if (binding == null) return;

        isLoading = true;
        binding.contentLocation.progressBarLoadMore.setVisibility(View.VISIBLE);

        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLaporanPagedRequest(token, page, new ApiFunction.ApiCallback<LaporanResponse.DataWrapper>() {
            @Override
            public void onSuccess(LaporanResponse.DataWrapper data) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;
                binding.contentLocation.progressBarLoadMore.setVisibility(View.GONE);

                lastPage = data.getLastPage();
                List<LaporanResponse.LaporanItem> items = data.getItems();
                if (items != null && !items.isEmpty()) {
                    laporanAdapter.addData(items);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;
                binding.contentLocation.progressBarLoadMore.setVisibility(View.GONE);

                Toast.makeText(requireContext(), "Gagal memuat lebih banyak: " + message, Toast.LENGTH_SHORT).show();
                // Mundur page agar bisa coba load lagi saat scroll
                currentPage--;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        laporanAdapter = null;
    }
}