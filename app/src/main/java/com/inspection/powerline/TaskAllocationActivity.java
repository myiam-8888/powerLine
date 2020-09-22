package com.inspection.powerline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.inspection.powerline.adapter.ShowDialogSelectAdapter;
import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.bean.ShowDialogBean;
import com.inspection.powerline.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class TaskAllocationActivity extends BaseActivity  {

    private static final String TAG = "SettingActivity";

    private Context mContext;

    private LinearLayout left_layout;
//
//    private TextView back;

    private TextView inspection_mode;

    //dialog的数据
    private List<ShowDialogBean> mList = new ArrayList<ShowDialogBean>();

    String[] sFileOpMenuArray;

    private AlertDialog dialog;

    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        setContentView(R.layout.activity_task_allocation);

        initView();



    }


    private void initView() {


        left_layout=findViewById(R.id.left_layout);
        left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        inspection_mode=findViewById(R.id.inspection_mode);
        inspection_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClickDialog();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e(TAG, "onResume");
    }








    private String getAppVersion(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "version date get error";
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");



    }












    public static class MyHandler extends Handler {

        private WeakReference<Context> reference;

        public MyHandler(Context mContext) {
            reference = new WeakReference<>(mContext);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TaskAllocationActivity settingActivity = (TaskAllocationActivity) reference.get();

            if (null != settingActivity) {
                switch (msg.what) {


                    default:
                        break;

                }

            }


        }
    }





    private void showClickDialog() {

        try {

            View operate_view = LayoutInflater.from(mContext).inflate(R.layout.dialog_file_operate_layout, null);
            ListView operate_listView = (ListView) operate_view.findViewById(R.id.listview);
            TextView operate_title = (TextView) operate_view.findViewById(R.id.title);
            operate_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                }
            });


            sFileOpMenuArray= getResources().getStringArray(R.array.inspection_mode_);
            operate_title.setText(getResources().getString(R.string.inspection_mode));



            mList.clear();

            String[] operate_name = sFileOpMenuArray;
            if (null != operate_name) {
                for (int i = 0; i < operate_name.length; i++) {
                    ShowDialogBean bean = new ShowDialogBean();
                    bean.setName(operate_name[i]);
                    mList.add(bean);
                }
                ShowDialogSelectAdapter adapter = new ShowDialogSelectAdapter(mContext, mList);
                operate_listView.setAdapter(adapter);
            }


            AlertDialog.Builder operate_builder = new AlertDialog.Builder(mContext, R.style.show_dialog);
            operate_builder.setCancelable(true);
            operate_builder.setView(operate_view);
            dialog = operate_builder.create();
            dialog.show();
            WindowManager.LayoutParams operate_lp = dialog.getWindow().getAttributes();
            operate_lp.width = dm.widthPixels * 2/ 5;
            operate_lp.height = dm.widthPixels  / 3;
            dialog.getWindow().setAttributes(operate_lp);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }












}
