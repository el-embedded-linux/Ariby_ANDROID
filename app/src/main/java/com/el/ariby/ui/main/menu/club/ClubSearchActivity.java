package com.el.ariby.ui.main.menu.club;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.el.ariby.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClubSearchActivity extends AppCompatActivity {
    ListView listview;
    EditText edittext;
    ClubSearchAdapter adapter;
    DatabaseReference ref;

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ClubSearchAdapter();

        listview = findViewById(R.id.listview);
        edittext = findViewById(R.id.find);
        listview.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("CLUB");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String url = snapshot.child("clubImageURL").getValue().toString();
                    String club = snapshot.getKey();
                    String nick = snapshot.child("leaderNick").getValue().toString();
                    long num = snapshot.child("member").getChildrenCount();
                    String location = snapshot.child("location").getValue().toString();
                    adapter.addItem(new ClubItem(url, club, nick, num, location));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filterText = s.toString();
                ((ClubSearchAdapter) listview.getAdapter()).getFilter().filter(filterText);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title=((ClubItem)adapter.getItem(position)).getTitle();
                String nick=((ClubItem)adapter.getItem(position)).getNick();
                String logo=((ClubItem)adapter.getItem(position)).getMainLogo();
                long num=((ClubItem)adapter.getItem(position)).getNumber();
                String map=((ClubItem)adapter.getItem(position)).getMap();
                Intent intent = new Intent(getApplicationContext(), ClubDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("nick", nick);
                intent.putExtra("logo", logo);
                intent.putExtra("num", num);
                intent.putExtra("map", map);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.not_move_activity);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_search);


    }
}
