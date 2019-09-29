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

        A th1 = new A();

        B th2 = new B();

        th1.start();

        try {

            th1.join();
            // join(): 두 개 이상의 쓰레드가 동작할 시 하나의 쓰레드에 대해서 지속을 거는 것. 두 개의 쓰레드가 진행하고 있는데 한 쓰레드에 대해서 join 걸면                                           // 그 쓰레드가 끝날때 까지 기다려준다.
            // cf.) sleep()은 전체 쓰레드에 대해 지연을 건다. 하지만 join()은 특정 쓰레드에 대해 지연을 건다.

        } catch(InterruptedException e) {}

        th2.start();

    }

    private void init() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myUid = auth.getUid();
        database = FirebaseDatabase.getInstance();
        followRef = database.getReference("FRIEND").child("follower").child(myUid);
        userRef = database.getReference("USER");
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
    class A extends Thread {

        public void run() {

            followRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UidList.add(snapshot.getKey());
                        Log.d("mList1", String.valueOf(UidList));
                    }
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (int i =0; i<UidList.size(); i++){
                                nickname = (String.valueOf(dataSnapshot.child(UidList.get(i)).child("nickname").getValue()));
                                profileUrl =(String.valueOf(dataSnapshot.child(UidList.get(i)).child("userImageURL").getValue()));
                                followerNum = (String.valueOf(dataSnapshot.child(UidList.get(i)).child("followerNum").getValue()));
                                followNum = (String.valueOf(dataSnapshot.child(UidList.get(i)).child("followNum").getValue()));
                                mList.add(new FollowItem(profileUrl, nickname,followerNum, followNum, UidList.get(i)));
                                Log.d("mList2", String.valueOf(mList));
                            }
                            if(mList.isEmpty()){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                                Log.d("mList3", String.valueOf(mList));
                            }else{
                                emptyView.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                Log.d("mList3", String.valueOf(mList));
                            }
                            mRecyclerView.setAdapter(new com.el.ariby.ui.main.menu.follow.FollowerListAdapter(getApplicationContext(), mList , R.layout.activity_follower_list));
                            mAdapter.notifyDataSetChanged();
                            Log.d("mList4", String.valueOf(mList));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }



    class B extends Thread {

        public void run() {



        }

    }
}