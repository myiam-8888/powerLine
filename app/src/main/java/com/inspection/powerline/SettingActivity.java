package com.inspection.powerline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.powerline.adapter.ShowDialogSelectAdapter;
import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.bean.ShowDialogBean;
import com.inspection.powerline.utils.LogUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class SettingActivity extends BaseActivity {

    private static final String TAG = "SettingActivity";

    private Context mContext;

    private LinearLayout left_layout;

    private TextView title;

    private RelativeLayout manage_logo, dji_manage, tower_setting, dev_number, update_version,map_manage;

    //dialog的数据
    private List<ShowDialogBean> mList = new ArrayList<ShowDialogBean>();

    private AlertDialog dialog;

    private DisplayMetrics dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        setContentView(R.layout.activity_setting);

        initView();


    }


    private void initView() {

        left_layout = (LinearLayout) findViewById(R.id.left_layout);
        left_layout.setVisibility(View.VISIBLE);
        left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.fly_setting));


        manage_logo = findViewById(R.id.manage_logo);
        manage_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClickDialog(1);
            }
        });


        dji_manage = findViewById(R.id.dji_manage);
        dji_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClickDialog(2);
            }
        });


        tower_setting = findViewById(R.id.tower_setting);
        tower_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClickDialog(3);
            }
        });



        map_manage = findViewById(R.id.map_manage);
        map_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClickDialog(4);
            }
        });





        dev_number = findViewById(R.id.dev_number);
        dev_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        update_version = findViewById(R.id.update_version);
        update_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            SettingActivity settingActivity = (SettingActivity) reference.get();

            if (null != settingActivity) {
                switch (msg.what) {


                    default:
                        break;

                }

            }


        }
    }



    String[] sFileOpMenuArray;
    private void showClickDialog(int pos) {

        try {

            View operate_view = LayoutInflater.from(mContext).inflate(R.layout.dialog_file_operate_layout, null);
            ListView operate_listView = (ListView) operate_view.findViewById(R.id.listview);
            TextView operate_title = (TextView) operate_view.findViewById(R.id.title);
            operate_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                }
            });

            if(pos==1){
                sFileOpMenuArray= getResources().getStringArray(R.array.manage_logo);
                operate_title.setText(getResources().getString(R.string.manage_logo));
            }else  if(pos==2){
                sFileOpMenuArray = getResources().getStringArray(R.array.dji_manege);
                operate_title.setText(getResources().getString(R.string.dji_manage));
            }else  if(pos==3){
                sFileOpMenuArray = getResources().getStringArray(R.array.tower_manege);
                operate_title.setText(getResources().getString(R.string.tower_setting));
            }else  if(pos==4){
                sFileOpMenuArray = getResources().getStringArray(R.array.map_manege);
                operate_title.setText(getResources().getString(R.string.map_manage));
            }

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
            operate_lp.height = dm.widthPixels * 2 / 5;
            dialog.getWindow().setAttributes(operate_lp);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
