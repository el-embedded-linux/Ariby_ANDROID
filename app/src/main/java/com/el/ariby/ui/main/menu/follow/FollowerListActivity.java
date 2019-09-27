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
    ArrayList<String> followingNumList = new ArrayList<String>();
    ArrayList<String> followerNumList = new ArrayList<String>();
    ArrayList<String> userImage = new ArrayList<String>();
    ArrayList<String> userNickname = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);

        adapter = new FollowerListAdapter();
        listView = findViewById(R.id.follower_list);
        listView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();
        userref = database.getReference("USER");

        loadData(new Callback() {
            @Override
            public void success(ArrayList<String> data,ArrayList<String> data2 , ArrayList<String> followerNum, ArrayList<String> followingNum) {
                userImage = data;
                userNickname = data2;
                followerNumList=followerNum;
                followingNumList=followingNum;

                adapter.addItem(new FollowItem(userImage, userNickname ,followerNumList, followingNumList));
                adapter.notifyDataSetChanged();
                Log.d("align", String.valueOf(userImage)+String.valueOf(userNickname)+String.valueOf(followerNumList)+String.valueOf(followingNumList));
            }

            @Override
            public void fail(String errorMessage) {

            }
        });


    }


    public interface Callback {
        void success(ArrayList<String> data, ArrayList<String> userNickname, ArrayList<String> followerNum, ArrayList<String> followingNum);

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
                    Log.d("align", String.valueOf(followingUid));
                }

            }

            // * 내가 팔로잉 되어있는 UID를 ArrayList<String> followingUid에 추가함 *
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0; i<followCount; i++){
                    String url = String.valueOf(dataSnapshot.child(followingUid.get(i)).child("userImageURL").getValue());
                    String nickname = String.valueOf(dataSnapshot.child(followingUid.get(i)).child("nickname").getValue());
                    userImage.add(url);
                    userNickname.add(nickname);
                    Log.d("align", String.valueOf(userImage));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        follwingNumRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0; i<followCount; i++){
                    String Count = String.valueOf(dataSnapshot.child("follower").child(followingUid.get(i)).getChildrenCount());
                    followingNumList.add(Count);
                    Log.d("align", String.valueOf(followingNumList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        followerNumRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0; i<followCount; i++){
                    String Count = String.valueOf(dataSnapshot.child("following").child(followingUid.get(i)).getChildrenCount());
                    followerNumList.add(Count);
                    Log.d("align", String.valueOf(followerNumList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter.addItem(new FollowItem(userImage, userNickname ,followerNumList, followingNumList));
        adapter.notifyDataSetChanged();

        callback.success(userImage, userNickname, followingNumList, followerNumList);

    }
}