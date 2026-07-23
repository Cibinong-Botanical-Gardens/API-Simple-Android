package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.response.LogbookResponse;

public class LogbookAdapter extends RecyclerView.Adapter<LogbookAdapter.ViewHolder> {

    private final Context context;
    private final List<LogbookResponse.LogbookItem> items;

    public LogbookAdapter(Context context, List<LogbookResponse.LogbookItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_logbook, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogbookResponse.LogbookItem item = items.get(position);
        if (item == null) return;

        String titleText = item.getJudul() != null ? item.getJudul() : item.getJenisKegiatan();
        holder.tvJenisKegiatan.setText(titleText != null ? titleText : "Logbook");

        String areaText = item.getArea() != null ? item.getArea() : item.getIsi();
        holder.tvArea.setText(areaText != null ? areaText : "-");

        if (item.getIdTree() != null && !item.getIdTree().isEmpty()) {
            holder.tvIdTree.setText("ID Pohon: " + item.getIdTree());
            holder.tvIdTree.setVisibility(View.VISIBLE);
        } else {
            holder.tvIdTree.setVisibility(View.GONE);
        }

        String tanggal = item.getTanggal() != null ? item.getTanggal() : item.getCreatedAt();
        holder.tvTanggal.setText(tanggal != null ? tanggal : "");
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJenisKegiatan, tvArea, tvIdTree, tvTanggal;

        public ViewHolder(View itemView) {
            super(itemView);
            tvJenisKegiatan = itemView.findViewById(R.id.tv_jenis_kegiatan);
            tvArea = itemView.findViewById(R.id.tv_area);
            tvIdTree = itemView.findViewById(R.id.tv_id_tree);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal_logbook);
        }
    }
}
