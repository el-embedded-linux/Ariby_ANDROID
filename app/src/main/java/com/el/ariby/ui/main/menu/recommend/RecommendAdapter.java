package com.el.ariby.ui.main.menu.recommend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.el.ariby.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    Context context;
    List<Recommend_item> items;
    int item_layout;
    DatabaseReference ref;
    FirebaseDatabase database;


    public RecommendAdapter(Context context, List<Recommend_item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recommend_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("COURSE");
        final Recommend_item item = items.get(position);
        Glide.with(holder.itemView.getContext()).load(item.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.image);
        holder.title.setText(item.getTitle());
        holder.rating.setText(item.getRating());
        holder.kmText.setText(item.getKmText());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                final String course = item.getTitle();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String startX = "37.565326";
                        String startY = "126.621893";
                        String endX = "37.591902";
                        String endY = "126.784270";
                        String nameCom = null;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            nameCom = snapshot.child("name").getValue().toString();
                            Log.e("courseName : ", snapshot.getKey());
                            if(course.equals(nameCom)){
                                Log.d("nameCom", nameCom);
                                startX = snapshot.child("startPoint").child("lat").getValue().toString();
                                startY = snapshot.child("startPoint").child("lon").getValue().toString();

                                endX = snapshot.child("endPoint").child("lat").getValue().toString();
                                endY = snapshot.child("endPoint").child("lon").getValue().toString();

                                Log.e("Adapter : ", startX+",   "+startY);

                                Intent intent = new Intent(context, CourseMapActivity .class);
                                intent.putExtra("courseName", nameCom);
                                intent.putExtra("startX", startX);
                                intent.putExtra("startY", startY);

                                intent.putExtra("endX", endX);
                                intent.putExtra("endY", endY);

                                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));

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
        ImageView image;
        TextView title, rating, kmText;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            rating = (TextView) itemView.findViewById(R.id.rating);
            kmText = (TextView) itemView.findViewById(R.id.kmText);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}

