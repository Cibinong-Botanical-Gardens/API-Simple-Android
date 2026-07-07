package id.mayaksa.simpel.ui.fragment.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import id.mayaksa.simpel.adapter.RecyclerViewReportAdapter;
import id.mayaksa.simpel.databinding.FragmentLocationBinding;
import id.mayaksa.simpel.model.Report;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;

    private ArrayList<Report> report;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLocationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents();

        return root;
    }

    void initComponents(){
        loadReport();
    }

    private void loadReport(){
        report = new ArrayList<>();

        report.add(new Report("2j",0,"Sumatera Tumbang", "Lapor Pak, Ada pohon tumbang di area sumatera", "-6.489797", "106.854330",0,"Bayu", "Mayaksa", "admin"));
        report.add(new Report("2j",0,"Kalimantan Ancur", "Buat Mayaksa, Kalimantannya coba dibenahi dengan tim.", "-6.490910", "106.856221",0,"Sopyan", "DPKI", "admin"));
        report.add(new Report("2j",0,"Gatau ini dimana", "Aduh, gatau ini dimana, banyak asset hancur", "-6.489216", "106.856346", 0,"Ardityo Cahyo", "Orang Luar", "dev"));

        final RecyclerViewReportAdapter summaries = new RecyclerViewReportAdapter(getActivity(), report);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.contentLocation.report.setLayoutManager(linearLayoutManager);
        binding.contentLocation.report.setAdapter(summaries);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}