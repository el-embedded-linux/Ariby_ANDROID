package com.el.ariby.ui.main.menu.recommend;

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
    final int ITEM_SIZE = 1;
    Button day,week,month;
    TextView rating;
    RatingBar ratingBar;
    FirebaseDatabase database;
    DatabaseReference ref;
    ImageView image;
    ArrayList<String> ImageUrl = new ArrayList<>();
    List<Recommend_item> items = new ArrayList<>();
    RecommendAdapter adapter;
    boolean finish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        day = findViewById(R.id.day);
        week = findViewById(R.id.week);
        month = findViewById(R.id.month);
        rating = findViewById(R.id.rating);
        ratingBar = findViewById(R.id.ratingbar);
        image =findViewById(R.id.image);
        database = FirebaseDatabase.getInstance();

        adapter =new RecommendAdapter(getApplicationContext(), items, R.layout.activity_recommend);

        final List<Recommend_item> items = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        doWork(new Callback() {
            @Override
            public void callback(ArrayList<String> data) {
                Log.d("data1", String.valueOf(data));
            }
        });

            Recommend_item[] item = new Recommend_item[ITEM_SIZE];

            for (int i = 0; i < ITEM_SIZE; i++) {
                items.add(item[0] = new Recommend_item("https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/profile%2FIMG_20181223_152057010.jpg?alt=media&token=f7ef5319-3f77-4d41-894c-9470ac5bbb3e", "강촌 자전거 하이킹", "21km", "1"));
            }

        add();

        recyclerView.setAdapter(new RecommendAdapter(getApplicationContext(), items, R.layout.activity_recommend));
    }

    //)
    public interface Callback {
        void callback(ArrayList<String> data);             // Callback 인터페이스 내의 속이 없는 껍데기 함수
    }

    public void doWork(final Callback mCallback) {
        ref = database.getReference("COURSE");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String list = String.valueOf(snapshot.child("image").getValue());
                    ImageUrl.add(list);
                    Log.d("url111", ImageUrl.get(0));

                }
                mCallback.callback(ImageUrl);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void add(){

        Recommend_item[] item = new Recommend_item[ITEM_SIZE];

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[0] = new Recommend_item("https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/profile%2FIMG_20181223_152057010.jpg?alt=media&token=f7ef5319-3f77-4d41-894c-9470ac5bbb3e", "강촌 자전거 하이킹", "21km", "1"));
        }adapter.notifyDataSetChanged();
            Log.d("asd","asd11");
    }
}

