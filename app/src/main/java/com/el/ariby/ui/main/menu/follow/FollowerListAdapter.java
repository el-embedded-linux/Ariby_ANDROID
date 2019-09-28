package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.el.ariby.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowerListAdapter extends RecyclerView.Adapter<FollowerListAdapter.ViewHolder> {
    Context context;
    private ArrayList<FollowItem> mlist;
    int item_layout;

    FollowerListAdapter(Context context, ArrayList<FollowItem> list, int item_layout) {
        this.context = context;
        this.mlist = list;
        this.item_layout = item_layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_follower_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        String nickname = mlist.get(i).getNick();
        String followNum = mlist.get(i).getFollwingNum();
        String followerNum = mlist.get(i).getFollowerNum();
        Log.e("테스트", nickname + followerNum + followNum + mlist.get(i).getIconDrawable());
        Glide.with(context)
                .load(mlist.get(i).getIconDrawable())
                .centerCrop()
                .into(holder.imgFollowProfile);
        holder.txtFollowNickname.setText(nickname);
        holder.txtFollowNum.setText(followNum);
        holder.txtFollowerNum.setText(followerNum);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFollowProfile;
        TextView txtFollowNickname;
        TextView txtFollowNum;
        TextView txtFollowerNum;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            imgFollowProfile = itemView.findViewById(R.id.img_follower_profile);
            txtFollowNickname = itemView.findViewById(R.id.txt_follower_nickname);
            txtFollowNum = itemView.findViewById(R.id.txt_follower_follow_num);
            txtFollowerNum = itemView.findViewById(R.id.txt_follower_follower_num);
        }
    }
}
