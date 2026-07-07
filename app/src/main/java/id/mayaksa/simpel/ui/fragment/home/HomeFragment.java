package id.mayaksa.simpel.ui.fragment.home;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.adapter.ObjectViewPagerInfoAdapter;
import id.mayaksa.simpel.adapter.RecyclerViewSummaryAdapter;
import id.mayaksa.simpel.databinding.FragmentHomeBinding;
import id.mayaksa.simpel.model.Info;
import id.mayaksa.simpel.model.Report;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<ImageView> dots;

    private ArrayList<Report> summary;
    private ArrayList<Info> info;

    Timer timer;
    int currentPage ;
    final long DELAY_MS ;
    final long PERIOD_MS;

    public HomeFragment() {
        currentPage = 0;
        DELAY_MS = 500;
        PERIOD_MS = 3000;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents();

        return root;
    }

    void initComponents(){
        loadInfo();
        loadSummary();
    }

    private void loadInfo(){
        info = new ArrayList<>();

        info.add(new Info(0, "Lapor Ketua", "Kejutan Harian Diskon S.D 90%"));
        info.add(new Info(0, "Tor Monitor Ketua", "Tor Monitor Ketua"));
        info.add(new Info(0, "DPKI", "DPKI"));

        ObjectViewPagerInfoAdapter adapter = new ObjectViewPagerInfoAdapter(getActivity(), info);
        binding.pagerViewInfo.setAdapter(adapter);
        binding.pagerViewInfo.setPadding(35,0,45,0);

        addDotsInfo();
        autoSwipeInfo(adapter);
    }

    private void loadSummary(){
        summary = new ArrayList<>();

        summary.add(new Report("2j",0,"Sumatera Tumbang", "Lapor Pak, Ada pohon tumbang di area sumatera", "-6.489797", "106.854330",0,"Bayu", "Mayaksa", "admin"));
        summary.add(new Report("2j",0,"Kalimantan Ancur", "Buat Mayaksa, Kalimantannya coba dibenahi dengan tim.", "-6.490910", "106.856221",0,"Sopyan", "DPKI", "admin"));
        summary.add(new Report("2j",0,"Gatau ini dimana", "Aduh, gatau ini dimana, banyak asset hancur", "-6.489216", "106.856346", 0,"Ardityo Cahyo", "Orang Luar", "dev"));

        final RecyclerViewSummaryAdapter summaries = new RecyclerViewSummaryAdapter(getActivity(), summary);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.pagerViewSummary.setLayoutManager(linearLayoutManager);
        binding.pagerViewSummary.setAdapter(summaries);
    }

    public void addDotsInfo() {
        dots = new ArrayList<>();

        for(int i = 0; i < info.size(); i++) {
            ImageView dot = new ImageView(getActivity());
            dot.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_carousel_unselected,null));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            binding.paginationViewEventPromo.addView(dot, params);

            dots.add(dot);
        }

        selectDotEventPromo(0);
        binding.pagerViewInfo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                selectDotEventPromo(position);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void selectDotEventPromo(int position) {
        Resources res = getResources();
        for(int i = 0; i < info.size(); i++) {
            int drawableId = (i==position)?(R.drawable.ic_carousel_selected):(R.drawable.ic_carousel_unselected);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    private void autoSwipeInfo(ObjectViewPagerInfoAdapter adapter){
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == adapter.getCount()) {
                    currentPage = 0;
                }
                binding.pagerViewInfo.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
//        binding = null;
    }
}