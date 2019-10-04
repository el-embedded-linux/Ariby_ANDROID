package com.el.ariby.ui.main.menu.club;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.el.ariby.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClubMemberActivity extends AppCompatActivity {
    ListView listView;
    Button btnBack;
    ClubMemberAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference clubRef;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_member);
        Intent intent = getIntent();
        listView = findViewById(R.id.list_member);
        btnBack = findViewById(R.id.btn_back);

        adapter = new ClubMemberAdapter();
        database = FirebaseDatabase.getInstance();

        clubRef = database.getReference("CLUB").child(intent.getStringExtra("clubTitle")).child("member");
        userRef = database.getReference("USER");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clubRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getValue().toString();
                    Log.e("uid", uid);

                    userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String nickname = dataSnapshot.child("nickname").getValue().toString();
                            String userImg = dataSnapshot.child("userImageURL").getValue().toString();
                            Log.e("nickname", nickname);
                            Log.e("userImg", userImg);
                            adapter.addItem(new ClubMemberItem(userImg, nickname));

                            listView.setAdapter(adapter);//userRef가 마지막으로 실행되기 때문에 이 부분에 추가함. 추후 변경예정.
                            adapter.notifyDataSetChanged();
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
