package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.Info;

public class ObjectViewPagerInfoAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Info> models;

    public ObjectViewPagerInfoAdapter(Context context, ArrayList<Info> models){
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount(){
        return models.size();
//        int limit = 5;
//        return Math.min(models.size(), limit);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_pager_info, container, false);

        ImageView image = view.findViewById(R.id.image);

        image.setImageResource(models.get(position).getImage());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }

}

