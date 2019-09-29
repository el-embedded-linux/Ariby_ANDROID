package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityFollowerListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowerListActivity extends AppCompatActivity {
    ActivityFollowerListBinding mBinding;
    private FirebaseUser auth;
    private FirebaseDatabase database;
    private DatabaseReference followRef, userRef;
    String nickname;
    String profileUrl;
    String followerNum;
    String followNum;
    String myUid;
    RecyclerView mRecyclerView;
    TextView emptyView;
    FollowerListAdapter mAdapter = null ;
    ArrayList<FollowItem> mList = new ArrayList<FollowItem>();
    ArrayList<String> UidList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_follower_list);
        mBinding.setActivity(this);
        init();
        mAdapter = new FollowerListAdapter() ;
        emptyView = findViewById(R.id.txt_follower_list_empty_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.list_follower) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        mBinding.listFollower.setLayoutManager(new LinearLayoutManager(this));


        doWork(new Callback() {
            @Override
            public void success(ArrayList<String> nick,ArrayList<String> profile,ArrayList<String> follower,ArrayList<String> follow) {
                ArrayList<String> checkNick = nick;
                if(checkNick.isEmpty()){
                    mRecyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }else{
                    emptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void fail(String errorMessage) {

            }
        });
    }

    private void init() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myUid = auth.getUid();
        database = FirebaseDatabase.getInstance();
        followRef = database.getReference("FRIEND").child("follower").child(myUid);
        userRef = database.getReference("USER");
    }

    public interface Callback{
        void success(ArrayList<String> nick,ArrayList<String> profile,ArrayList<String> follower,ArrayList<String> follow);
        void fail(String errorMessage);
    }
    public void doWork(final Callback mCallback) {
        //followerRef = database.getReference("FRIEND").child("follower").child(myUid)
        followRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UidList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i =0; i<UidList.size(); i++){
                    nickname = (String.valueOf(dataSnapshot.child(UidList.get(i)).child("nickname").getValue()));
                    profileUrl =(String.valueOf(dataSnapshot.child(UidList.get(i)).child("userImageURL").getValue()));
                    followerNum = (String.valueOf(dataSnapshot.child(UidList.get(i)).child("followerNum").getValue()));
                    followNum = (String.valueOf(dataSnapshot.child(UidList.get(i)).child("followNum").getValue()));
                    mList.add(new FollowItem(profileUrl, nickname,followerNum, followNum, UidList.get(i)));
                }
                mRecyclerView.setAdapter(new com.el.ariby.ui.main.menu.follow.FollowerListAdapter(getApplicationContext(), mList , R.layout.activity_follower_list));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class FollowerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            CustomFollower view = new CustomFollower(context);
            return view;
        }

        public void addItem(FollowItem item){ mList.add(item); }
    }
}