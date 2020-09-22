package com.vlcplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;


/**
 * Author：caoyamin
 * Time: 2017/1/22
 */

public class VlcScroolFullControllerView extends FrameLayout {

    private static final String TAG = "VideoControllerView";
    private MediaPlayerControl mPlayer;
    private Context mContext;
    private View controller;
    private LinearLayout left_layout_scrool;
    private boolean mShowing = false;
    private boolean isFullscreen = false;
    private boolean mDragging;
    private static final int sDefaultTimeout = 10000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int PAUSE_START_BUTTON = 3;

    public TextView real_time_scrool,resolution_mode_scrool,wifi_statues_scrool;

    private LinearLayout lock_layout_scrool;

    private ImageView lock_layout_image_scrool;


    public ImageView setting_volume_image_scrool;
    private ImageView urgent_record_landspace_image_scrool;
    private ImageView take_photo_landspace_image_scrool;
    private ImageView sd_file_landspace_image_scrool;
    private ImageView local_video_landspace_image_scrool;

    private LinearLayout urgent_record_landspace_scrool;

    private LinearLayout setting_volume_scrool;

    private LinearLayout take_photo_landspace_scrool;
    private LinearLayout local_video_landspace_scrool;
    private LinearLayout sd_file_landspace_scrool;

    private Handler mHandler = new MessageHandler(this);

    private RelativeLayout top_layout_scrool;
    private LinearLayout  bottom_layout_scrool;

    private boolean isLock = false;

    public VlcScroolFullControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Log.i(TAG, TAG);
    }

    public VlcScroolFullControllerView(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        Log.i(TAG, TAG);
    }

    public VlcScroolFullControllerView(Context context) {
        this(context, true);
        Log.i(TAG, TAG);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (controller != null)
            initControllerView(controller);
    }

    public void setMediaPlayerListener(MediaPlayerControl player) {
        mPlayer = player;

    }

    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view
     */
    public void setControllerView(VlcVideoView videoView) {


        controller = makeControllerView();
        LayoutParams bottom = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
        );
        addView(controller, bottom);

        controller.setVisibility(GONE);
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        controller = inflate.inflate(R.layout.layout_scrool_full_controller, null);
        initControllerView(controller);
        return controller;
    }



    private void initControllerView(View v) {

        top_layout_scrool=(RelativeLayout)v.findViewById(R.id.tip_layout_scrool);
        bottom_layout_scrool=(LinearLayout)v.findViewById(R.id.bottom_layout_scrool);

        left_layout_scrool = (LinearLayout) v.findViewById(R.id.left_layout_scrool);
        if (null != left_layout_scrool) {
            left_layout_scrool.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (null != mTopOpeate) {
                        mTopOpeate.pushBack();
                    }


                }
            });
        }


        real_time_scrool=(TextView) v.findViewById(R.id.real_time_scrool);

        resolution_mode_scrool=(TextView) v.findViewById(R.id.resolution_mode_scrool);

        wifi_statues_scrool=(TextView) v.findViewById(R.id.wifi_statues_scrool);


        lock_layout_scrool=(LinearLayout) v.findViewById(R.id.lock_layout_scrool);
        lock_layout_scrool.setOnClickListener(onLockLayoutBtnClick);
        lock_layout_image_scrool=(ImageView) v.findViewById(R.id.lock_layout_image_scrool);
        showImageDrawable(R.drawable.unlock, lock_layout_image_scrool);

        urgent_record_landspace_scrool = (LinearLayout)  v.findViewById(R.id.urgent_record_landspace_scrool);
        urgent_record_landspace_scrool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mTopOpeate){
                    mTopOpeate.urgent_record();
                }
            }
        });
        urgent_record_landspace_image_scrool = (ImageView)  v.findViewById(R.id.urgent_record_landspace_image_scrool);
        showImageDrawable(R.drawable.urgent_record_land, urgent_record_landspace_image_scrool);

        setting_volume_scrool = (LinearLayout)  v.findViewById(R.id.setting_volume_scrool);
        setting_volume_scrool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mTopOpeate){
                    mTopOpeate.setting_volume();
                }
            }
        });
        setting_volume_image_scrool = (ImageView)  v.findViewById(R.id.setting_volume_image_scrool);
        showImageDrawable(R.drawable.record_close_volume_vertical_, setting_volume_image_scrool);

        take_photo_landspace_scrool = (LinearLayout)  v.findViewById(R.id.take_photo_landspace_scrool);
        take_photo_landspace_scrool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mTopOpeate){
                    mTopOpeate.takephoto();
                }
            }
        });
        take_photo_landspace_image_scrool = (ImageView)  v.findViewById(R.id.take_photo_landspace_image_scrool);
        showImageDrawable(R.drawable.take_photo_land, take_photo_landspace_image_scrool);


        local_video_landspace_scrool = (LinearLayout)  v.findViewById(R.id.local_video_landspace_scrool);
        local_video_landspace_scrool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mTopOpeate){
                    mTopOpeate.local_video();
                }
            }
        });
        local_video_landspace_image_scrool = (ImageView)  v.findViewById(R.id.local_video_landspace_image_scrool);
        showImageDrawable(R.drawable.local_video_land, local_video_landspace_image_scrool);

        sd_file_landspace_scrool = (LinearLayout)  v.findViewById(R.id.sd_file_landspace_scrool);
        sd_file_landspace_scrool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mTopOpeate){
                    mTopOpeate.sd_file();
                }
            }
        });
        sd_file_landspace_image_scrool = (ImageView)  v.findViewById(R.id.sd_file_landspace_image_scrool);
        showImageDrawable(R.drawable.sd_card_land, sd_file_landspace_image_scrool);





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


    //
    private View.OnClickListener onLockLayoutBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {
                isLock = !isLock;

                if (isLock) {
                    showImageDrawable(R.drawable.lock, lock_layout_image_scrool);
                } else {
                    showImageDrawable(R.drawable.unlock, lock_layout_image_scrool);


                }


            }
        }
    };

    public void setTitle(String name) {
//        if (null != title) {
//            title.setText(name);
//        }

    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }


    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing) {
            controller.setVisibility(VISIBLE);
            mShowing = true;
        }
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
//        top_layout_scrool.setVisibility(VISIBLE);
//        bottom_layout_scrool.setVisibility(VISIBLE);
        if(isLock){

        }

    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if(isLock){

        }

        try {
            controller.setVisibility(GONE);

        } catch (IllegalArgumentException ex) {
            Log.w("MediaController", "already removed");
        }
        mShowing = false;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPlayer == null) {
            return true;
        }

        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }









    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();


        void toggleFullScreen(boolean fullscreen);
    }


    public interface topOpeate {
        void pushBack();
        void takephoto();
        void  urgent_record();
        void setting_volume();
        void   local_video();
        void sd_file();

    }


    public topOpeate mTopOpeate;

    public void setTopOpeate(topOpeate mTopOpeate) {
        this.mTopOpeate = mTopOpeate;

    }


    private static class MessageHandler extends Handler {
        private final WeakReference<VlcScroolFullControllerView> mView;

        MessageHandler(VlcScroolFullControllerView view) {
            mView = new WeakReference<VlcScroolFullControllerView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            VlcScroolFullControllerView view = mView.get();
            if (view == null || view.mPlayer == null) {
                return;
            }

            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    view.hide();
                    break;
                case SHOW_PROGRESS:
                    break;
                case PAUSE_START_BUTTON:

                    break;
                default:
                    break;
            }
        }
    }
}