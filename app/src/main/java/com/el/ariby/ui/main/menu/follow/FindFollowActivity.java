package com.el.ariby.ui.main.menu.follow;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityFindFollowBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindFollowActivity extends AppCompatActivity implements TextWatcher {
    ActivityFindFollowBinding mBinding;
    private FirebaseUser auth;
    private FirebaseDatabase database;
    private DatabaseReference followerRef, userRef;

    String myUid;
    RecyclerView mRecyclerView;
    EditText editTextFilter;
    TextView emptyView;
    FindFollowAdapter mAdapter;
    ArrayList<FollowItem> mList = new ArrayList<FollowItem>();
    ArrayList<String> UidList = new ArrayList<String>();
    ArrayList<String> Followlist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_follow);
        mBinding.setActivity(this);
        init();
        mAdapter = new FindFollowAdapter(getApplicationContext(), mList , R.layout.activity_find_follow);
        emptyView = (TextView) findViewById(R.id.txt_find_follow_list_empty_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.list_find_follow) ;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        mRecyclerView.setHasFixedSize(true);
        mBinding.listFindFollow.setLayoutManager(new LinearLayoutManager(this));
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        editTextFilter = findViewById(R.id.edittxt_find_follow);
        editTextFilter.addTextChangedListener(this);

        followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Followlist.add(snapshot.getKey());
                }
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(Followlist.contains(snapshot.getKey()) || auth.getUid().equals(snapshot.getKey())){

                            }else{
                                UidList.add(snapshot.getKey());
                            }
                        }
                            for (int i =0; i<UidList.size(); i++){
                                String nickname = dataSnapshot.child(UidList.get(i)).child("nickname").getValue().toString();
                                String profileUrl = dataSnapshot.child(UidList.get(i)).child("userImageURL").getValue().toString();
                                String followerNum = dataSnapshot.child(UidList.get(i)).child("followerNum").getValue().toString();
                                String followNum = dataSnapshot.child(UidList.get(i)).child("followNum").getValue().toString();
                                mList.add(new FollowItem(profileUrl, nickname, followNum ,followerNum, UidList.get(i).toString()));
                            }
                            if(mList.isEmpty()){
                                mRecyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }else{
                                emptyView.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            mRecyclerView.setAdapter(new FindFollowAdapter(getApplicationContext(), mList , R.layout.activity_find_follow));
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

    private void init() {
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myUid = auth.getUid();
        database = FirebaseDatabase.getInstance();
        followerRef = database.getReference("FRIEND").child("following").child(myUid);
        userRef = database.getReference("USER");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String filterText = s.toString();
        ((FindFollowAdapter) mRecyclerView.getAdapter()).getFilter().filter(filterText);
    }
}


