package com.inspection.powerline;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inspection.powerline.adapter.MyVideoAdapter;
import com.inspection.powerline.adapter.ShowDialogSelectAdapter;
import com.inspection.powerline.audio.activitys.VideoPlayerActivity;
import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.bean.ShowDialogBean;
import com.inspection.powerline.bean.VideoBean;
import com.inspection.powerline.utils.ConstantPara;
import com.inspection.powerline.utils.FileSizeUtil;
import com.inspection.powerline.utils.LogUtils;
import com.inspection.powerline.widgt.LoadingDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;


import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocalMediaActivity extends BaseActivity {

    private static final String TAG = "LocalMediaActivity";

    private Context mContext;

    private RecyclerView mRecyclerview;

    private MyVideoAdapter myVideoAdapter;

    private LinearLayout noDataLayout;

    private TextView title;

    private int curLocalIn = ConstantPara.LOCAL_IN_VIDEO;

    private File sPathPic;

    private LoadingDialog loadingDialog;

    private String sdCardDir;

    private ArrayList<VideoBean> videoList;

    private ArrayList<VideoBean> photoList;

    private LinearLayout left_layout;

    private ImageView no_data_image;

    private RadioGroup fileRadioGroup;

    private RadioButton local_video;

    private RadioButton local_photo;

    //
    private AlertDialog dialog;

    //dialog的数据
    private List<ShowDialogBean> mList = new ArrayList<ShowDialogBean>();

    private DisplayMetrics dm;

    private static String RootFilePath = "";
    private static String sSubDir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        RootFilePath = Environment.getExternalStorageDirectory() + "/DCIM/";
        LogUtils.e(TAG, "RootFilePath==" + RootFilePath);

        sSubDir = getResources().getString(R.string.subdir);

        setContentView(R.layout.activity_local_media);
        initView();
        requestP();

//        prepareSdPath();
//        LogUtils.e(TAG, "11111111111111111111111==");
//        scanerVideoAsyncTask videoAsyncTask = new scanerVideoAsyncTask();
//        videoAsyncTask.execute();
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

        noDataLayout = (LinearLayout) findViewById(R.id.no_data);

        no_data_image = (ImageView) findViewById(R.id.no_data_image);
        showImageDrawable(R.mipmap.no_data, no_data_image);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));


        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.local_file));
        fileRadioGroup = (RadioGroup) findViewById(R.id.fileRadioGroup);

        local_video = (RadioButton) findViewById(R.id.local_video);
        local_photo = (RadioButton) findViewById(R.id.local_photo);

        fileRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.local_video) {
                    curLocalIn = ConstantPara.LOCAL_IN_VIDEO;
                    refreshUI();
                } else if (checkedId == R.id.local_photo) {
                    curLocalIn = ConstantPara.LOCAL_IN_PHOTO;
                    refreshUI();
                }

            }
        });


    }

    //准备sd卡存储路径用来存储视频和图片
    private void prepareSdPath() {

            String RootFilePath = Environment.getExternalStorageDirectory() + "/DCIM/";

            String Thumanailpath = Environment.getExternalStorageDirectory() + "/ThumbnailImage/";

            String sSubDir = getResources().getString(R.string.subdir);


            try {


                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File sPathRoot = new File(RootFilePath);
                    if (!sPathRoot.exists()) {
                        sPathRoot.mkdir();
                    }
                    File sPathVid = new File(RootFilePath + sSubDir);
                    if (!sPathVid.exists()) {
                        sPathVid.mkdir();
                    }
                    sPathPic = sPathVid;

                    File sPathRootThumbnail = new File(Thumanailpath);
                    if (!sPathRootThumbnail.exists()) {
                        sPathRootThumbnail.mkdir();
                    }
                    sdCardDir = sPathRootThumbnail.toString();

                }




            } catch (Exception ex) {
                sPathPic = null;
                ex.printStackTrace();
            }



    }


    private void requestP() {

        AndPermission.with(this)
                .requestCode(ConstantPara.REQUEST_CODE_PERMISSION_SD)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(LocalMediaActivity.this, rationale).
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
//                    Toast.makeText(ShowMobleSDMediaActivity.this, R.string.message_post_succeed, Toast.LENGTH_SHORT).show();

                    prepareSdPath();
                    scanerVideoAsyncTask videoAsyncTask = new scanerVideoAsyncTask();
                    videoAsyncTask.execute();

                    break;
                }

            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case ConstantPara.REQUEST_CODE_PERMISSION_SD: {
//                    Toast.makeText(ShowMobleSDMediaActivity.this, R.string.message_post_failed, Toast.LENGTH_SHORT).show();222
                    break;
                }

            }


            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(LocalMediaActivity.this, deniedPermissions)) {

                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(LocalMediaActivity.this, ConstantPara.REQUEST_CODE_SETTING, dm).show();

                // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

            }
        }
    };


    /**
     * 获取视频文件
     *
     * @param
     * @param file
     * @return
     */
    private ArrayList<VideoBean> getVideoFile(File file) {
        final ArrayList<VideoBean> list = new ArrayList<VideoBean>();

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {

                String name = file.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")) {
                        VideoBean video = new VideoBean();
                        video.setDisplayName(file.getName());
                        video.setPath(file.getAbsolutePath());
                        video.setSize(FileSizeUtil.getAutoFileOrFilesSize(file.getAbsolutePath() + ""));
                        list.add(video);
                        return true;
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getVideoFile(file);
                }
                return false;
            }
        });

        return list;
    }


    /**
     * 获取视频文件
     *
     * @param
     * @param file
     * @return
     */
    private ArrayList<VideoBean> getPhotoFile(File file) {
        final ArrayList<VideoBean> list = new ArrayList<VideoBean>();

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {

                String name = file.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".jpg") || name.equalsIgnoreCase(".png")) {
                        VideoBean video = new VideoBean();
                        video.setDisplayName(file.getName());
                        video.setPath(file.getAbsolutePath());
                        video.setSize(FileSizeUtil.getAutoFileOrFilesSize(file.getAbsolutePath() + ""));
                        list.add(video);
                        return true;
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getPhotoFile(file);
                }
                return false;
            }
        });

        return list;
    }


    //RecyclerView Bug：IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter的解决方案
    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    private class scanerVideoAsyncTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始准备--运行在主线程
            loadingDialog = new LoadingDialog(mContext, getResources().getString(R.string.load_data));
            loadingDialog.setCanceledOnTouchOutside(true);
            loadingDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            //加载图片数据
            photoList = new ArrayList<VideoBean>();
            photoList = getPhotoFile(sPathPic);

            //加载视频数据
            //耗时操作--运行在工作线程
            videoList = new ArrayList<VideoBean>();
            videoList = getVideoFile(sPathPic);


            return true;


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            //运行结束--更新UI--运行在主线程
            if (null != loadingDialog && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }


            refreshUI();
            LogUtils.e(TAG, "00000000000000000000000000000000==");
        }


    }


    private void refreshUI() {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        if (curLocalIn == ConstantPara.LOCAL_IN_PHOTO) {
            if (null != photoList && photoList.size() > 0) {
                noDataLayout.setVisibility(View.GONE);
                mRecyclerview.setVisibility(View.VISIBLE);

                if (null == myVideoAdapter) {

                    myVideoAdapter = new MyVideoAdapter(photoList, curLocalIn);
                    mRecyclerview.setAdapter(myVideoAdapter);
                    myVideoAdapter.setOnItemClickListener(new MyVideoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, String path) {


                            showDeleteDialog(path);


                        }
                    });

                } else {

                    myVideoAdapter.setDatas(photoList, curLocalIn);
                    myVideoAdapter.notifyDataSetChanged();
                }


            } else {
                noDataLayout.setVisibility(View.VISIBLE);
                mRecyclerview.setVisibility(View.GONE);

            }


        } else {
            //视频文档

            if (null != videoList && videoList.size() > 0) {
                noDataLayout.setVisibility(View.GONE);
                mRecyclerview.setVisibility(View.VISIBLE);

                if (null == myVideoAdapter) {

                    myVideoAdapter = new MyVideoAdapter(videoList, curLocalIn);
                    mRecyclerview.setAdapter(myVideoAdapter);
                    myVideoAdapter.setOnItemClickListener(new MyVideoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, String path) {

                            showDeleteDialog(path);


                        }
                    });

                } else {

                    myVideoAdapter.setDatas(videoList, curLocalIn);
                    myVideoAdapter.notifyDataSetChanged();
                }


            } else {
                noDataLayout.setVisibility(View.VISIBLE);
                mRecyclerview.setVisibility(View.GONE);

            }

        }


    }


    private void showDeleteDialog(final String path) {


        View operate_view = LayoutInflater.from(mContext).inflate(R.layout.dialog_file_operate_layout, null);
        ListView operate_listView = (ListView) operate_view.findViewById(R.id.listview);
        TextView operate_title = (TextView) operate_view.findViewById(R.id.title);
        operate_title.setText(getResources().getString(R.string.title_file_operate));
        operate_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (null != dialog) {
                    dialog.dismiss();
                }

                if (0 == position) {

                    if (curLocalIn == ConstantPara.LOCAL_IN_PHOTO) {
                        if (!TextUtils.isEmpty(path)) {
                            //跳转查看照片
                            //图片显示
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //判断当前Android版本号是否大于等于24
                            Uri imageUri;
                            if (Build.VERSION.SDK_INT >= 24){
                                //如果是则使用FileProvider
                                imageUri = FileProvider.getUriForFile(mContext,
                                        "com.inspection.powerline.fileprovider",new File(path));
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                                //否则，使用原来的fromFile()
                                imageUri = Uri.fromFile(new File(path));
                            }
                            intent.setDataAndType(imageUri, "image/*");
                            startActivity(intent);

                        }
                    } else {
                        if (!TextUtils.isEmpty(path)) {
                            //播放视频

//


                            LogUtils.e("path", "path==" + path);
                            Intent intent;
                            intent = new Intent(mContext, VideoPlayerActivity.class);
                            intent.putExtra("videoPath", path);
                            startActivity(intent);

                        }
                    }

                } else if (1 == position) {


//                    showDeleteSinglDialog(path);

                } else if (2 == position) {

//                    deleteAllLocalFile();


                }


            }
        });
        mList.clear();
        String[] operate_name = getResources().getStringArray(R.array.operate_local_file_items);
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
        operate_lp.width = dm.widthPixels * 4 / 5;
        operate_lp.height = dm.widthPixels * 3 / 5;
        dialog.getWindow().setAttributes(operate_lp);
    }





