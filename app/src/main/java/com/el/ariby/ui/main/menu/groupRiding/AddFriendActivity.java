package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.CustomFriendList;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.FriendListItem;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.FriendListRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {
    TextView nickname;
    ImageView profile;
    Button invite;
    ArrayList<FriendListItem> friendListItems = new ArrayList<FriendListItem>();
    AddFriendAdapter adapter;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference userRef;
    FirebaseUser user;
    String uid, followuid;
    DatabaseReference ref;

    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_to_group);
        adapter = new AddFriendAdapter();
        final RecyclerView recyclerView = findViewById(R.id.follow_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        nickname = findViewById(R.id.txt_friend_nick);
        profile = findViewById(R.id.img_Prof);
        invite = findViewById(R.id.btn_invite);

        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        ref = database.getReference("FRIEND").child("following").child(uid);
        userRef = database.getReference("USER");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            int i=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                String user = snapshot1.getKey();
                                followuid = snapshot.getKey();
                                if(followuid.equals(user)){
                                    String url = (String)snapshot1.child("userImageURL").getValue();
                                    String nickname = dataSnapshot.child(user).child("nickname").getValue().toString();
                                    Log.d("url",url+"  nick : "+nickname);
                                    if(url == null)
                                    {
                                        url = "https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/profile%2Fprofile.png?alt=media&token=b65b2e7b-e58b-4ff5-a38d-99ce048ec97a";
                                    }
                                    friendListItems.add(new FriendListItem(nickname,url));
                                    Log.e("str : ", friendListItems.get(i).toString());
                                    i = i+1;
                                }
                            }
                            recyclerView.setAdapter(new FriendListRecyclerAdapter(getApplicationContext(), friendListItems, R.layout.activity_add_friend_to_group));

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


    public class AddFriendAdapter extends BaseAdapter{

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
            CustomFriendList view = new CustomFriendList(context);
            return view;
        }
        public void addItem(FriendListItem item){friendListItems.add(item);}
    }

}
