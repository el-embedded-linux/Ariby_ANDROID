package com.el.ariby.ui.main.menu.navigation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.el.ariby.R;

import java.util.ArrayList;
import java.util.List;

public class MapSearchAdapter extends RecyclerView.Adapter<MapViewHolder> {

    private ArrayList<MapData> mList = new ArrayList<>();
    private MapDataOnClickListener mapDataOnClickListener;

    /**
     * 어댑터 아이템 클릭시 이벤트 전달
     */
    public interface MapDataOnClickListener {
        void onClickAdapterItem(MapData mapData);
    }

    public MapSearchAdapter(MapDataOnClickListener mapDataOnClickListener) {
        this.mapDataOnClickListener = mapDataOnClickListener;
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_map_name,
                viewGroup,
                false);

        return new MapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MapViewHolder mapViewHolder, int position) {
        final MapData mapData = mList.get(position);
        mapViewHolder.mapName.setText(mapData.getMapName());
        mapViewHolder.lat.setText(mapData.getLat());
        mapViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapDataOnClickListener.onClickAdapterItem(mapData);
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
