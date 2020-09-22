package com.inspection.powerline.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.inspection.powerline.R;
import com.inspection.powerline.bean.VideoBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.photoview.PhotoView;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;

    private ArrayList<VideoBean> photoList;
    public static onCallBack scallBack;


    public ViewPagerAdapter(Context context, ArrayList<VideoBean> photoList) {
        this.context = context;
        this.photoList = photoList;


    }

    public static void setCallBack(onCallBack callBack) {
        scallBack = callBack;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);

//        Glide.with(context).load(data.get(position)).into(photoView);

        String imageUrl = ImageDownloader.Scheme.FILE.wrap(photoList.get(position).getPath());
        ImageLoader.getInstance().displayImage(imageUrl, photoView);

        container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() { // 给每个ViewPager加载页添加点击事件,点击消失
            @Override
            public void onClick(View view) {
                if (scallBack != null) {
                    scallBack.onItemClick();
                }
            }
        });

        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface onCallBack {
        void onItemClick();
    }
}
