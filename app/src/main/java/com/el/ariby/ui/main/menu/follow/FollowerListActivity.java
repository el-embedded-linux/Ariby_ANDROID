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
    FirebaseDatabase database;
    EditText editTextFilter;
    FollowerListAdapter adapter;
    ListView listView;
    FirebaseUser user;
    String uid,followuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);

        adapter = new FollowerListAdapter();
        editTextFilter = findViewById(R.id.find);
        listView = findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        ref = database.getReference("FRIEND").child("follower").child(uid);
        userref = database.getReference("USER");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    userref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                String user = snapshot1.getKey();
                                followuid = snapshot.getKey();
                                if(followuid.equals(user)) {
                                    String url = (String) snapshot1.child("userImageURL").getValue();
                                    String nickname = dataSnapshot.child(user).child("nickname").getValue().toString();
                                    String following = snapshot1.child("following").getValue().toString();
                                    String follower = snapshot1.child("follower").getValue().toString();
                                    Log.d("123321", nickname);
                                    adapter.addItem(new FollowItem(url, nickname,following,follower));

                                }

                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}