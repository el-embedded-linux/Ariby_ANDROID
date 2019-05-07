package com.el.ariby.ui.main.menu.challenge.challenge_child;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.challenge.ChallengeItem;
import com.el.ariby.ui.main.menu.challenge.CustomChallenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Challenge_ChildFragment3 extends Fragment {
    ListView listChallenge;
    Challenge_ChildFragment3.ChallengeAdapter adapter;
    DatabaseReference ref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.challenge_childfragment3, container,false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("challenge").child("chall_end");
        final SwipeRefreshLayout mSwipeRefreshLayout = v.findViewById(R.id.swipe_layout);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String chall_img = snapshot.child("chall_img").getValue().toString();
                    String comment = snapshot.child("comment").getValue().toString();
                    String exp = snapshot.child("exp").getValue().toString();
                    String unit = snapshot.child("unit").getValue().toString();
                    adapter.addItem(new ChallengeItem(chall_img, comment, exp, unit));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listChallenge = v.findViewById(R.id.list_challenge3);
        adapter = new Challenge_ChildFragment3.ChallengeAdapter();

        listChallenge.setAdapter(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String chall_img = snapshot.child("chall_img").getValue().toString();
                            String comment = snapshot.child("comment").getValue().toString();
                            String exp = snapshot.child("exp").getValue().toString();
                            String unit = snapshot.child("unit").getValue().toString();
                            adapter.addItem(new ChallengeItem(chall_img, comment, exp, unit));
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
                    String chall_img = snapshot.child("chall_img").getValue().toString();
                    String comment = snapshot.child("comment").getValue().toString();
                    String exp = snapshot.child("exp").getValue().toString();
                    String unit = snapshot.child("unit").getValue().toString();
                    adapter.addItem(new ChallengeItem(chall_img, comment, exp, unit));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class ChallengeAdapter extends BaseAdapter {
        ArrayList<ChallengeItem> items = new ArrayList<ChallengeItem>();

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

        public void addItem(ChallengeItem item) {
            items.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomChallenge view = new CustomChallenge(getActivity());
            ChallengeItem item = items.get(position);
            view.setChall_img(item.getChall_img());
            view.setChall_txtComment(item.getComment());
            view.setChall_txtExp(item.getExp());
            view.setChall_txtUnit(String.valueOf(item.getUnit()));
            return view;
        }

        public void clearItem() {
            items.clear();
        }
    }
}