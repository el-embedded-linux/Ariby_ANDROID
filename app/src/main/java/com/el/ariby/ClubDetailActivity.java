package com.el.ariby;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.el.ariby.databinding.ActivityClubDetailBinding;

public class ClubDetailActivity extends AppCompatActivity {
    ActivityClubDetailBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_club_detail);

    }
}
