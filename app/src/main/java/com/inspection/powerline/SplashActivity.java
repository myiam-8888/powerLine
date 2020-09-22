package com.inspection.powerline;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


import androidx.annotation.NonNull;

import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.utils.ConstantPara;
import com.inspection.powerline.utils.LogUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class SplashActivity extends BaseActivity {

    private static String TAG="SplashActivity";

    private ImageView splash_view;
    private Context mContext;
    private DisplayMetrics dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.activity_splash);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (Build.VERSION.SDK_INT >= 26){
            //你再调用。。。。。

            closeAndroidPDialog();
        }

        if (Build.VERSION.SDK_INT >= 23) {

            requestP();

        }

        initView();
        startAnim2();

        LogUtils.e(TAG, "onCreate");
    }


   private void initView(){
       splash_view=findViewById(R.id.splash_view);
   }

    private void startAnim2(  ) {

        AlphaAnimation alpah = new AlphaAnimation(0.0f, 1.0f);
        alpah.setDuration(3000);
        alpah.setFillAfter(true);
        splash_view.startAnimation(alpah);
        alpah.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {



            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(SplashActivity.this, Login1Activity.class));
                finish();



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//
    private void requestP() {

        AndPermission.with(this)
                .requestCode(ConstantPara.REQUEST_CODE_PERMISSION_SD)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(mContext, rationale).
                                show();
                    }
                })
                .start();
    }


    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case ConstantPara.REQUEST_CODE_PERMISSION_SD: {
//                    Toast.makeText(MainActivity.this, "111111111111", Toast.LENGTH_SHORT).show();
                    LogUtils.e(TAG, "REQUEST_CODE_PERMISSION_SD1111111111111111111111111111111111111111111111111111");


                    break;
                }

            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case ConstantPara.REQUEST_CODE_PERMISSION_SD: {
//                    Toast.makeText(ShowMobleSDMediaActivity.this, R.string.message_post_failed, Toast.LENGTH_SHORT).show();
                    break;
                }

            }


            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, deniedPermissions)) {

                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(SplashActivity.this, ConstantPara.REQUEST_CODE_SETTING, dm).show();

                // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

            }
        }
    };



}
