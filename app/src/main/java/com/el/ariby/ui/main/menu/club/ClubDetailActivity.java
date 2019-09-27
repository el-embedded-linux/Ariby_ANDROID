package com.el.ariby.ui.main.menu.club;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.el.ariby.R;
import com.el.ariby.databinding.ActivityClubDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ClubDetailActivity extends AppCompatActivity {
    ActivityClubDetailBinding mBinding;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference clubRef;
    FirebaseUser mUser;
    String nickname;
    int memberNum;
    public static ClubDetailActivity clubDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_club_detail);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        clubDetail=ClubDetailActivity.this;

        Intent intent = getIntent();

        userRef = database.getReference("USER").child(mUser.getUid());
        clubRef = database.getReference("CLUB").child(intent.getStringExtra("title"));

        Glide.with(this)
                .load(intent.getStringExtra("logo"))
                .centerCrop()
                .into(mBinding.imgLogo);
        mBinding.txtClubName.setText(intent.getStringExtra("title"));
        mBinding.txtLocation.setText(intent.getStringExtra("map"));
        mBinding.txtMaster.setText(intent.getStringExtra("nick"));
        mBinding.txtMember.setText(String.valueOf(intent.getLongExtra("num", 0) + "명"));

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nick = dataSnapshot.child("nickname").getValue().toString();
                if(mBinding.txtMaster.getText().equals(nick)) {
                    mBinding.btnSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBinding.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ClubSettingActivity.class);
                intent1.putExtra("title", mBinding.txtClubName.getText().toString());
                startActivity(intent1);
            }
        });

        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finish();
            }
        });
        memberNum = (int) intent.getLongExtra("num", 0);

        mBinding.linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),ClubMemberActivity.class);
                intent1.putExtra("clubTitle",mBinding.txtClubName.getText().toString());
                startActivity(intent1);
            }
        });
        mBinding.fabJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nickname = dataSnapshot.child("nickname").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int flag = 0;
                        String leader = dataSnapshot.child("leaderNick").getValue().toString();
                        Iterator<DataSnapshot> child =
                                dataSnapshot.child("member").
                                getChildren().
                                iterator();
                        while (child.hasNext()) {
                            String nick = child.next().getValue().toString();
                            if (nick.equals(mUser.getUid())) {
                                flag = 1;
                        }
                        if (leader.equals(nickname))
                                flag = 2;
                        }
                        if (flag == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setIcon(R.drawable.ic_account_circle_black_24dp);
                            builder.setTitle("클럽 가입");
                            builder.setMessage("가입하시겠습니까?");
                            builder.setPositiveButton("YES", joinDialogListener);
                            builder.setNegativeButton("NO", null);
                            builder.show();
                        } else if (flag == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setIcon(R.drawable.ic_account_circle_black_24dp);
                            builder.setTitle("클럽 탈퇴");
                            builder.setMessage("탈퇴하시겠습니까?");
                            builder.setPositiveButton("YES", exitDialogListener);
                            builder.setNegativeButton("NO", null);
                            builder.show();
                        } else
                            Toast.makeText(getApplicationContext(),
                                    "운영자는 가입이나 탈퇴가 불가능합니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });

    }

    DialogInterface.OnClickListener joinDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            clubRef.child("member").child(nickname).setValue(mUser.getUid());
            mBinding.txtMember.setText(String.valueOf((++memberNum)+ "명"));
        }
    };

    DialogInterface.OnClickListener exitDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            clubRef.child("member").child(nickname).removeValue();
            mBinding.txtMember.setText(String.valueOf((--memberNum) + "명"));
        }
    };
}
