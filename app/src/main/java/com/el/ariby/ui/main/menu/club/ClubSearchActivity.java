package com.el.ariby.ui.main.menu.club;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_search);

        adapter = new ClubSearchAdapter();

        listview = findViewById(R.id.listview);
        edittext=findViewById(R.id.find);
        listview.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("club");

        adapter.addItem(new ClubItem("test",3,"test"));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String url = snapshot.child("clubImageURL").getValue().toString();
                    String club = snapshot.getKey();
                    long num = snapshot.child("member").getChildrenCount();
                    String location = snapshot.child("location").getValue().toString();
                    adapter.addItem(new ClubItem(url, club, num, location));
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
                String filterText = s.toString() ;
                ((ClubSearchAdapter)listview.getAdapter()).getFilter().filter(filterText) ;
            }
        });

    }
}
