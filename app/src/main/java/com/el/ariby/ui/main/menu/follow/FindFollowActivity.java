package com.el.ariby.ui.main.menu.follow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FindFollowActivity extends AppCompatActivity {
    DatabaseReference ref, followref;
    FirebaseDatabase database;
    EditText editTextFilter;
    FindFollowAdapter adapter;
    ListView listView;
    FirebaseUser user;
    String myUid, userUid;

    int followCount;
    ArrayList<String> followingUid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_follow);

        adapter = new FindFollowAdapter();
        editTextFilter = findViewById(R.id.find);
        listView = findViewById(R.id.listview1);
        listView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();

        loadData(new Callback() {
            @Override
            public void success(ArrayList<String> data) {
                followingUid=data;
                ref = database.getReference("USER");
                ref.addListenerForSingleValueEvent(new ValueEventListener() { // USER
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("align", "2");
                            userUid = snapshot.getKey();
                            boolean a = true;
                            Log.d("asd", userUid);

                            for (int j = 0; j < followCount; j++) {
                                if (userUid.equals(followingUid.get(j)) || myUid.equals(userUid)) {
                                    a = false;
                                }
                            }

                            if (a) {
                                String url = (String) snapshot.child("userImageURL").getValue();
                                String nickname = snapshot.child("nickname").getValue().toString();
                                String following = snapshot.child("following").getValue().toString();
                                String follower = snapshot.child("follower").getValue().toString();
                                adapter.addItem(new FollowItem(url, nickname, following, follower));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
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


        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filterText = s.toString();
                ((FindFollowAdapter) listView.getAdapter()).getFilter().filter(filterText);
            }
        });

    }

    public interface Callback {
        void success(ArrayList<String> data);

        void fail(String errorMessage);
    }

    public void loadData(final Callback callback) {
        followref = database.getReference("FRIEND").child("following").child(myUid);

        followref.addListenerForSingleValueEvent(new ValueEventListener() { //following
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followCount = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String list = snapshot.getKey();
                    followingUid.add(list);
                    Log.d("align", "1");
                }
                callback.success(followingUid);
            }

            // * 내가 팔로잉 되어있는 UID를 ArrayList<String> followingUid에 추가함 *
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


