package com.inspection.powerline;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.utils.ConstantPara;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity {

    private ImageView task_allocation,flight_tatistics,sys_setting,local_file;

    LinearLayout right_layout;


    private MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        initView();

    }


    private void initView(){

        task_allocation=findViewById(R.id.task_allocation);
        task_allocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TaskAllocationActivity.class));


            }
        });
        flight_tatistics=findViewById(R.id.flight_tatistics);
        flight_tatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,FlightStatisticsActivity.class));
            }
        });
        sys_setting=findViewById(R.id.sys_setting);
        sys_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
            }
        });
        local_file=findViewById(R.id.local_file);
        local_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.amaze.filemanager.ui.activities.MainActivity.class));

            }
        });



        right_layout=findViewById(R.id.right_layout);
        right_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Login1Activity.class));
                finish();

            }
        });





        myHandler.sendEmptyMessage(ConstantPara.SHOW_REAL_TIME);
    }


    public static class MyHandler extends Handler {

        private WeakReference<Context> reference;

        public MyHandler(Context mContext) {
            reference = new WeakReference<>(mContext);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = (MainActivity) reference.get();

            if (null != mainActivity) {
                switch (msg.what) {

                    case ConstantPara.SHOW_REAL_TIME:

//                        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
//                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                        String str = date.format(curDate);
//                        mainActivity.real_date.setText(str);
//
//                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//                        Date curtime = new Date(System.currentTimeMillis());//获取当前时间
//                        String strtime = formatter.format(curtime);
//                        mainActivity.real_time.setText(strtime);
                 break;

                    default:
                        break;

                }

            }


        }
    }






}
