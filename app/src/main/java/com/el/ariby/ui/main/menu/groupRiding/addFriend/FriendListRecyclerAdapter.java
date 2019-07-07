package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.el.ariby.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class FriendListRecyclerAdapter extends RecyclerView.Adapter<FriendListRecyclerAdapter.ViewHolder>{

        Context context;
        List<FriendListItem> items;
        int item_layout;

        public FriendListRecyclerAdapter(Context context, List<FriendListItem> items, int item_layout) {
            this.context = context;
            this.items = items;
            this.item_layout = item_layout;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_friend_to_group, null);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final FriendListItem item = items.get(position);
            viewHolder.nickName.setText(item.getFriend_nick());
            Picasso.with(context).load(item.getProfile()).into(viewHolder.profile);

        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nickName;
            ImageView profile;

            public ViewHolder(View itemView) {
                super(itemView);
                nickName = (TextView) itemView.findViewById(R.id.txt_friend_nick);
                profile = (ImageView) itemView.findViewById(R.id.img_Prof);

            }

        }


    }




