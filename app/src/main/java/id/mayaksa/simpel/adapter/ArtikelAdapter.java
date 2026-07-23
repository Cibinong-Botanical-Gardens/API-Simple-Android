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

import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.ApiClient;
import id.mayaksa.simpel.model.rest.response.InfoResponse;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ViewHolder> {

    private final Context context;
    private final List<InfoResponse.InfoItem> items;

    public ArtikelAdapter(Context context, List<InfoResponse.InfoItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artikel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoResponse.InfoItem item = items.get(position);
        if (item == null) return;

        holder.tvJudul.setText(item.getJudul() != null ? item.getJudul() : "");
        holder.tvDeskripsi.setText(item.getDeskripsi() != null ? item.getDeskripsi() : "");

        if (item.getUser() != null && item.getUser().getNamaUser() != null) {
            holder.tvNamaUser.setText(item.getUser().getNamaUser());
        } else {
            holder.tvNamaUser.setText("Admin");
        }

        if (item.getPublishedAt() != null) {
            holder.tvTanggal.setText(item.getPublishedAt());
        } else if (item.getCreatedAt() != null) {
            holder.tvTanggal.setText(item.getCreatedAt());
        } else {
            holder.tvTanggal.setText("");
        }

        String imagePath = item.getGambarUrl();
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
                    .into(holder.imgArtikel);
        } else {
            holder.imgArtikel.setImageResource(R.drawable.bg_card);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArtikel, imgAvatar;
        TextView tvJudul, tvDeskripsi, tvNamaUser, tvTanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArtikel = itemView.findViewById(R.id.img_artikel);
            imgAvatar = itemView.findViewById(R.id.img_avatar_artikel);
            tvJudul = itemView.findViewById(R.id.tv_judul_artikel);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_artikel);
            tvNamaUser = itemView.findViewById(R.id.tv_nama_user_artikel);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal_artikel);
        }
    }
}
