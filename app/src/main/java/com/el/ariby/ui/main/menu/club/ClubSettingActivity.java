package com.el.ariby.ui.main.menu.club;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityClubSettingBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClubSettingActivity extends AppCompatActivity {

    ActivityClubSettingBinding mBinding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("CLUB");
    String clubTitle;
    ClubDetailActivity clubDetailActi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_club_setting);
        Intent intent = getIntent();
        clubTitle = intent.getStringExtra("title");
        clubDetailActi = (ClubDetailActivity)ClubDetailActivity.clubDetail;

        mBinding.btnClubSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.btnClubDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setIcon(R.drawable.ic_account_circle_black_24dp);
                builder.setTitle("클럽 삭제");
                builder.setMessage("정말 삭제하시겠습니까?");
                builder.setPositiveButton("YES", deleteClubListener);
                builder.setNegativeButton("NO", null);
                builder.show();
            }
        });
    }

    DialogInterface.OnClickListener deleteClubListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ref.child(clubTitle).setValue(null);
            clubDetailActi.finish();
            finish();
        }
    };
}
