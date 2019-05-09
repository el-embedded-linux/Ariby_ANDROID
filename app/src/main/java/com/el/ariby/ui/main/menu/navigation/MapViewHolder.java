package com.el.ariby.ui.main.menu.navigation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.R;

public class MapViewHolder extends RecyclerView.ViewHolder {
    TextView mapname;
    TextView lat;
    public MapViewHolder(View parent) {
        super(parent);
        this.mapname=parent.findViewById(R.id.mapname);
        this.lat = parent.findViewById(R.id.lat);
    }

}
