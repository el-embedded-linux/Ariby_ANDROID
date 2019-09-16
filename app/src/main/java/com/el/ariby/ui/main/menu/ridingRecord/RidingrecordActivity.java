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
    DatabaseReference user;
    String myNick;
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
        ref = database.getReference("RECORD");


    doWork(new Callback() {
        @Override
        public void callback() {

        }
    });


        ref.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Log.d("snapshot",dataSnapshot.getRef().toString());
                        String ridingtime = dataSnapshot.child("dailyData").child("1567263600000").child("dailyTotalTime").getValue().toString();
                        String date = dataSnapshot.child("dailyData").child("1567263600000").child("date").getValue().toString();
                        Log.d("nickname", date + ridingtime);
                        RidingrecordItems.add(new Ridingrecorditem(date, ridingtime));
                        adapter.notifyDataSetChanged();

                    //}
                    recyclerView.setAdapter(new com.el.ariby.ui.main.menu.ridingRecord.RidingrecordAdapter(getApplicationContext(), RidingrecordItems, R.layout.activity_ridingrecord));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        void callback();             // Callback 인터페이스 내의 속이 없는 껍데기 함수
    }


    public void doWork(final Callback mCallback) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String Timestamp = snapshot.child("dailyData").getKey().toString();
                    Log.d("Timestamp",Timestamp);
                    record.add(Timestamp);
                }
                mCallback.callback();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
