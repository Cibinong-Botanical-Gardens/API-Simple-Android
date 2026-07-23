package id.mayaksa.simpel.ui.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import id.mayaksa.simpel.databinding.FragmentHistoryBinding;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    private static final String[] TAB_TITLES = new String[]{
            "All",
            "Pending",
            "Diproses",
            "Selesai",
            "Ditolak"
    };

    private static final String[] TAB_STATUS_VALUES = new String[]{
            null,         // All (no status query)
            "pending",
            "diproses",
            "selesai",
            "ditolak"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup ViewPager2 Adapter
        HistoryPagerAdapter pagerAdapter = new HistoryPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);

        // Attach TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(TAB_TITLES[position]);
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static class HistoryPagerAdapter extends FragmentStateAdapter {

        public HistoryPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            String status = TAB_STATUS_VALUES[position];
            return LaporanTabFragment.newInstance(status);
        }

        @Override
        public int getItemCount() {
            return TAB_TITLES.length;
        }
    }
}
