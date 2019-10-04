package com.el.ariby.ui.main.menu.raspberry;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.R;

public class videoPlayer extends AppCompatActivity implements SurfaceHolder.Callback{

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    SeekBar videoTimebar;
    TextView info;
    Handler handler;
    Button btnPlay, btnPause;
    ConstraintLayout controller;
    ProgressBar video_loading;
    int controllerVisibleTime = 2000;
    int controllerVisibleTimeCount = 0;
    int duration = 0;
    int timestamp = 0;
    int videoHeight = 0;
    int videoWidth = 0;
    float videoRatio = 0;
    String videoURL,videoTitle,videoFileName;
    String nextVideoURL = "";
    String previousVideoURL = "";

    @Override
    public  void onWindowFocusChanged(boolean  hasFocus){
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if(hasFocus){

            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_IMMERSIVE
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY  // this flag do=Semi-transparent bars temporarily appear and then hide again
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // Make Content Appear Behind the status  Bar
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  // it Make Content Appear Behind the Navigation Bar
                    |View.SYSTEM_UI_FLAG_FULLSCREEN  // hide status bar
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();
        videoURL = intent.getStringExtra("URL");
        controller = findViewById(R.id.controller);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        surfaceView = findViewById(R.id.surfaceView);
        videoTimebar = findViewById(R.id.videotimebar);
        video_loading = findViewById(R.id.video_loading);
        info = findViewById(R.id.info);
        handler = new Handler();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPauseBtn();
                mediaPlayer.start();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayBtn();
                mediaPlayer.pause();
            }
        });

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setVisibility(View.VISIBLE);
                controllerVisibleTimeCount=0;
                new Thread(new Thread(){
                    @Override
                    public void run(){
                        while(true){
                            try {
                                sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            controllerVisibleTimeCount++;
                            if(controllerVisibleTimeCount > controllerVisibleTime){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.setVisibility(View.INVISIBLE);
                                    }
                                });
                                break;
                            }
                        }
                    }
                }).start();
            }
        });

        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerVisibleTimeCount=0;
            }
        });

    }
    @Override
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        Log.d("MyTag","surfaceCreated");

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            Log.e("URL",videoURL);
            mediaPlayer.setDataSource(videoURL);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //비디오 플레이 시작
                    mediaPlayer.start();
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                    videoWidth = mediaPlayer.getVideoWidth();
                    videoHeight = mediaPlayer.getVideoHeight();
                    videoRatio = (float)videoWidth/videoHeight;
                    surfaceHolder.setFixedSize((int)(dm.heightPixels*videoRatio),dm.heightPixels);
                    duration = mediaPlayer.getDuration();
                    video_loading.setVisibility(View.GONE);
                    videoTimebar.setMax(duration);
                    videoTimebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser){
                                Toast.makeText(getApplicationContext(),Integer.toString(progress),Toast.LENGTH_SHORT).show();
                                mediaPlayer.seekTo(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    //playcheck
                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override public void run() {
                            while(true){
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    if(mediaPlayer.isPlaying()){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setPauseBtn();
                                            }
                                        });
                                    }else{
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                setPlayBtn();
                                            }
                                        });
                                    }
                                    timestamp = (int)(mediaPlayer.getTimestamp().getAnchorMediaTimeUs() / 1000);
                                }catch (Exception e){
                                    e.printStackTrace();
                                    break;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        videoTimebar.setProgress(timestamp);
                                    }
                                });
                            }
                        }
                    }).start();

                }
            });
            //mediaPlayer.setVolume(0, 0); //볼륨 제거
            mediaPlayer.setDisplay(surfaceHolder); // 화면 호출
            mediaPlayer.prepareAsync(); // 비디오 load 준비
            //mediaPlayer.setOnCompletionListener(completionListener); // 비디오 재생 완료 리스너

        } catch (Exception e) {
            Log.e("MyTag","surface view error : " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        Log.d("MyTag","surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e("MyTag","surfaceDestroyed");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void setPlayBtn(){
        btnPause.setVisibility(View.INVISIBLE);
        btnPause.setEnabled(false);
        btnPlay.setVisibility(View.VISIBLE);
        btnPlay.setEnabled(true);
    }

    public void setPauseBtn(){
        btnPlay.setVisibility(View.INVISIBLE);
        btnPlay.setEnabled(false);
        btnPause.setVisibility(View.VISIBLE);
        btnPause.setEnabled(true);
    }
}
