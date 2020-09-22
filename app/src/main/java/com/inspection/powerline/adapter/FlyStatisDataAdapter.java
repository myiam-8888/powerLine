package com.inspection.powerline.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.inspection.powerline.R;
import com.inspection.powerline.bean.FlyStatisBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;


import java.util.ArrayList;

/**
 * Created by 149-201605 on 2018/8/16.
 */

public class FlyStatisDataAdapter extends BaseAdapter {

    private Context mContext;







    private ArrayList<FlyStatisBean> mlist;

    public FlyStatisDataAdapter(Context mContext, ArrayList<FlyStatisBean> mlist){
        this.mContext=mContext;
        this.mlist=mlist;


    }

    public void setData(ArrayList<FlyStatisBean> mlist){
        this.mlist = mlist;

    }



    @Override
    public int getCount() {

        return null==mlist?0:mlist.size();
    }

    @Override
    public Object getItem(int position) {

        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlyStatisDataAdapter.ViewHolder sdFileListViewHolder = null;

        if (null == convertView) {
            sdFileListViewHolder = new FlyStatisDataAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sd_file_list_item, null);

            sdFileListViewHolder.ItemTitle = (TextView) convertView.findViewById(R.id.ItemTitle);
            sdFileListViewHolder.ItemText = (TextView) convertView.findViewById(R.id.ItemText);
//            sdFileListViewHolder.ItemSize = (TextView) convertView.findViewById(R.id.ItemSize);
            sdFileListViewHolder.ItemTitle1 = (TextView) convertView.findViewById(R.id.ItemTitle1);
            sdFileListViewHolder.ItemText1 = (TextView) convertView.findViewById(R.id.ItemText1);


            convertView.setTag(sdFileListViewHolder);
        } else {

            sdFileListViewHolder = (FlyStatisDataAdapter.ViewHolder) convertView.getTag();
        }

        FlyStatisBean ambaMediaInfoBean=mlist.get(position);




        return convertView;
    }


    class ViewHolder {

        TextView ItemTitle;

        TextView ItemText;

        TextView ItemTitle1;

        TextView ItemText1;






    }


}
