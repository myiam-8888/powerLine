package com.inspection.powerline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.inspection.powerline.R;
import com.inspection.powerline.bean.ShowDialogBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 149-201605 on 2016/8/1.
 */

/*
* 主界面显示 dialog的资源适配器
* */
public class ShowDialogSelectAdapter extends BaseAdapter {

    private List<ShowDialogBean> mList;

    private Context mContext;

    //控制checkbox是否已经选择
    private HashMap<Integer, Boolean> isSelected;

    public ShowDialogSelectAdapter(Context mContext, List<ShowDialogBean> mList) {
        this.mList = mList;
        this.mContext = mContext;
        isSelected = new HashMap<Integer, Boolean>();
        //初始化数据
        initData();

    }

    private void initData() {
        for (int i = 0; i < mList.size(); i++) {
            isSelected.put(i, false);
        }

    }



    @Override
    public int getCount() {
        int count = 0;
        if (null != mList && !mList.isEmpty()) {
            count = mList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder showDialogHolder = null;
        if (null == convertView) {
            showDialogHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_file_operate_item, null);
            showDialogHolder.name = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(showDialogHolder);
        } else {
            showDialogHolder = (ViewHolder) convertView.getTag();
        }

        final int index = position;

        if (null != mList.get(position).getName()) {
            showDialogHolder.name.setText(mList.get(position).getName());
        }


        return convertView;
    }


    class ViewHolder {
        TextView name;
    }
}


