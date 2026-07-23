package id.mayaksa.simpel.ui.fragment.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.mayaksa.simpel.adapter.LaporanAdapter;
import id.mayaksa.simpel.databinding.FragmentLaporanTabBinding;
import id.mayaksa.simpel.model.rest.ApiFunction;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;
import id.mayaksa.simpel.ui.activity.LaporanDetailActivity;
import id.mayaksa.simpel.utils.SharedPreferences;

public class LaporanTabFragment extends Fragment {

    private static final String ARG_STATUS = "arg_status";

    private String statusFilter;
    private FragmentLaporanTabBinding binding;
    private LaporanAdapter adapter;

    // Pagination state
    private int currentPage = 1;
    private int lastPage = 1;
    private boolean isLoading = false;

    /**
     * Launcher untuk membuka LaporanDetailActivity.
     * Jika result RESULT_OK → refresh data tab ini agar list terupdate.
     */
    private final ActivityResultLauncher<Intent> detailLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Update berhasil di detail — refresh tab ini
                    refreshData();
                }
            });

    public static LaporanTabFragment newInstance(String status) {
        LaporanTabFragment fragment = new LaporanTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            statusFilter = getArguments().getString(ARG_STATUS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLaporanTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Custom empty state text per status
        if (statusFilter != null && !statusFilter.isEmpty()) {
            binding.tvEmptyState.setText("Belum ada laporan dengan status " + statusFilter);
        } else {
            binding.tvEmptyState.setText("Belum ada laporan");
        }

        // Setup RecyclerView & Adapter
        adapter = new LaporanAdapter(requireContext(), null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.rvLaporan.setLayoutManager(layoutManager);
        binding.rvLaporan.setAdapter(adapter);

        // Klik item → buka halaman detail laporan
        adapter.setOnItemClickListener(item -> {
            Intent intent = LaporanDetailActivity.createIntent(requireContext(), item);
            detailLauncher.launch(intent);
        });

        // Pull-to-refresh
        binding.swipeRefresh.setOnRefreshListener(this::refreshData);

        // Infinite Scroll Listener
        binding.rvLaporan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // Scrolling ke bawah
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && currentPage < lastPage) {
                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount && firstVisibleItem >= 0) {
                            currentPage++;
                            loadMoreData(currentPage);
                        }
                    }
                }
            }
        });

        // Initial Data Fetch
        loadFirstPage();
    }

    private void refreshData() {
        currentPage = 1;
        lastPage = 1;
        loadFirstPage();
    }

    private void loadFirstPage() {
        if (binding == null) return;

        if (!binding.swipeRefresh.isRefreshing()) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        binding.tvEmptyState.setVisibility(View.GONE);
        binding.rvLaporan.setVisibility(View.GONE);

        isLoading = true;
        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLaporanFilteredRequest(token, statusFilter, 1, new ApiFunction.ApiCallback<LaporanResponse.DataWrapper>() {
            @Override
            public void onSuccess(LaporanResponse.DataWrapper data) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;

                binding.progressBar.setVisibility(View.GONE);
                if (binding.swipeRefresh.isRefreshing()) {
                    binding.swipeRefresh.setRefreshing(false);
                }

                lastPage = data.getLastPage();
                List<LaporanResponse.LaporanItem> items = data.getItems();

                if (items == null || items.isEmpty()) {
                    binding.tvEmptyState.setVisibility(View.VISIBLE);
                    binding.rvLaporan.setVisibility(View.GONE);
                    adapter.setData(null);
                } else {
                    binding.tvEmptyState.setVisibility(View.GONE);
                    binding.rvLaporan.setVisibility(View.VISIBLE);
                    adapter.setData(items);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;

                binding.progressBar.setVisibility(View.GONE);
                if (binding.swipeRefresh.isRefreshing()) {
                    binding.swipeRefresh.setRefreshing(false);
                }

                binding.tvEmptyState.setText("Gagal memuat data laporan");
                binding.tvEmptyState.setVisibility(View.VISIBLE);
                binding.rvLaporan.setVisibility(View.GONE);

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoreData(int page) {
        if (binding == null) return;

        isLoading = true;
        binding.progressBarLoadMore.setVisibility(View.VISIBLE);

        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLaporanFilteredRequest(token, statusFilter, page, new ApiFunction.ApiCallback<LaporanResponse.DataWrapper>() {
            @Override
            public void onSuccess(LaporanResponse.DataWrapper data) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;
                binding.progressBarLoadMore.setVisibility(View.GONE);

                lastPage = data.getLastPage();
                List<LaporanResponse.LaporanItem> items = data.getItems();
                if (items != null && !items.isEmpty()) {
                    adapter.addData(items);
                }
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null || binding == null) return;
                isLoading = false;
                binding.progressBarLoadMore.setVisibility(View.GONE);

                Toast.makeText(requireContext(), "Gagal memuat lebih banyak: " + message, Toast.LENGTH_SHORT).show();
                currentPage--;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        adapter = null;
    }
}
