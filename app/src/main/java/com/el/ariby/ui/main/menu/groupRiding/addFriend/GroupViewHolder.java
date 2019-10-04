package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.el.ariby.R;

public class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView nickName;
        ImageView profile;
        Button invite;

        public GroupViewHolder(View itemView) {
            super(itemView);
            nickName = (TextView) itemView.findViewById(R.id.txt_friend_nick);
            profile = (ImageView) itemView.findViewById(R.id.img_Prof);
            invite = (Button)itemView.findViewById(R.id.btn_invite);
        }
}
