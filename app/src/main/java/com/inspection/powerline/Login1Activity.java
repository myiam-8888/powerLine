package com.inspection.powerline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.inspection.powerline.base.BaseActivity;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;


public class Login1Activity extends BaseActivity implements View.OnClickListener{


    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    private Button sign_in_button;

    private AutoCompleteTextView phonenum;

    private TextInputEditText password;

    private Toolbar toolbar;

    private Context mContext;

    private ImageView login_maxim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        mContext=this;

        initView();







    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_normal);
        setSupportActionBar(toolbar);

        login_maxim =findViewById(R.id.login_maxim);
//        if (null != login_maxim) {
//            showImageDrawable(R.mipmap.login_maxim, login_maxim);
//        }

        sign_in_button=findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(this);

        phonenum=findViewById(R.id.phonenum);


        password=findViewById(R.id.password);




    }










    public void showImageDrawable(Integer path, ImageView imageview) {
        //图片来源于
        imageview.setImageBitmap(ReadBitMap(mContext, path));
    }


    public Bitmap ReadBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.sign_in_button:

                startActivity(new Intent(Login1Activity.this, MainActivity.class));
                finish();
//                signIn();
              break;

          default:
         break;

        }

    }






//    private void signIn(){
//
//        String phnumber=phonenum.getText().toString();
//
//        String pass=password.getText().toString();
//
//        if(TextUtils.isEmpty(phnumber)){
//
//            Toast.makeText(mContext,getResources().getString(R.string.error_empty_phonenum), Toast.LENGTH_LONG).show();
//
//            return ;
//        }
//
//        if(11!=phnumber.length()){
//
//            Toast.makeText(mContext,getResources().getString(R.string.error_invalid_phonenum), Toast.LENGTH_LONG).show();
//
//            return ;
//        }
//
//
//        if(TextUtils.isEmpty(pass)){
//            Toast.makeText(mContext,getResources().getString(R.string.error_empty_password), Toast.LENGTH_LONG).show();
//
//            return ;
//
//        }
//
//        Map<String, Object> map=new HashMap<>();
//        map.put("mobile",phnumber);
//        map.put("password",pass);
//        map.put("app",ConstantPara.EXPRESS_CABINT);
//
//        Gson gg=new Gson();
//        String json =gg.toJson(map);
//
//        RequestBody body = RequestBody.create(JSON,json);
//        OkGo.<LzyResponse<loginModel>>post(Urls.URL_LOGIN)//
//                .tag(this)//
//                .upRequestBody(body)
//                .isMultipart(false)         //强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
//                .execute(new DialogCallback<LzyResponse<loginModel>>(Login1Activity.this) {
//                    @Override
//                    public void onSuccess(Response<LzyResponse<loginModel>> response) {
//
//
//
//
//
//                        Log.e("LoginActivity","onSuccess--------------------");
//
//                        BaseApplication.getIntance().setLoginBean(response.body().data);
//
////                      Log.e("MainActivity","onSuccess--------------------"+BaseApplication.getIntance().getLoginBean().getId());
////                      Log.e("MainActivity","onSuccess--------------------"+BaseApplication.getIntance().getLoginBean().getMobile());
//                        BaseApplication.getIntance().setLogin(true);
//                        startActivity(new Intent(Login1Activity.this, LogInfoActivity.class));
////
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(Response<LzyResponse<loginModel>> response) {
//                        Log.e("LoginActivity","onError---------------------");
////                      startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
//
//
//                    }
//                });
//
//
//
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
//        OkGo.getInstance().cancelTag(this);


    }


}
