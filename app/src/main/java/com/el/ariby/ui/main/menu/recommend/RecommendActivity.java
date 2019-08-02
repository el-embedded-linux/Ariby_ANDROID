package com.el.ariby.ui.main.menu.recommend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.RecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {
    CourseAdapter adapter;
    ArrayList<Recommend_item> RecommentItems = new ArrayList<Recommend_item>();

    FirebaseDatabase database;
    DatabaseReference ref;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        adapter = new CourseAdapter();
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("COURSE");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String km = snapshot.child("km").getValue().toString();
                    RecommentItems.add(new Recommend_item(image, name, km));
                }
                recyclerView.setAdapter(new RecommendAdapter(getApplicationContext(), RecommentItems, R.layout.activity_recommend));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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

        public void addItem(Recommend_item item){ RecommentItems.add(item); }
        //public void clearItem(){rankingItems.clear();}
    }
}