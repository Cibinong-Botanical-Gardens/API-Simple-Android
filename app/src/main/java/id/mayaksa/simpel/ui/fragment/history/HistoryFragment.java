package id.mayaksa.simpel.ui.fragment.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import id.mayaksa.simpel.adapter.RecyclerViewReportAdapter;
import id.mayaksa.simpel.databinding.FragmentHistoryBinding;
import id.mayaksa.simpel.model.Report;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    private ArrayList<Report> report;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initComponents();

        return root;
    }

    void initComponents(){
        loadReport();
    }

    private void loadReport(){
        report = new ArrayList<>();

        report.add(new Report("2j",0,"Sumatra knp Pak Bayu?", "Lapor Pak, Ada pohon tumbang di area sumatera", "-6.489797", "106.854330",0,"Sopyan", "DPKI", "admin"));
        report.add(new Report("2j",0,"Loch kan ambruk", "Buat Mayaksa, Kalimantannya coba dibenahi dengan tim.", "-6.490910", "106.856221",0,"Sopyan", "DPKI", "admin"));
        report.add(new Report("2j",0,"Apa tuch?", "Aduh, gatau ini dimana, banyak asset hancur", "-6.489216", "106.856346", 0,"Sopyan", "DPKI", "admin"));

        final RecyclerViewReportAdapter summaries = new RecyclerViewReportAdapter(getActivity(), report);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.report.setLayoutManager(linearLayoutManager);
        binding.report.setAdapter(summaries);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}