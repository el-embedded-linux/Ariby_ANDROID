package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import android.content.Context;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.el.ariby.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class FriendListRecyclerAdapter extends RecyclerView.Adapter<GroupViewHolder>{

        Context context;
        private ArrayList<FriendListItem> arrayList;
        int item_layout;
        private SparseBooleanArray mSelectedItemsIds;



        public FriendListRecyclerAdapter(Context context, ArrayList<FriendListItem> items) {
            this.context = context;
            this.arrayList = items;
            mSelectedItemsIds = new SparseBooleanArray();
        }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
            ViewGroup mainGroup = (ViewGroup)mInflater.inflate(R.layout.custom_add_friend_to_group, viewGroup,false);
        return new GroupViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i) {
        groupViewHolder.nickName.setText(arrayList.get(i).getFriend_nick());
        Picasso.with(context).load(arrayList.get(i).getProfile()).into(groupViewHolder.profile);
        /*change background color of the selected items in list view */
        if(mSelectedItemsIds.get(i)){
            groupViewHolder.invite.setText("선택됨");
            groupViewHolder.invite.setBackgroundResource(R.drawable.selected_button_border);
            groupViewHolder.invite.setTextColor(Color.WHITE);
        }else{
            groupViewHolder.invite.setText("그룹 초대하기");
            groupViewHolder.invite.setBackgroundResource(R.drawable.friend_add_button_border);
            groupViewHolder.invite.setTextColor(Color.rgb(9, 107, 58));
        }

        }

        /*항목 선택, 선택 해제 위한 함수*/
    //Toggle Selection
    public void toggleSelection(int position){
        selectView(position, !mSelectedItemsIds.get(position));
    }

    //Remove selected selections
    public void removeSelection(){
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value){
        if(value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount(){
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds(){
        return mSelectedItemsIds;
    }



        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }




    }




