package com.el.ariby;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.el.ariby.databinding.ActivityFirstBinding;
import com.el.ariby.ui.join.AgeActivity;
import com.el.ariby.ui.login.LoginActivity;

public class FirstActivity extends AppCompatActivity {
    ActivityFirstBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 풀스크린으로 세팅(상단바, 메뉴바 없어짐)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_first);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mainvideo);//비디오 파일 경로 설정
        mBinding.vdoView.setVideoURI(video);

        mBinding.btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AgeActivity.class);
                startActivity(intent);
            }
        });

        mBinding.btnOldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mBinding.vdoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBinding.vdoView.start(); // 비디오 무한 반복
            }
        });

        mBinding.vdoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.vdoView.start();
    }
}
