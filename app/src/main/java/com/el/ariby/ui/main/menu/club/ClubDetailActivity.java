package com.el.ariby.ui.main.menu.club;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.el.ariby.MainActivity;
import com.el.ariby.R;
import com.el.ariby.databinding.ActivityClubDetailBinding;
import com.el.ariby.ui.main.menu.ClubFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ClubDetailActivity extends AppCompatActivity {
    ActivityClubDetailBinding mBinding;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference clubRef;
    FirebaseUser mUser;
    String nickname;
    int memberNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_club_detail);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        database=FirebaseDatabase.getInstance();

        Intent intent = getIntent();

        userRef=database.getReference("USER").child(mUser.getUid());
        clubRef=database.getReference("CLUB").child(intent.getStringExtra("title"));

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
        memberNum=(int)intent.getLongExtra("num",0);

        mBinding.fabJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nickname=dataSnapshot.child("nickname").getValue().toString();
                        if(!(nickname.equals(mBinding.txtMaster.getText().toString()))) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setIcon(R.drawable.ic_account_circle_black_24dp);
                            builder.setTitle("클럽 가입");
                            builder.setMessage("가입하시겠습니까?");
                            builder.setPositiveButton("YES", dialogListener);
                            builder.setNegativeButton("NO", null);
                            builder.show();
                        } else
                            Toast.makeText(getApplicationContext(),"클럽장은 가입이 불가능합니다.",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });

    }

    DialogInterface.OnClickListener dialogListener =new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            HashMap<String,String> map=new HashMap<>();
            map.put(nickname,mUser.getUid());
            clubRef.child("member").child(nickname).setValue(mUser.getUid());
            mBinding.txtMember.setText((memberNum+1) + "명");
        }
    };
}
