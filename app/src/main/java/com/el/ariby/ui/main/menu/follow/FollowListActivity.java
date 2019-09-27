package com.el.ariby.ui.main.menu.follow;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityFollowListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {
    ActivityFollowListBinding mBinding;
    private FirebaseUser auth;
    private FirebaseDatabase database;
    private DatabaseReference follwerRef, userRef;
    final ArrayList<FollowItem> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_follow_list);
        mBinding.setActivity(this);
        init();

        mBinding.listFollow.setLayoutManager(new LinearLayoutManager(this));


    }

    private void init() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        follwerRef = database.getReference("FRIEND").child("follower");
        userRef = database.getReference("USER");
    }

    private void addItem(FollowItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list.add(data);
    }

    public interface Callback{
        void success(String data);
        void fail(String errorMessage);
    }

    public void doWork(final Callback mCallback) {
        //follwerRef = database.getReference("FRIEND").child("follower")
        follwerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String[] nickname = new String[1];
                final String[] profileUrl = new String[1];
                final String[] follwerNum = new String[1];
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    userRef.child(data.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            nickname[0] = (String) dataSnapshot.child("nickname").getValue();
                            profileUrl[0] = (String) dataSnapshot.child("userImageURL").getValue();
                            follwerNum[0] = (String) dataSnapshot.child("userImageURL").getValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}