package com.el.ariby.ui.main.menu.groupRiding;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.groupRidingMap.Group_MapActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    List<GroupRideItem> items;
    int item_layout;
    DatabaseReference ref;
    FirebaseDatabase database;


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

    public void updateData(ArrayList<GroupRideItem> viewModels){
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("GROUP_RIDING");
        final GroupRideItem item = items.get(position);
        holder.groupName.setText(item.getGroupName());
        holder.start.setText(item.getStart());
        holder.end.setText(item.getEnd());
        holder.location.setText(item.getLocation());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getGroupName(), Toast.LENGTH_SHORT).show();
                final String group = item.getGroupName();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String startX = null;
                        String startY = null;
                        String endX = null;
                        String endY = null;
                        String nameCom = null;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            nameCom = snapshot.getKey();
                            Log.e("groupName : ", snapshot.getKey());
                            if(group.equals(nameCom)){
                                Log.d("nameCom", nameCom);
                                startX = snapshot.child("startPoint").child("lon").getValue().toString();
                                startY = snapshot.child("startPoint").child("lat").getValue().toString();

                                endX = snapshot.child("endPoint").child("lon").getValue().toString();
                                endY = snapshot.child("endPoint").child("lat").getValue().toString();
                                Log.e("Adapter : ", startX+",   "+startY);

                                Intent intent = new Intent(context, Group_MapActivity.class);
                                intent.putExtra("groupName", nameCom);
                                intent.putExtra("startX", startX);
                                intent.putExtra("startY", startY);

                                intent.putExtra("endX", endX);
                                intent.putExtra("endY", endY);

                                context.startActivity(intent);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
