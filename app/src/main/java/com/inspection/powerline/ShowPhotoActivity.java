package com.inspection.powerline;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.inspection.powerline.adapter.MyVideoAdapter;
import com.inspection.powerline.adapter.PhotoAdapter;
import com.inspection.powerline.adapter.PhotoViewPager;
import com.inspection.powerline.adapter.ShowDialogSelectAdapter;
import com.inspection.powerline.adapter.ViewPagerAdapter;
import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.bean.ShowDialogBean;
import com.inspection.powerline.bean.VideoBean;
import com.inspection.powerline.utils.ConstantPara;
import com.inspection.powerline.utils.FileSizeUtil;
import com.inspection.powerline.utils.LogUtils;
import com.inspection.powerline.widgt.ClearEditText;
import com.inspection.powerline.widgt.LoadingDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class ShowPhotoActivity extends BaseActivity {

    private static final String TAG = "ShowPhotoActivity";

    private Context mContext;

    //    private LinearLayout left_layout;
//
    private TextView back;
    private TextView whole_tower;
    private TextView insulator;
    private TextView fittings;
    private TextView basics;
    private TextView ancillary_facilities;
    private TextView ground_wire;
    private TextView photo_name;
    private TextView basics_pop;
    private TextView basics_ok;


    //dialog的数据
    private List<ShowDialogBean> mList = new ArrayList<ShowDialogBean>();

    String[] sFileOpMenuArray;

    private AlertDialog dialog;

    private DisplayMetrics dm;

    static boolean isMultipleSelectionMode=true;//判断进入多选模式

    RecyclerView recyclerview;

    PhotoViewPager view_pager;

    int[] imageID = {R.mipmap.user_info_bg, R.mipmap.user_info_bg, R.mipmap.user_info_bg, R.mipmap.user_info_bg, R.mipmap.user_info_bg, R.mipmap.user_info_bg, R.mipmap.user_info_bg, R.mipmap.user_info_bg};

    PhotoAdapter photoAdapter;

    ViewPagerAdapter viewPagerAdapter;

    // 定义popupWindow
    private PopupWindow popupWindow;


    //一级菜单控件
    private ListView mFirstList;
    //一级菜单适配器
    private FirstAdapter mFirstAdapter;
    //一级菜单数据List
    private List<String> mFirstItemList = new ArrayList<String>();

    //二级菜单控件
    private static ListView mSecondList;
    //二级菜单适配器
    private SecondAdapter mSecondAdapter;
    //二级菜单数据List
    int pos;
    private List<String> mSecondItemList = new ArrayList<String>();

    String path;

    private LoadingDialog loadingDialog;

    private LinearLayout noDataLayout;

    private ImageView no_data_image;

    ClearEditText basics_text;


    private String firstSel;
    private String secondSel;

    private StringBuffer  secondSelbU;



    //底部跟顶部工具栏显示跟隐藏的动画
    private Animation mAnimSlideInTop;
    private Animation mAnimSlideInBottom;
    private Animation mAnimSlideOutTop;
    private Animation mAnimSlideOutBottom;

    private MyHandler myHandler = new MyHandler(this);

    FrameLayout frame_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        setContentView(R.layout.activity_show_photo);


        try {

            path = getIntent().getStringExtra("path");

            LogUtils.e(TAG, "------------path-======" + path);

            int inx = path.lastIndexOf("/");
            LogUtils.e(TAG, "------------inx-======" + inx);

            path = path.substring(0, inx);

            LogUtils.e(TAG, "------------path22222======" + path);

        } catch (Exception e) {
            e.printStackTrace();
        }


        initView();

        if (Build.VERSION.SDK_INT >= 23) {

            requestP();

        }


    }


    private void initView() {
        initToolsAnimation();

        frame_layout = findViewById(R.id.frame_layout);


        photo_name = findViewById(R.id.photo_name);





        whole_tower = findViewById(R.id.whole_tower);
        whole_tower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow(0, back);
            }
        });


        insulator = findViewById(R.id.insulator);
        insulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow(1, back);
            }
        });


        fittings = findViewById(R.id.fittings);
        fittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow(2, back);
            }
        });


        ground_wire = findViewById(R.id.ground_wire);
        ground_wire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow(3, back);
            }
        });

        ancillary_facilities = findViewById(R.id.ancillary_facilities);
        ancillary_facilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow(4, back);
            }
        });


        basics = findViewById(R.id.basics);
        basics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow(5, back);
            }
        });


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        view_pager = findViewById(R.id.view_pager);



        noDataLayout = (LinearLayout) findViewById(R.id.no_data);

        no_data_image = (ImageView) findViewById(R.id.no_data_image);
        showImageDrawable(R.mipmap.no_data, no_data_image);

        recyclerview = findViewById(R.id.recyclerview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowPhotoActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);

        myHandler.sendEmptyMessageDelayed(MSG_HIDE_TOOLBAR, DEFAULT_TIME_OUT);
    }

    //工具栏显示持续的时间
    private static final int DEFAULT_TIME_OUT = 4000;
    //工具栏标签
    private static final int MSG_HIDE_TOOLBAR = 2;

    private static final int MSG_SHOW_TOOLBAR = 3;
    //判断工具栏是否显示
    private boolean mToolBarIsShowing = false;


    //显示工具栏
    private void showTool() {


        recyclerview.setVisibility(View.VISIBLE);
        photo_name.setVisibility(View.VISIBLE);
        myHandler.sendEmptyMessageDelayed(MSG_HIDE_TOOLBAR, DEFAULT_TIME_OUT);
            try {
                if (null != mAnimSlideInTop) {
                    photo_name.startAnimation(mAnimSlideInTop);
                }

                if (null != mAnimSlideInBottom) {
                    recyclerview.startAnimation(mAnimSlideInBottom);
                }

                mToolBarIsShowing = true;

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }



    }


    //隐藏工具栏
    private void hideTool() {


         recyclerview.setVisibility(View.GONE);
           photo_name.setVisibility(View.GONE);
            myHandler.removeMessages(MSG_HIDE_TOOLBAR);

            try {
                if (null != mAnimSlideOutTop) {
                    photo_name.startAnimation(mAnimSlideOutTop);
                }


                if (null != mAnimSlideOutBottom) {
                    recyclerview.startAnimation(mAnimSlideOutBottom);
                }


                mToolBarIsShowing = false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "--hideTool--e====" + e);
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

        clearToolsAnimZero();

    }


    private void popupWindow(int spos, View view) {
        mFirstItemList.clear();
        mSecondItemList.clear();

        hideTool();

        View popupView = ShowPhotoActivity.this.getLayoutInflater().inflate(R.layout.activity_lmune, null);


        mFirstList = popupView.findViewById(R.id.first_list);
        mSecondList = popupView.findViewById(R.id.second_list);
        mSecondList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        basics_text =  popupView.findViewById(R.id.basics_text);
        basics_ok =  popupView.findViewById(R.id.basics_ok);
        basics_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(null!=popupWindow&&popupWindow.isShowing()){

                   popupWindow.dismiss();
               }

//                Toast.makeText(mContext,firstSel+""+secondSel,Toast.LENGTH_LONG).show();
                secondSelbU=new StringBuffer();

                if(null!=mSecondItemList&&mSecondItemList.size()>0){

                    for(int i=0;i<mSecondItemList.size();i++){
                        if( mSecondList.isItemChecked(i)){
                            secondSelbU.append(" "+mSecondItemList.get(i));

                        }

                    }
                }
                Toast.makeText(mContext,"您选择了："+secondSelbU.toString(),Toast.LENGTH_LONG).show();

            }
        });

        basics_pop =  popupView.findViewById(R.id.basics_pop);
        basics_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(basics_text.getVisibility()==View.GONE){
                    basics_text.setVisibility(View.VISIBLE);
                }


            }
        });



        mFirstAdapter = new FirstAdapter(mContext);
        mFirstList.setAdapter(mFirstAdapter);
        mFirstList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //选中一级菜单点击位置
                mFirstAdapter.setSelectedPosition(position);
                //将二级菜单上次的点击位置回置
                mSecondAdapter.setSelectedPosition(0);
                //刷新点击位置
                mFirstAdapter.notifyDataSetInvalidated();
                //获取一级菜单点击位置数据
                String ItemCode = mFirstItemList.get(position);

                //通过点击数据获取二级菜单数据
                getSecond(ItemCode, spos);
                LogUtils.e(TAG,"-------------------position22=="+position);

            }
        });

        mSecondAdapter = new SecondAdapter(mContext);
        mSecondList.setAdapter(mSecondAdapter);
        mSecondList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSecondAdapter.setSelectedPosition(position);
                mSecondAdapter.notifyDataSetInvalidated();

