package com.el.ariby.ui.main.menu.ridingRecord;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    TextView emptyView;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ridingrecord);
        adapter = new RidingrecordAdapter();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        emptyView = findViewById(R.id.txt_riding_record_empty_view);

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        final String myUid = mUser.getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("RECORD").child(myUid).child("dailyData").child("data");



            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String timestamp = snapshot.getKey();
                            Log.d("timestamp : ", timestamp);
                                String ridingTime = dataSnapshot.child(timestamp).child("time").getValue().toString();
                                String date = dataSnapshot.child(timestamp).child("date").getValue().toString();
                                String ridingDis = dataSnapshot.child(timestamp).child("distance").getValue().toString()+"km";
                                String kcal = dataSnapshot.child(timestamp).child("kcal").getValue().toString()+"kcal";
                                RidingrecordItems.add(new Ridingrecorditem(date, ridingDis, ridingTime, kcal));
                                adapter.notifyDataSetChanged();
                            }

                        }
                        recyclerView.setAdapter(new com.el.ariby.ui.main.menu.ridingRecord.RidingrecordAdapter(getApplicationContext(), RidingrecordItems, R.layout.activity_ridingrecord));
                        adapter.notifyDataSetChanged();
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
                if(record.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }else{
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mCallback.success(record);
    }
}
