package com.el.ariby.ui.main.menu.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.el.ariby.MapInputActivity;
import com.el.ariby.NavigationActivity;
import com.el.ariby.R;

import java.util.ArrayList;

public class MapSearchAdapter extends RecyclerView.Adapter<MapViewHolder> {
    private ArrayList<MapData> mList;

    public MapSearchAdapter(ArrayList<MapData> list) {
        super();
        this.mList=list;
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_map_name,viewGroup,false);

        MapViewHolder viewHolder = new MapViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MapViewHolder viewHolderTest, int i) {
        viewHolderTest.mapname.setText(mList.get(i).getMapname());
        viewHolderTest.lat.setText(mList.get(i).getLat());
        viewHolderTest.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result_msg",viewHolderTest.mapname.getText().toString());
                ((MapInputActivity)v.getContext()).setResult(3000,intent);
                ((MapInputActivity)v.getContext()).finish();
                Toast.makeText(v.getContext(),viewHolderTest.mapname.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
