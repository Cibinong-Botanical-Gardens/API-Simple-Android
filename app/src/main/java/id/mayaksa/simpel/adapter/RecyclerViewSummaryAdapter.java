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
import id.mayaksa.simpel.model.rest.response.LaporanResponse;

public class RecyclerViewSummaryAdapter extends RecyclerView.Adapter<RecyclerViewSummaryAdapter.ViewHolder> {

    private final Context context;
    private final List<LaporanResponse.LaporanItem> itemData;

    public RecyclerViewSummaryAdapter(Context context, List<LaporanResponse.LaporanItem> itemData) {
        this.context = context;
        this.itemData = itemData;
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
        holder.title.setText(item.getJudul());
        holder.desc.setText(item.getDeskripsi());
        
        if (item.getFotoBefore() != null && !item.getFotoBefore().isEmpty()) {
            Glide.with(context)
                    .load(item.getFotoBefore())
                    .placeholder(R.drawable.bg_card)
                    .error(R.drawable.ic_logo_tumbang)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.bg_card);
        }
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, desc;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.name); // Using 'name' id for desc as per existing layout maybe
        }
    }
}
