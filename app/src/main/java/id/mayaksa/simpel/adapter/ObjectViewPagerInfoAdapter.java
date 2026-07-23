package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.response.InfoResponse;

public class ObjectViewPagerInfoAdapter extends PagerAdapter {

    private final Context context;
    private final List<InfoResponse.InfoItem> items;

    public ObjectViewPagerInfoAdapter(Context context, List<InfoResponse.InfoItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_pager_info, container, false);
        
        TextView title = view.findViewById(R.id.title);
        TextView desc = view.findViewById(R.id.text);
        
        InfoResponse.InfoItem item = items.get(position);
        title.setText(item.getJudul());
        desc.setText(item.getDeskripsi());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
