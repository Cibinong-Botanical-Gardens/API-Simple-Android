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
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogbookResponse.LogbookItem item = items.get(position);
        holder.title.setText(item.getJenisKegiatan());
        holder.area.setText(item.getArea());
        holder.idTree.setText(item.getIdTree());
        holder.date.setText(item.getCreateAt());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, area, idTree, date;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            area = itemView.findViewById(R.id.name);
            idTree = itemView.findViewById(R.id.institute);
            date = itemView.findViewById(R.id.latitude); // Reusing latitude field for date
        }
    }
}
