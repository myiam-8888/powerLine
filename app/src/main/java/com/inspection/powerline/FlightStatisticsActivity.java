package com.inspection.powerline;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inspection.powerline.adapter.FlyStatisDataAdapter;
import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.bean.FlyStatisBean;
import com.inspection.powerline.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class FlightStatisticsActivity extends BaseActivity  {

    private static final String TAG = "SettingActivity";

    private Context mContext;

    private RelativeLayout right_layout;

    ListView fly_statis_list;

    ArrayList<FlyStatisBean> mlist=new ArrayList<>();

    FlyStatisDataAdapter flyStatisDataAdapter;

    private LinearLayout left_layout;

    private TextView title;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;


        setContentView(R.layout.activity_flight_statis);

        initView();



    }


    private void initView() {


//        right_layout =  findViewById(R.id.right_layout);
//        right_layout.setVisibility(View.VISIBLE);

        left_layout = (LinearLayout) findViewById(R.id.left_layout);
        left_layout.setVisibility(View.VISIBLE);
        left_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.fly_statistics));


        fly_statis_list= findViewById(R.id.fly_statis_list);


        for(int i=0;i<10;i++){

            FlyStatisBean bean=new FlyStatisBean();
            bean.setI(i);

            mlist.add(bean);
        }


        flyStatisDataAdapter=new FlyStatisDataAdapter(mContext,mlist);
        fly_statis_list.setAdapter(flyStatisDataAdapter);
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
            FlightStatisticsActivity settingActivity = (FlightStatisticsActivity) reference.get();

            if (null != settingActivity) {
                switch (msg.what) {


                    default:
                        break;

                }

            }


        }
    }


















}
