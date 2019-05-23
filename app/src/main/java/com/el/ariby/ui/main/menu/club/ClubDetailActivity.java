package com.el.ariby.ui.main.menu.club;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.el.ariby.MainActivity;
import com.el.ariby.R;
import com.el.ariby.databinding.ActivityClubDetailBinding;
import com.el.ariby.ui.main.menu.ClubFragment;

public class ClubDetailActivity extends AppCompatActivity {
    ActivityClubDetailBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_club_detail);
        Intent intent = getIntent();
        Glide.with(this)
                .load(intent.getStringExtra("logo"))
                .centerCrop()
                .into(mBinding.imgLogo);
        mBinding.txtClubName.setText(intent.getStringExtra("title"));
        mBinding.txtLocation.setText(intent.getStringExtra("map"));
        mBinding.txtMaster.setText(intent.getStringExtra("nick"));
        mBinding.txtMember.setText(String.valueOf(intent.getLongExtra("num",0) + "명"));
        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mBinding.fabJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setIcon(R.drawable.ic_account_circle_black_24dp);
                builder.setTitle("클럽 가입");
                builder.setMessage("가입하시겠습니까?");
                builder.setPositiveButton("YES",null);
                builder.setNegativeButton("NO",null);
                builder.show();
            }
        });

    }
}
