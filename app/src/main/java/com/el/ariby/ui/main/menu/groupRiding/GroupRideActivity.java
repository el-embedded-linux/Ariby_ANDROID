package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.el.ariby.R;
import java.util.ArrayList;

public class GroupRideActivity extends AppCompatActivity {
    Button createGroup;
    GroupRidingAdapter adapter;
    ArrayList<GroupRideItem> groupRideItems = new ArrayList<GroupRideItem>();


    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        adapter = new GroupRidingAdapter();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        createGroup = findViewById(R.id.btn_createG);

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GroupRideActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        while(i<10){
            groupRideItems.add(new GroupRideItem("Hello","Seoul","USA","Seoul"));

            i=i+1;
        }
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), groupRideItems, R.layout.activity_group));

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


