package com.inspection.powerline.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.inspection.powerline.R;
import com.inspection.powerline.bean.VideoBean;
import com.inspection.powerline.utils.BitmapUtils;
import com.inspection.powerline.utils.ConstantPara;
import com.inspection.powerline.utils.FileUtil;
import com.inspection.powerline.utils.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;


import java.util.ArrayList;

/**
 *
 */
public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {

    public ArrayList<VideoBean> datas = null;

    private int curLocalIn;

    private DisplayImageOptions options;

    public MyVideoAdapter(ArrayList<VideoBean> datas, int curLocalIn) {
        this.datas = datas;
        this.curLocalIn = curLocalIn;

        if (curLocalIn == ConstantPara.LOCAL_IN_PHOTO) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.image_default)
                    .showImageOnFail(R.mipmap.image_default)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)

                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        } else {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.video_default)
                    .showImageOnFail(R.mipmap.video_default)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.local_media_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {


        if (curLocalIn == ConstantPara.LOCAL_IN_PHOTO) {

            viewHolder.diaplayname.setText(datas.get(position).getDisplayName());
            viewHolder.thumbnail.setTag(datas.get(position).getPath());
            viewHolder.thumbnail.setImageResource(R.mipmap.image_default);

            String name = datas.get(position).getDisplayName();
            if (!TextUtils.isEmpty(name)) {
                name=name.replaceAll(".jpg","");
                name=name.replaceAll(".JPG","");
                showThumbByAsynctack(datas.get(position).getPath(), viewHolder.thumbnail, name);
            }

            viewHolder.size.setText(datas.get(position).getSize());

            viewHolder.image_lock.setVisibility(View.GONE);

            final String path = datas.get(position).getPath();
            if (null != onItemClickListener) {
                viewHolder.home_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(viewHolder.itemView, viewHolder.getLayoutPosition(), path);
                    }
                });
            }


        } else if (curLocalIn == ConstantPara.LOCAL_IN_VIDEO) {
            viewHolder.diaplayname.setText(datas.get(position).getDisplayName());


            viewHolder.thumbnail.setTag(datas.get(position).getPath());
            viewHolder.thumbnail.setImageResource(R.mipmap.video_default);

            String name = datas.get(position).getDisplayName();
            if (!TextUtils.isEmpty(name)) {
                name=name.replaceAll(".mp4","");
                name=name.replaceAll(".MP4","");
                name=name.replaceAll(".mov","");
                name=name.replaceAll(".MOV","");
                showThumbByAsynctack(datas.get(position).getPath(), viewHolder.thumbnail, name);
            }

            viewHolder.size.setText(datas.get(position).getSize());

            String nameLock = datas.get(position).getDisplayName();

            if(!TextUtils.isEmpty(nameLock)){

                if(nameLock.contains(ConstantPara.LOCK_GSR)){
                    viewHolder.image_lock.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.image_lock.setVisibility(View.GONE);
                }

            }else{
                viewHolder.image_lock.setVisibility(View.GONE);
            }


            final String path = datas.get(position).getPath();
            if (null != onItemClickListener) {
                viewHolder.home_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(viewHolder.itemView, viewHolder.getLayoutPosition(), path);
                    }
                });
            }

        }


    }

    public void setDatas(ArrayList<VideoBean> datas, int curLocalIn) {
        this.datas = datas;
        this.curLocalIn = curLocalIn;
        notifyDataSetChanged();

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView diaplayname;
        public TextView size;
        public RelativeLayout home_layout;

        private ImageView thumbnail;
        private ImageView image_lock;

        public ViewHolder(View view) {
            super(view);
            diaplayname = (TextView) view.findViewById(R.id.diaplay_name);
            size = (TextView) view.findViewById(R.id.size);
            home_layout = (RelativeLayout) view.findViewById(R.id.home_layout);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            image_lock=(ImageView)view.findViewById(R.id.image_lock) ;
        }

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, String path);

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
            ImageLoader.getInstance().displayImage(imageUrl, imgview, options);
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
            if (curLocalIn == ConstantPara.LOCAL_IN_PHOTO) {
                 bitmap = BitmapUtils.decodeSampledBitmapFromFd(path,100,50);
            }else{
//                 bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
                bitmap= BitmapUtils.getVideoThumbnail(path);
            }

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
                ImageLoader.getInstance().displayImage(imageUrl, imgView, options);
            }
        }
    }



}