//
//    private void showDeleteSinglDialog(final String path){
//
//        AlertDialog.Builder deleteSinglDialog=new AlertDialog.Builder(mContext)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle(R.string.title_deletlocal)
//                .setPositiveButton(R.string.downstopok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //Toast.makeText(getApplicationContext(), "Download is running!", Toast.LENGTH_LONG).show();
//                        try {
//                            File fLocal = new File(path);
//                            if (fLocal.exists()) {
//                                if (fLocal.delete()) {
//                                    if (curLocalIn == ConstantPara.LOCAL_IN_PHOTO) {
//                                        if (null != photoList && photoList.size() != 0) {
//
//                                            for (int i = 0; i < photoList.size(); i++) {
//                                                if (photoList.get(i).getPath().equals(path)) {
//                                                    photoList.remove(i);
//                                                }
//
//                                            }
//
//                                            if (null != myVideoAdapter) {
//                                                myVideoAdapter.notifyDataSetChanged();
//                                            }
//
//                                        }
//
//
//                                    } else {
//
//
//                                        if (null != videoList && videoList.size() != 0) {
//
//                                            for (int i = 0; i < videoList.size(); i++) {
//                                                if (videoList.get(i).getPath().equals(path)) {
//                                                    videoList.remove(i);
//                                                }
//
//                                            }
//
//                                            if (null != myVideoAdapter) {
//                                                myVideoAdapter.notifyDataSetChanged();
//                                            }
//
//                                        }
//
//
//                                    }
//
//
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                })
//                .setNegativeButton(R.string.downstopno, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//
//                    }
//                });
//
//        dialog = deleteSinglDialog.create();
//        dialog.show();
//
//    }
//
//
//    private void deleteAllLocalFile(){
//        AlertDialog.Builder deleteAllLocalBuilder= new AlertDialog.Builder(mContext)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle(R.string.title_deletealllocal)
//                .setPositiveButton(R.string.downstopok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //Toast.makeText(getApplicationContext(), "Download is running!", Toast.LENGTH_LONG).show();
//                        String sLocalFile;
////                        if(!BaseApplication.getIntance().getIsAmba()){
////                            sLocalFile = RootFilePath + sSubDir + "/";
////                        }else{
////                             sLocalFile = RootFilePath ;
////                        }
//                        sLocalFile = RootFilePath + sSubDir + "/";
//
//                        File fLocal = new File(sLocalFile);
//
//                        if (fLocal.exists() && fLocal.isDirectory()) {
//                            try {
//
//                                FileFilter filter = new FileFilter() {
//                                    @Override
//                                    public boolean accept(File pathname) {
//                                        boolean res = false;
//                                        if (pathname.isFile()) {
//                                            res = true;
//                                        }
//                                        return res;
//                                    }
//                                };
//                                File[] subFiles = fLocal.listFiles(filter);
//                                for (File fi : subFiles) {
//                                    fi.delete();
//                                }
//
//                                fLocal.delete();
//
//
//                                photoList.clear();
//
//                                videoList.clear();
//
//                                myVideoAdapter.notifyDataSetChanged();
//
//                            } catch (Exception e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                    }
//                })
//                .setNegativeButton(R.string.downstopno, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//
//                    }
//                });
//
//        dialog = deleteAllLocalBuilder.create();
//        dialog.show();
//
//
//    }




}
