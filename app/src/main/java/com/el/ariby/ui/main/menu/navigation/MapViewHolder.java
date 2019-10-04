package com.el.ariby.ui.main.menu.navigation;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.el.ariby.R;

public class MapViewHolder extends RecyclerView.ViewHolder {
    TextView mapName;
    TextView lat;
    public MapViewHolder(View parent) {
        super(parent);
        this.mapName=parent.findViewById(R.id.mapName);
        this.lat = parent.findViewById(R.id.lat);
    }

}
