package com.el.ariby.ui.main.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.CreateGroupActivity;
import com.el.ariby.ui.main.menu.groupRiding.CustomGroupRide;
import com.el.ariby.ui.main.menu.groupRiding.GroupRideItem;
import com.el.ariby.ui.main.menu.groupRiding.RecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CourseFragment extends Fragment {
    private static View view;
    Button createGroup;
    GroupRidingAdapter adapter;
    ArrayList<GroupRideItem> groupRideItems = new ArrayList<GroupRideItem>();

    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference myGroupRef;
    DatabaseReference userRef;
    final ArrayList<String> myGroupList = new ArrayList<>();
    int leaderNo = 0;
    int i=0;

    final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    final String myUid = mUser.getUid();
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        adapter = new GroupRidingAdapter();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        createGroup = view.findViewById(R.id.btn_createG);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("GROUP_RIDING");
        myGroupRef = database.getReference("GROUP_RIDING_MEMBERS");

        callList();
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    public void callList(){
        myGroupRef.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                myGroupList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String groupName = dataSnapshot1.getKey();
                        myGroupList.add(groupName);
                        //i++;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            int index = 0;
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clearItem();
                for(int i=0 ; i < myGroupList.size(); i++ ) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String groupName = snapshot.getKey();
                        if (myGroupList.get(index).equals(groupName)) {
                            String startPoint = snapshot.child("startPoint").child("name").getValue().toString();
                            String endPoint = snapshot.child("endPoint").child("name").getValue().toString();
                            String leader = snapshot.child("members").child("0").child("nickname").getValue().toString();
                            groupRideItems.add(new GroupRideItem(groupName, startPoint, endPoint, leader));
                            index++;
                            break;
                        }
                    }
                }
                recyclerView.setAdapter(new RecyclerAdapter(getContext(), groupRideItems, R.layout.activity_group));
                myGroupList.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        callList();
    }

    public class GroupRidingAdapter extends BaseAdapter {

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

        public void clearItem(){groupRideItems.clear();}
    }





}