//
//                if (isMultipleSelectionMode) {
////                    setCountChange();
//
//                }
//                mSecondAdapter.notifyDataSetChanged();
                LogUtils.e(TAG,"-------------------position11=="+position);

            }
        });



        //获取一级菜单数据
        getFirst(spos);
//        getSecond("null", spos);


        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        PopupWindow window = new PopupWindow(popupView, 1400, 1000);

        // TODO: 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        // TODO: 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        window.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        window.showAsDropDown(view, 360, 20);


    }


    public static class MyHandler extends Handler {

        private WeakReference<Context> reference;

        public MyHandler(Context mContext) {
            reference = new WeakReference<>(mContext);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ShowPhotoActivity showPhotoActivity = (ShowPhotoActivity) reference.get();

            if (null != showPhotoActivity) {
                switch (msg.what) {


                    case MSG_HIDE_TOOLBAR:
                        //隐藏工具栏
//                    Log.e(TAG, "MSG_HIDE_TOOLBAR-------");
                        showPhotoActivity.hideTool();
                        break;

                    case MSG_SHOW_TOOLBAR:
                        //隐藏工具栏
//                    Log.e(TAG, "MSG_SHOW_TOOLBAR-------");
                        showPhotoActivity.showTool();
                        break;
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


            sFileOpMenuArray = getResources().getStringArray(R.array.inspection_mode_);
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
            operate_lp.width = dm.widthPixels * 2 / 5;
            operate_lp.height = dm.widthPixels / 3;
            dialog.getWindow().setAttributes(operate_lp);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void getFirst(int ii) {
        mFirstItemList.clear();

        if (ii == 0) {
            mFirstItemList.add("塔身");
            mFirstItemList.add("横担");
            mFirstItemList.add("爬梯");
            mFirstItemList.add("拉线");
            firstSel="塔身";
        } else if (ii == 1) {
            mFirstItemList.add("绝缘子");
            mFirstItemList.add("均压环");
            mFirstItemList.add("支柱");
            firstSel="绝缘子";
        } else if (ii == 2) {
            mFirstItemList.add("悬垂线夹");
            mFirstItemList.add("U型挂环");
            mFirstItemList.add("引流板");
            mFirstItemList.add("防震锤");
            mFirstItemList.add("联板");
            mFirstItemList.add("并沟线夹");
            mFirstItemList.add("护线条");
            firstSel="悬垂线夹";

        } else if (ii == 3) {
            mFirstItemList.add("导线");
            mFirstItemList.add("地线");

            firstSel="导线";
        } else if (ii == 4) {
            mFirstItemList.add("色标牌");
            mFirstItemList.add("杆号牌");
            mFirstItemList.add("防鸟挡板");
            mFirstItemList.add("避雷器");

            firstSel="色标牌";
        } else if (ii == 5) {

            mFirstItemList.add("异物堆积");
            mFirstItemList.add("树木");
            mFirstItemList.add("危险品");
            mFirstItemList.add("排水不完善");
            mFirstItemList.add("裂纹");
            firstSel="异物堆积";
        }

        mFirstAdapter.notifyDataSetChanged();
    }

    private void getSecond(String first, int ii) {
        mSecondItemList.clear();

        if (ii == 0) {
            if (first.equals("塔身")) {
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("塔材丢失");
                mSecondItemList.add("塔材变形");
                mSecondItemList.add("鸟巢");
                mSecondItemList.add("异物");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("螺栓丢失");
                mSecondItemList.add("裂纹");

                secondSel="锈蚀严重";
            } else if (first.equals("横担")) {
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("塔材丢失");
                mSecondItemList.add("塔材变形");
                mSecondItemList.add("鸟巢");
                mSecondItemList.add("异物");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("螺栓丢失");
                mSecondItemList.add("裂纹");
                secondSel="横担";
            } else if (first.equals("爬梯")) {

                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("脚钉丢失");
                mSecondItemList.add("脚钉变形");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("螺栓丢失");
                mSecondItemList.add("异物");

                secondSel="锈蚀严重";
            } else if (first.equals("拉线")) {

                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("磨损严重");
                mSecondItemList.add("脚钉变形");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("螺栓丢失");
                mSecondItemList.add("异物");

                secondSel="拉线";
            } else {

                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("塔材丢失");
                mSecondItemList.add("塔材变形");
                mSecondItemList.add("鸟巢");
                mSecondItemList.add("异物");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("螺栓丢失");
                mSecondItemList.add("裂纹");
                secondSel="锈蚀严重";
            }
        } else if (ii == 1) {

            if (first.equals("绝缘子")) {
                mSecondItemList.add("粉化严重");
                mSecondItemList.add("伞裙破损");
                mSecondItemList.add("断裂");
                mSecondItemList.add("内锲式绝缘子");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("插销松动");
                mSecondItemList.add("异物");

                secondSel="粉化严重";

            } else if (first.equals("均压环")) {
                mSecondItemList.add("脱落");
                mSecondItemList.add("变形严重");
                mSecondItemList.add("螺栓脱落");
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("破损");
                mSecondItemList.add("异物");


                secondSel="脱落";
            } else if (first.equals("支柱")) {
                mSecondItemList.add("粉化严重");
                mSecondItemList.add("破损");
                mSecondItemList.add("挂点锈蚀严重");
                mSecondItemList.add("断裂");
                mSecondItemList.add("螺母脱落");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("异物");
                secondSel="粉化严重";
            } else {

                mSecondItemList.add("粉化严重");
                mSecondItemList.add("伞裙破损");
                mSecondItemList.add("断裂");
                mSecondItemList.add("内锲式绝缘子");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("插销松动");
                mSecondItemList.add("异物");

                secondSel="粉化严重";
            }

        } else if (ii == 2) {

            if (first.equals("悬垂线夹")) {

                mSecondItemList.add("插销丢失");
                mSecondItemList.add("蛇片脱落");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("破损");
                mSecondItemList.add("螺母脱落");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("异物");
                secondSel="插销丢失";
            } else if (first.equals("U型挂环")) {
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("螺母松动");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("变形严重");
                mSecondItemList.add("螺母脱落");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("异物");
                secondSel="插销丢失";
            } else if (first.equals("引流板")) {

                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("螺栓脱落");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("异物");
                secondSel="螺栓松动";
            } else if (first.equals("防震锤")) {

                mSecondItemList.add("破损");
                mSecondItemList.add("脱落");
                mSecondItemList.add("移位");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("未与地面垂直");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("异物");

                secondSel="破损";
            } else if (first.equals("联板")) {

                mSecondItemList.add("插销丢失");
                mSecondItemList.add("螺栓脱落");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("螺栓松动");

                secondSel="插销丢失";
            } else if (first.equals("并沟线夹")) {

                mSecondItemList.add("插销丢失");
                mSecondItemList.add("螺栓脱落");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("丢失");

                secondSel="插销丢失";
            } else if (first.equals("护线条")) {

                mSecondItemList.add("断股");
                mSecondItemList.add("移位");
                mSecondItemList.add("松脱");
                secondSel="断股";
            } else {
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("蛇片脱落");
                mSecondItemList.add("锈蚀严重");
                mSecondItemList.add("破损");
                mSecondItemList.add("螺母脱落");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("异物");

                secondSel="插销丢失";
            }


        } else if (ii == 3) {

            if (first.equals("导线")) {

                mSecondItemList.add("断股");
                mSecondItemList.add("异物");

                secondSel="断股";

            } else if (first.equals("地线")) {
                mSecondItemList.add("断股");
                mSecondItemList.add("异物");
                mSecondItemList.add("过线脱落");
                mSecondItemList.add("引下线与塔身接触");

                secondSel="地线";

            } else {
                mSecondItemList.add("断股");
                mSecondItemList.add("异物");

                secondSel="断股";

            }

        } else if (ii == 4) {

            if (first.equals("色标牌")) {

                mSecondItemList.add("挂错");
                mSecondItemList.add("脱落");
                mSecondItemList.add("褪色");

                secondSel="挂错";

            } else if (first.equals("杆号牌")) {
                mSecondItemList.add("挂错");
                mSecondItemList.add("脱落");
                mSecondItemList.add("褪色");
                secondSel="杆号牌";
            } else if (first.equals("防鸟挡板")) {
                mSecondItemList.add("异物");
                mSecondItemList.add("脱落");

                secondSel="防鸟挡板";
            } else if (first.equals("避雷器")) {
                mSecondItemList.add("粉化严重");
                mSecondItemList.add("破损");
                mSecondItemList.add("挂点锈蚀严重");
                mSecondItemList.add("断裂");
                mSecondItemList.add("螺母松动");
                mSecondItemList.add("灼烧痕迹");
                mSecondItemList.add("插销丢失");
                mSecondItemList.add("螺栓松动");
                mSecondItemList.add("异物");
                secondSel="避雷器";

            } else {
                mSecondItemList.add("挂错");
                mSecondItemList.add("脱落");
                mSecondItemList.add("褪色");
                secondSel="挂错";
            }

        } else if (ii == 5) {
            secondSel="";
        }


        if (null != mSecondItemList) {
            mSecondAdapter.notifyDataSetChanged();

        }


    }


    public class FirstAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        //点击位置
        private int selectedPosition = -1;

        //设置点击位置给Adapter
        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public FirstAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mFirstItemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_simple, null);
                viewHolder.date = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //判断当前位置是否为点击位置
            if (selectedPosition == position) {
                //设置选中状态背景色
                viewHolder.date.setBackgroundColor(Color.parseColor("#8a8a8a"));
            } else {
                viewHolder.date.setBackgroundColor(Color.TRANSPARENT);
            }
            viewHolder.date.setText(mFirstItemList.get(position));
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView date;
    }

    public class SecondAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        private int selectedPosition = -1;

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public SecondAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mSecondItemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_simple, null);
                viewHolder.date = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            if (selectedPosition == position) {
//                viewHolder.date.setBackgroundColor(Color.parseColor("#8a8a8a"));
//            } else {
//                viewHolder.date.setBackgroundColor(Color.TRANSPARENT);
//            }

            //判断position位置是否被选中，改变颜色
            if (ShowPhotoActivity.mSecondList.isItemChecked(position) && ShowPhotoActivity.isMultipleSelectionMode) {
                viewHolder.date.setBackgroundColor(Color.parseColor("#8a8a8a"));
            } else {
                viewHolder.date.setBackgroundColor(Color.TRANSPARENT);
            }
            viewHolder.date.setText(mSecondItemList.get(position));
            return convertView;
        }
    }

    private static class ViewHolder2 {
        TextView date;
    }






    private ArrayList<VideoBean> photoList;


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
            File sPathVid = new File(path);
            if (!sPathVid.exists()) {
                sPathVid.mkdirs();
            }
            photoList = getPhotoFile(sPathVid);

            //加载视频数据
            //耗时操作--运行在工作线程


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
//            LogUtils.e(TAG, "00000000000000000000000000000000==");
        }


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




    private void refreshUI() {

        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

            if (null != photoList && photoList.size() > 0) {
                photo_name.setText(photoList.get(0).getDisplayName());
                noDataLayout.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
                photoAdapter = new PhotoAdapter(ShowPhotoActivity.this, photoList);
                recyclerview.setAdapter(photoAdapter);
                photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        view_pager.setCurrentItem(position);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                photo_name.setText(photoList.get(position).getDisplayName());
                            }
                        });
                    }
                });
                viewPagerAdapter = new ViewPagerAdapter(mContext, photoList);
                view_pager.setAdapter(viewPagerAdapter);
                ViewPagerAdapter.setCallBack(new ViewPagerAdapter.onCallBack() {
                    @Override
                    public void onItemClick() {
                        if (mToolBarIsShowing) {
                            hideTool();
                        } else {
                            showTool();
                        }
                    }
                });
                view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        recyclerview.smoothScrollToPosition(position);
                        photoAdapter.setBg(position);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                photo_name.setText(photoList.get(position).getDisplayName());
                            }
                        });
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });


            } else {
                noDataLayout.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);

            }





    }



    private File sPathPic;
    private void prepareSdPath() {

        String RootFilePath = Environment.getExternalStorageDirectory() + "/DCIM/";


        String sSubDir = getResources().getString(R.string.subdir);


        try {


            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File sPathRoot = new File(RootFilePath);
                if (!sPathRoot.exists()) {
                    sPathRoot.mkdirs();
                }
                File sPathVid = new File(RootFilePath + sSubDir);
                if (!sPathVid.exists()) {
                    sPathVid.mkdirs();
                }

                sPathPic = sPathVid;

            }




        } catch (Exception ex) {
            sPathPic = null;
            ex.printStackTrace();
        }



    }




    private void requestP() {

        AndPermission.with(this)
                .requestCode(ConstantPara.REQUEST_CODE_PERMISSION_SD)
                .permission( Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
//                    Toast.makeText(ShowMobleSDMediaActivity.this, R.string.message_post_failed, Toast.LENGTH_SHORT).show();
                    break;
                }

            }


            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(ShowPhotoActivity.this, deniedPermissions)) {

                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(ShowPhotoActivity.this, ConstantPara.REQUEST_CODE_SETTING, dm).show();

                // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

            }
        }
    };




    public void initToolsAnimation() {
        mAnimSlideOutBottom = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_out_bottom);
        mAnimSlideOutTop = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_out_top);
        mAnimSlideInBottom = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_in_bottom);
        mAnimSlideInTop = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_in_top);

    }



    //清空工具栏的动画
    private void clearToolsAnimZero() {
        if (null != mAnimSlideOutBottom) {
            mAnimSlideOutBottom = null;
        }

        if (null != mAnimSlideOutTop) {
            mAnimSlideOutTop = null;
        }

        if (null != mAnimSlideInBottom) {
            mAnimSlideInBottom = null;
        }

        if (null != mAnimSlideInTop) {
            mAnimSlideInTop = null;
        }

    }







}
