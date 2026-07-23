package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.ViewHolder> {

    /** Listener untuk event klik item laporan. */
    public interface OnItemClickListener {
        void onItemClick(LaporanResponse.LaporanItem item);
    }

    private final Context context;
    private final List<LaporanResponse.LaporanItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public LaporanAdapter(Context context, List<LaporanResponse.LaporanItem> items) {
        this.context = context;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public void setData(List<LaporanResponse.LaporanItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    /** Set listener untuk event klik pada item. */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /** Append data baru ke bawah list yang sudah ada (dipakai untuk infinite scroll). */
    public void addData(List<LaporanResponse.LaporanItem> newItems) {
        if (newItems == null || newItems.isEmpty()) return;
        int startPosition = this.items.size();
        this.items.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_laporan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LaporanResponse.LaporanItem item = items.get(position);
        if (item == null) return;

        holder.tvJudul.setText(item.getJudul() != null ? item.getJudul() : "");
        holder.tvDeskripsi.setText(item.getDeskripsi() != null ? item.getDeskripsi() : "");

        holder.tvJenisLaporan.setText(item.getJenisLaporan() != null ? item.getJenisLaporan() : "Laporan");
        
        if (item.getPrioritas() != null) {
            holder.tvPrioritas.setText("Prioritas: " + item.getPrioritas());
            holder.tvPrioritas.setVisibility(View.VISIBLE);
        } else {
            holder.tvPrioritas.setVisibility(View.GONE);
        }

        holder.tvStatus.setText(item.getStatus() != null ? item.getStatus() : "Pending");

        if (item.getUser() != null && item.getUser().getNamaUser() != null) {
            holder.tvNamaUser.setText(item.getUser().getNamaUser());
        } else {
            holder.tvNamaUser.setText("Pelapor");
        }

        String tanggal = item.getTanggal() != null ? item.getTanggal() : item.getCreatedAt();
        holder.tvTanggal.setText(tanggal != null ? tanggal : "");

        // Klik item → buka detail
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJenisLaporan, tvPrioritas, tvStatus, tvJudul, tvDeskripsi, tvNamaUser, tvTanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisLaporan = itemView.findViewById(R.id.tv_jenis_laporan);
            tvPrioritas = itemView.findViewById(R.id.tv_prioritas);
            tvStatus = itemView.findViewById(R.id.tv_status_laporan);
            tvJudul = itemView.findViewById(R.id.tv_judul_laporan);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_laporan);
            tvNamaUser = itemView.findViewById(R.id.tv_nama_user_laporan);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal_laporan);
        }
    }
}
