package id.mayaksa.simpel.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        
        loadInfo();
        loadSummary();
        
        return binding.getRoot();
    }

    private void loadInfo() {
        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetArtikelRequest(token, new ApiFunction.ApiCallback<List<InfoResponse.InfoItem>>() {
            @Override
            public void onSuccess(List<InfoResponse.InfoItem> data) {
                if (getActivity() == null) return;
                ObjectViewPagerInfoAdapter adapter = new ObjectViewPagerInfoAdapter(getActivity(), data);
                binding.pagerViewInfo.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSummary() {
        String token = SharedPreferences.loadToken(requireContext());
        ApiFunction.GetLaporanRequest(token, new ApiFunction.ApiCallback<List<LaporanResponse.LaporanItem>>() {
            @Override
            public void onSuccess(List<LaporanResponse.LaporanItem> data) {
                if (getActivity() == null) return;
                RecyclerViewSummaryAdapter adapter = new RecyclerViewSummaryAdapter(getActivity(), data);
                binding.pagerViewSummary.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                binding.pagerViewSummary.setAdapter(adapter);
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
