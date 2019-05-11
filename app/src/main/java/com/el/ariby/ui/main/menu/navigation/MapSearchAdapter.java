package com.el.ariby.ui.main.menu.navigation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.el.ariby.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MapSearchAdapter extends RecyclerView.Adapter<MapViewHolder> {

    private ArrayList<MapData> mList = new ArrayList<>();

    public MapSearchAdapter() {
        super();
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_map_name, viewGroup, false);

        return new MapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MapViewHolder viewHolderTest, int i) {
        viewHolderTest.mapName.setText(mList.get(i).getMapName());
        viewHolderTest.lat.setText(mList.get(i).getLat());
        viewHolderTest.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] str = new String[4];
                int i=0;
                Intent intent = ((MapSearchActivity) v.getContext()).getIntent();
                StringTokenizer token1= new StringTokenizer(viewHolderTest.lat.getText().toString()," ");
                while (token1.hasMoreTokens()) {
                    str[i++]=token1.nextToken();
                }
                intent.putExtra("result_msg", viewHolderTest.mapName.getText().toString());
                intent.putExtra("X", str[1]);
                intent.putExtra("Y", str[3]);
                ((MapSearchActivity) v.getContext()).setResult(3000, intent);
                ((MapSearchActivity) v.getContext()).finish();
                Toast.makeText(v.getContext(), viewHolderTest.mapName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    public void replaceAll(List<MapData> mArrayListTest) {
        mList.clear();
        mList.addAll(mArrayListTest);
        notifyDataSetChanged();
    }
}
