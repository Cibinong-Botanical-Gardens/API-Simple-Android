package id.mayaksa.simpel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import id.mayaksa.simpel.R;
import id.mayaksa.simpel.model.OnBoarding;

public class ObjectViewPagerOnBoardAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<OnBoarding> models;

    public ObjectViewPagerOnBoardAdapter(Context context, ArrayList<OnBoarding> models){
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount(){
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_pager_on_board, container, false);

        ImageView image = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView text = view.findViewById(R.id.text);

        OnBoarding model = models.get(position);
        int img = model.getImage();

        image.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        text.setText(models.get(position).getText());


        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }

}
