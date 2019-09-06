package com.el.ariby.ui.main.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.recommend.CustomRecommend;
import com.el.ariby.ui.main.menu.recommend.RecommendAdapter;
import com.el.ariby.ui.main.menu.recommend.Recommend_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecommendCourseFragment extends Fragment {
    CourseAdapter adapter;
    ArrayList<Recommend_item> RecommentItems = new ArrayList<Recommend_item>();

    FirebaseDatabase database;
    DatabaseReference ref;

    int i = 0;

    @Override
    public void onResume() {
        super.onResume();
        RecommentItems.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend_course, container, false);
        adapter = new CourseAdapter();
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("COURSE");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int number = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String km = snapshot.child("km").getValue().toString();
                    RecommentItems.add(new Recommend_item(image, name, km, String.valueOf(number)));
                    number++;
                    Log.d("number", String.valueOf(number));
                }
                recyclerView.setAdapter(new RecommendAdapter(getActivity(), RecommentItems, R.layout.fragment_recommend_course));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }

    public class CourseAdapter extends BaseAdapter {

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
            CustomRecommend view = new CustomRecommend(context);
            return view;
        }

        public void addItem(Recommend_item item) {
            RecommentItems.add(item);
        }
        //public void clearItem(){rankingItems.clear();}
    }
}