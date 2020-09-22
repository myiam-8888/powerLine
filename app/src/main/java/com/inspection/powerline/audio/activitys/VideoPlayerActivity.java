package com.inspection.powerline.audio.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.inspection.powerline.R;
import com.inspection.powerline.base.BaseActivity;
import com.inspection.powerline.utils.LogUtils;
import com.vlcplayer.VlcControllerView;
import com.vlcplayer.VlcMediaController;
import com.vlcplayer.VlcVideoView;

import org.videolan.vlc.listener.FullScreenListener;


public class VideoPlayerActivity extends BaseActivity implements View.OnClickListener {
    VlcVideoView videoView;
    VlcMediaController controller;
    String path ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        if(null!=getIntent().getExtras()){
            path=getIntent().getExtras().getString("videoPath");
        }

        VlcControllerView controllerView= (VlcControllerView) findViewById(R.id.controllerView);

        videoView = (VlcVideoView) findViewById(R.id.videoView);

        controller=new VlcMediaController(controllerView,videoView);
        videoView.setMediaListenerEvent(controller);
        controllerView.setTopOpeate(new VlcControllerView.topOpeate() {
            @Override
            public void pushBack() {
                finish();
            }
        });
        controllerView.setTitle(path);
        LogUtils.e("VideoPlayerActivity","path=="+path);
        videoView.setVlcVideoViewOnComplete(new VlcVideoView.VlcVideoViewOnComplete() {
            @Override
            public void oncomplete() {

                finish();

            }
        });

        controller.setFullScreenListener(new FullScreenListener() {
            @Override
            public void Fullscreen(boolean fullscreen) {
                if (fullscreen) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(null!=path){
            videoView.startPlay(path);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.onDestory();
    }

    @Override
    public void onClick(View v) {

    }
}
