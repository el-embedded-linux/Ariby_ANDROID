package com.el.ariby.ui.main.menu.ridingRecord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.el.ariby.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RidingrecordAdapter extends RecyclerView.Adapter<RidingrecordAdapter.ViewHolder> {
    Context context;
    List<Ridingrecorditem> items;
    int item_layout;
    DatabaseReference ref;
    FirebaseDatabase database;


    public RidingrecordAdapter(Context context, List<Ridingrecorditem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_riding_record_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("COURSE");
        final Ridingrecorditem item = items.get(position);
        holder.date.setText(item.getDate());
        holder.ridingTime.setText(item.getRidingtime());
        holder.ridingKm.setText(item.getRidingkm());
        holder.ridingKcal.setText(item.getRidingkcal());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, ridingTime, ridingKm, ridingKcal;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            ridingTime = (TextView) itemView.findViewById(R.id.txt_time);
            ridingKm = (TextView) itemView.findViewById(R.id.txt_dis);
            ridingKcal = (TextView) itemView.findViewById(R.id.txt_kcal);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
