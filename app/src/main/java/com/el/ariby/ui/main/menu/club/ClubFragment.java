package com.el.ariby.ui.main.menu.club;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.el.ariby.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClubFragment extends Fragment {
    Button btnCreate;
    Button btnFind;
    ListView listClub;
    ClubAdapter adapter;
    DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_club, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("club");
        final SwipeRefreshLayout mSwipeRefreshLayout = v.findViewById(R.id.swipe_layout);

        btnCreate = v.findViewById(R.id.btn_create);
        btnFind = v.findViewById(R.id.btn_find);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClubCreateActivity.class);
                startActivity(intent);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClubSearchActivity.class);
                startActivity(intent);
            }
        });

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


        listClub = v.findViewById(R.id.list_club);
        adapter = new ClubAdapter();

        listClub.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clearItem();
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
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clearItem();
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
    }

    class ClubAdapter extends BaseAdapter {
        ArrayList<ClubItem> items = new ArrayList<ClubItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public Object getItem(int position) {
            return items.get(position);
        }

        public void addItem(ClubItem item) {
            items.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomClub view = new CustomClub(getActivity());
            ClubItem item = items.get(position);
            view.setImgNickMain(item.getMainLogo());
            view.setTxtTitle(item.getTitle());
            view.setTxtNickname(item.getNick());
            view.setTxtNumber(String.valueOf(item.getNumber()));
            view.setTxtMap(item.getMap());
            return view;
        }

        public void clearItem() {
            items.clear();
        }
    }
}