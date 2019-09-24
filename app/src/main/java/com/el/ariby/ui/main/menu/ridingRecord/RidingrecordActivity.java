package com.el.ariby.ui.main.menu.ridingRecord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.el.ariby.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RidingrecordActivity extends AppCompatActivity {
    RidingrecordAdapter adapter;
    ArrayList<Ridingrecorditem> RidingrecordItems = new ArrayList<Ridingrecorditem>();
    ArrayList<String> record = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridingrecord);
        adapter = new RidingrecordAdapter();
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String myUid = mUser.getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("RECORD").child(myUid).child("dailyData");


    doWork(new Callback() {
        @Override
        public void success(final ArrayList<String> data) {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String uid = snapshot.getKey();
                            if(data.get(i).equals(uid)){
                                String ridingtime = dataSnapshot.child(uid).child("dailyTotalTime").getValue().toString();
                                String date = dataSnapshot.child(uid).child("date").getValue().toString();
                                RidingrecordItems.add(new Ridingrecorditem(date, ridingtime));
                                adapter.notifyDataSetChanged();

                                if(data.size()-1 > i){
                                    i++;
                                }
                            }

                        }
                        recyclerView.setAdapter(new com.el.ariby.ui.main.menu.ridingRecord.RidingrecordAdapter(getApplicationContext(), RidingrecordItems, R.layout.activity_ridingrecord));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    });

    }
    public class RidingrecordAdapter extends BaseAdapter {

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
            CustomRidingrecord view = new CustomRidingrecord(context);
            return view;
        }

        public void addItem(Ridingrecorditem item){ RidingrecordItems.add(item); }
        //public void clearItem(){rankingItems.clear();}
    }
    public interface Callback {
        void success(ArrayList<String> data);
    }

    public void doWork(final Callback mCallback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String recordUid;
                        recordUid = snapshot.getKey();
                        String str = snapshot.getValue().toString();
                        if(str.contains("dailyTotalTime") && str.contains("date")){
                            record.add(recordUid);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mCallback.success(record);
    }
}