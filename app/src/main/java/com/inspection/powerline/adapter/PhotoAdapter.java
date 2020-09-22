package com.inspection.powerline.adapter;

import android.content.Context;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.inspection.powerline.R;
import com.inspection.powerline.bean.VideoBean;
import com.inspection.powerline.utils.BitmapUtils;
import com.inspection.powerline.utils.FileUtil;
import com.inspection.powerline.utils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


/**
 *
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {


    private ArrayList<VideoBean> photoList;

    int pos;

      private Context mContext;


    public PhotoAdapter(Context mContext, ArrayList<VideoBean> photoList) {
        this.photoList = photoList;
        this.mContext = mContext;



    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_imageview_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {


           if(position==pos){
               viewHolder.image_layout.setBackground(mContext.getDrawable(R.drawable.fillet_gray_border_btn));
           }else{

               viewHolder.image_layout.setBackground(null);
           }
        viewHolder.thumbnail.setTag(photoList.get(position).getPath());
//        viewHolder.thumbnail.setImageResource(R.mipmap.image_default);

        String name = photoList.get(position).getDisplayName();
        if (!TextUtils.isEmpty(name)) {
            name=name.replaceAll(".jpg","");
            name=name.replaceAll(".JPG","");
            name=name.replaceAll(".png","");
            name=name.replaceAll(".PNG","");
            showThumbByAsynctack(photoList.get(position).getPath(), viewHolder.thumbnail, name);
        }









            if (null != onItemClickListener) {
                viewHolder.image_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(viewHolder.itemView, viewHolder.getLayoutPosition());
                    }
                });
            }




    }



    public void setBg( int poss) {

       pos= poss;
        notifyDataSetChanged();

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return photoList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView thumbnail;
        private LinearLayout image_layout;


        public ViewHolder(View view) {
            super(view);

            thumbnail =  view.findViewById(R.id.image_view);
            image_layout = view.findViewById(R.id.image_layout);

        }

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    public void showThumbByAsynctack(String path, ImageView imgview, String name) {

        String pathThumb = Environment.getExternalStorageDirectory() + "/ThumbnailImage/" + name + ".jpg";

        LogUtils.e("MyVideoAdapter","pathThumb=="+pathThumb);

        if (!FileUtil.isFileExist(pathThumb)) {
            //异步加载
            new MyBobAsynctack(imgview, path, name).execute();
//            LogUtils.e("tag", "11111111111111");
        } else {
            String imageUrl = ImageDownloader.Scheme.FILE.wrap(pathThumb);
            ImageLoader.getInstance().displayImage(imageUrl, imgview);
        }

    }


    class MyBobAsynctack extends AsyncTask<String, Void, String> {
        private ImageView imgView;
        private String path;

        private String name;

        public MyBobAsynctack(ImageView imageView, String path, String name) {
            this.imgView = imageView;
            this.path = path;
            this.name = name;
            LogUtils.e("MyVideoAdapter","MyBobAsynctack===========================");
        }

        @Override
        protected String doInBackground(String... params) {
            //这里的创建缩略图方法是调用VideoUtil类的方法，也是通过 android中提供的 ThumbnailUtils.createVideoThumbnail(vidioPath, kind);
            Bitmap bitmap=null;

                 bitmap = BitmapUtils.decodeSampledBitmapFromFd(path,100,50);


            String pathThumb = Environment.getExternalStorageDirectory() + "/ThumbnailImage/" + name + ".jpg";
            //加入缓存中
            if (!FileUtil.isFileExist(pathThumb) && null != bitmap) {
                FileUtil.saveBitmapToSD(bitmap, name);
                bitmap.recycle();
                return pathThumb;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String pathThumb) {
            //通过 Tag可以绑定 图片地址和 imageView，这是解决Listview加载图片错位的解决办法之一
            if (imgView.getTag().equals(path)) {
                String imageUrl = ImageDownloader.Scheme.FILE.wrap(pathThumb);
                ImageLoader.getInstance().displayImage(imageUrl, imgView);
            }
        }
    }



}
