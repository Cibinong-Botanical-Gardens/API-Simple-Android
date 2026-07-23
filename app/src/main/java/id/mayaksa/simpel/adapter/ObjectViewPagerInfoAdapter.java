package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.rest.ApiClient;
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
        return items != null ? items.size() : 0;
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
        TextView tvAuthor = view.findViewById(R.id.tv_author);
        TextView tvDate = view.findViewById(R.id.tv_date);
        ImageView imageView = view.findViewById(R.id.image);
        
        InfoResponse.InfoItem item = items.get(position);
        if (item != null) {
            title.setText(item.getJudul() != null ? item.getJudul() : "");
            desc.setText(item.getDeskripsi() != null ? item.getDeskripsi() : "");

            if (item.getUser() != null && item.getUser().getNamaUser() != null) {
                tvAuthor.setText("Oleh: " + item.getUser().getNamaUser());
            } else {
                tvAuthor.setText("Oleh: Admin");
            }

            if (item.getPublishedAt() != null) {
                tvDate.setText(item.getPublishedAt());
            } else if (item.getCreatedAt() != null) {
                tvDate.setText(item.getCreatedAt());
            } else {
                tvDate.setText("");
            }

            // Gabungkan Base URL + relative path gambar_url jika relatif
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
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.bg_card);
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
