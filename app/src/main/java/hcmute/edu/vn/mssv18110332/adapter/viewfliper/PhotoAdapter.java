package hcmute.edu.vn.mssv18110332.adapter.viewfliper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.R;

public class PhotoAdapter extends PagerAdapter {

    Context mContext;
    List<Photo> photoList;

    public PhotoAdapter(Context mContext, List<Photo> photoList) {
        this.mContext = mContext;
        this.photoList = photoList;
    }

    @Override
    public int getCount() {
        if (photoList != null)
            return photoList.size();
        return 0;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.ad_photo_item, container, false);
        ImageView imageView = view.findViewById(R.id.ad_photo_imageview);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Photo photo = photoList.get(position);
        if (photo != null)
        {
           Glide.with(mContext).load(photo.getResourceId()).into(imageView);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }
}
