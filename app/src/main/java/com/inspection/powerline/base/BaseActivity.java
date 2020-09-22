package com.inspection.powerline.base;


import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;



public class BaseActivity extends AppCompatActivity
{
    AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager = AppManager.getAppManager();
        // 把当前activity加入栈中
        appManager.addActivity(this);
        appManager.getSize("销毁前堆栈activity数目");



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity关闭，从栈中移除
        appManager.finishActivity(this);
        appManager.getSize("销毁后堆栈activity数目");


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }








}

