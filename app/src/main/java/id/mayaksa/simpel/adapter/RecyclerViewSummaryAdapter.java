package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.ApiClient;
import id.mayaksa.simpel.model.rest.response.LaporanResponse;

public class RecyclerViewSummaryAdapter extends RecyclerView.Adapter<RecyclerViewSummaryAdapter.ViewHolder> {

    private final Context context;
    private final List<LaporanResponse.LaporanItem> itemData = new ArrayList<>();

    public RecyclerViewSummaryAdapter(Context context, List<LaporanResponse.LaporanItem> itemData) {
        this.context = context;
        if (itemData != null) {
            this.itemData.addAll(itemData);
        }
    }

    public void setData(List<LaporanResponse.LaporanItem> newData) {
        this.itemData.clear();
        if (newData != null) {
            this.itemData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LaporanResponse.LaporanItem item = itemData.get(position);
        if (item == null) return;

        holder.title.setText(item.getJudul() != null ? item.getJudul() : "");

        if (item.getUser() != null && item.getUser().getNamaUser() != null) {
            holder.desc.setText(item.getUser().getNamaUser());
        } else {
            holder.desc.setText(item.getDeskripsi() != null ? item.getDeskripsi() : "");
        }

        if (item.getTanggal() != null) {
            holder.tvTime.setText(item.getTanggal());
        } else if (item.getCreatedAt() != null) {
            holder.tvTime.setText(item.getCreatedAt());
        } else {
            holder.tvTime.setText("");
        }
        
        String imagePath = item.getFotoBefore();
        if (imagePath != null && !imagePath.isEmpty()) {
            String fullUrl;
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                fullUrl = imagePath;
            } else {
                String baseUrl = ApiClient.BASE_URL_API.replace("api/v1/", "");
                fullUrl = baseUrl + (imagePath.startsWith("/") ? imagePath.substring(1) : imagePath);
            }

            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.bg_card)
                    .error(R.drawable.ic_logo_tumbang)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.bg_card);
        }
    }

    @Override
    public int getItemCount() {
        return itemData != null ? itemData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, desc, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.name);
            tvTime = itemView.findViewById(R.id.time);
        }
    }
}
