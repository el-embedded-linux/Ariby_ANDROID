package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    private DatabaseReference followerRef, userRef;
    final ArrayList<FollowItem> list = new ArrayList<>();
    ArrayList<String> Followlist = new ArrayList<String>();
    String nickname;
    String profileUrl;
    String followerNum;
    String followNum;
    String myUid;
    RecyclerView mRecyclerView;
    FollowListAdapter mAdapter = null ;
    ArrayList<FollowItem> mList = new ArrayList<FollowItem>();
    ArrayList<String> UidList = new ArrayList<String>();
    TextView emptyView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_follow_list);
        mBinding.setActivity(this);
        init();
        emptyView = (TextView) findViewById(R.id.txt_follow_list_empty_view);
        mAdapter = new FollowListAdapter() ;
        mRecyclerView = (RecyclerView)findViewById(R.id.list_follow) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        mBinding.listFollow.setLayoutManager(new LinearLayoutManager(this));
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        A th1 = new A();

        th1.start();

            try {

                th1.join();
                // join(): 두 개 이상의 쓰레드가 동작할 시 하나의 쓰레드에 대해서 지속을 거는 것. 두 개의 쓰레드가 진행하고 있는데 한 쓰레드에 대해서 join 걸면                                           // 그 쓰레드가 끝날때 까지 기다려준다.
                // cf.) sleep()은 전체 쓰레드에 대해 지연을 건다. 하지만 join()은 특정 쓰레드에 대해 지연을 건다.

            } catch (InterruptedException e) {
            }




    }

    private void init() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myUid = auth.getUid();
        database = FirebaseDatabase.getInstance();
        followerRef = database.getReference("FRIEND").child("following").child(myUid);
        userRef = database.getReference("USER");
    }

    private void addItem(FollowItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list.add(data);
    }

    public class FollowListAdapter extends BaseAdapter {

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
            CustomFollow view = new CustomFollow(context);
            return view;
        }

        public void addItem(FollowItem item){ mList.add(item); }
    }

    class A extends Thread {

        public void run() {

            followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Followlist.add(snapshot.getKey());
                    }
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (int i =0; i<Followlist.size(); i++){
                                nickname = (String.valueOf(dataSnapshot.child(Followlist.get(i)).child("nickname").getValue()));
                                profileUrl = (String.valueOf(dataSnapshot.child(Followlist.get(i)).child("userImageURL").getValue()));
                                followerNum = (String.valueOf(dataSnapshot.child(Followlist.get(i)).child("followerNum").getValue()));
                                followNum = (String.valueOf(dataSnapshot.child(Followlist.get(i)).child("followNum").getValue()));
                                mList.add(new FollowItem(profileUrl, nickname, followNum ,followerNum, Followlist.get(i)));
                            }
                            if(mList.isEmpty()){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }else{
                                emptyView.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            mRecyclerView.setAdapter(new com.el.ariby.ui.main.menu.follow.FollowListAdapter(getApplicationContext(), mList , R.layout.activity_follow_list));
                            mAdapter.notifyDataSetChanged();
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

}