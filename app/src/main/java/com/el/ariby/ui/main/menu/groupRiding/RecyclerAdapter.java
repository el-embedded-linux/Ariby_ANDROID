package com.el.ariby.ui.main.menu.groupRiding;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.R;


import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    List<GroupRideItem> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<GroupRideItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_group_ride, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GroupRideItem item = items.get(position);
        holder.groupName.setText(item.getGroupName());
        holder.start.setText(item.getStart());
        holder.end.setText(item.getEnd());
        holder.location.setText(item.getLocation());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getGroupName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        TextView start;
        TextView end;
        TextView location;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.txt_groupName);
            start = (TextView) itemView.findViewById(R.id.txt_departure);
            end = (TextView)itemView.findViewById(R.id.txt_destination);
            location = (TextView)itemView.findViewById(R.id.txt_loc);
            cardview = (CardView) itemView.findViewById(R.id.custom_group_ride);
        }
    }
}
