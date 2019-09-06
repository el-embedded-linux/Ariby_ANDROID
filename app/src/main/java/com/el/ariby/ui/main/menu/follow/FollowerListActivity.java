package com.el.ariby.ui.main.menu.follow;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import com.el.ariby.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowerListActivity extends AppCompatActivity {
    DatabaseReference ref;
    DatabaseReference userref;
    DatabaseReference followref,followerNumRef, follwingNumRef;
    FirebaseDatabase database;
    EditText editTextFilter;
    FollowerListAdapter adapter;
    ListView listView;
    FirebaseUser user;
    String uid,followuid;
    String myUid, userUid;
    int followCount;
    String follower, following;
    ArrayList<String> followingUid = new ArrayList<>();
    ArrayList<String[]> followingNumList = new ArrayList<String[]>();
    ArrayList<String[]> followerNumList = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        adapter = new FollowerListAdapter();
        editTextFilter = findViewById(R.id.find);
        listView = findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();
        userref = database.getReference("USER");

        loadData(new Callback() {
            @Override
            public void success(ArrayList<String> data, ArrayList<String[]> followerNum, ArrayList<String[]> followingNum) {
                followingUid=data;
                followerNumList=followerNum;
                followingNumList=followingNum;

                ref = database.getReference("USER");
                ref.addListenerForSingleValueEvent(new ValueEventListener() { // USER
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("align", "2");
                            userUid = snapshot.getKey();
                            boolean a = false;

                            for (int j = 0; j < followCount; j++) {
                                if (userUid.equals(followingUid.get(j))) {
                                    a = true;
                                }
                            }

                            if (a) {
                                String url = (String) snapshot.child("userImageURL").getValue();
                                String nickname = snapshot.child("nickname").getValue().toString();

                                for(int t=0; t<followingNumList.size(); t++)  {
                                    if(followingNumList.get(t)[0].equals(userUid)){
                                        if(followingNumList.get(t)[1].equals(null)){
                                            following = String.valueOf(0);
                                        } else {
                                            following = followingNumList.get(t)[1];
                                        }
                                    }
                                }
                                //요소의 크기만큼 돌면서
                                for(int t=0; t<followerNumList.size(); t++){
                                    if(followerNumList.get(t)[0].equals(userUid)){
                                        if(followerNumList.get(t)[1].equals(null)){
                                            follower = String.valueOf(0);
                                        } else {
                                            follower = followerNumList.get(t)[1];
                                        }
                                    }
                                }

                                Log.d("getChildrenCount",follower+"\n"+following+"\n"+snapshot.getRef()+"\n"+userUid);
                                adapter.addItem(new FollowItem(url, nickname, following, follower));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void fail(String errorMessage) {

            }
        });


    }


    public interface Callback {
        void success(ArrayList<String> data, ArrayList<String[]> followerNum, ArrayList<String[]> followingNum);

        void fail(String errorMessage);
    }

    public void loadData(final Callback callback) {
        followref = database.getReference("FRIEND").child("follower").child(myUid);
        followerNumRef = database.getReference("FRIEND").child("follower");
        follwingNumRef= database.getReference("FRIEND").child("following");

        followref.addListenerForSingleValueEvent(new ValueEventListener() { //following
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followCount = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String list = snapshot.getKey();
                    followingUid.add(list);
                    Log.d("align", "1");
                }

            }

            // * 내가 팔로잉 되어있는 UID를 ArrayList<String> followingUid에 추가함 *
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        follwingNumRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String uid = snapshot.getKey();

                    following = String.valueOf(snapshot.getChildrenCount());
                    followingNumList.add(new String[]{uid, following});
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        followerNumRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String uid = snapshot.getKey();
                    follower = String.valueOf(snapshot.getChildrenCount());
                    followerNumList.add(new String[]{uid, follower});

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        callback.success(followingUid,followingNumList,followerNumList);

    }
}