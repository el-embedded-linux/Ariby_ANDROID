package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.el.ariby.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupRideActivity extends AppCompatActivity {
    Button createGroup;
    GroupRidingAdapter adapter;
    ArrayList<GroupRideItem> groupRideItems = new ArrayList<GroupRideItem>();

    FirebaseDatabase database;
    DatabaseReference ref;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        adapter = new GroupRidingAdapter();
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        createGroup = findViewById(R.id.btn_createG);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("GROUP_RIDING");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String groupName = snapshot.getKey();
                    String startPoint = snapshot.child("startPoint").child("name").getValue().toString();
                    String endPoint = snapshot.child("endPoint").child("name").getValue().toString();
                    String leader = snapshot.child("leader_nick").getValue().toString();
                    groupRideItems.add(new GroupRideItem(groupName, startPoint, endPoint, leader));
                }
                recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), groupRideItems, R.layout.activity_group));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GroupRideActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

    }
    public class GroupRidingAdapter extends BaseAdapter{

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
            CustomGroupRide view = new CustomGroupRide(context);
            return view;
        }

       public void addItem(GroupRideItem item){ groupRideItems.add(item); }
        //public void clearItem(){rankingItems.clear();}
    }
}


