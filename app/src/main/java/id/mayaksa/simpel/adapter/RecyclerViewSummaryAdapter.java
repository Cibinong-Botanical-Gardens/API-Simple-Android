package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.Report;

public class RecyclerViewSummaryAdapter extends RecyclerView.Adapter<RecyclerViewSummaryAdapter.ViewHolder> {

    private final Context context ;
    private final List<Report> itemData;


    public RecyclerViewSummaryAdapter(Context context, List<Report> itemData) {
        this.context = context;
        this.itemData = itemData;

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.item_recycler_view_summary,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // This will set the images in imageview
        holder.image.setImageResource(itemData.get(position).getImage());
        holder.title.setText(itemData.get(position).getTitle());
//        holder.abstr.setText(itemData.get(position).getAbstr());
//        holder.text.setText(itemData.get(position).getText());
        holder.name.setText(itemData.get(position).getName());
        holder.institute.setText(itemData.get(position).getInstitute());

        if(itemData.get(position).getProfile_image() != 0){
            holder.profileImage.setImageResource(itemData.get(position).getProfile_image());
        }

        if(itemData.get(position).getRole().equals("admin")){
            if(itemData.get(position).getInstitute().equals("DPKI")){
                holder.badge.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_circle_24,null));
                holder.badge.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
                holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_logo_brin,null));
                holder.icon.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.brin, null));
            }else{
                holder.badge.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_shield_24,null));
                holder.badge.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.admin_badge, null));
                holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_round_star_24,null));
            }
        }else if(itemData.get(position).getRole().equals("dev")){
            holder.badge.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_empty_badge,null));
            holder.badge.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.developer_badge, null));
            holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_science_24,null));
        }else if(itemData.get(position).getRole().equals("user")){
        holder.badge.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_empty_badge,null));
        holder.badge.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.verified_badge, null));
        holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_round_check_24,null));
    }
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView profileImage;
        ImageView icon;
        ImageView badge;
        TextView title;
        TextView name;
        TextView institute;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            badge = (ImageView) itemView.findViewById(R.id.badge);
            title = (TextView) itemView.findViewById(R.id.title);
            name = (TextView) itemView.findViewById(R.id.name);
            institute = (TextView) itemView.findViewById(R.id.institute);
        }

    }

}