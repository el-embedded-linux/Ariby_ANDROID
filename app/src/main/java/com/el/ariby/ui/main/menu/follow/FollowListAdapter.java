package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
import android.graphics.Color;
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
import com.el.ariby.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowListAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    ArrayList<FollowItem> FollowItemList = new ArrayList<FollowItem>();
    ArrayList<FollowItem> filteredItemList = FollowItemList;
    ArrayList<String> uidList = new ArrayList<>();
    DatabaseReference ref, followref;
    FirebaseDatabase database;
    FirebaseUser auth;
    Filter listFilter;
    FollowListAdapter adapter;
    public FollowListAdapter(){

    }
    //Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        auth = FirebaseAuth.getInstance().getCurrentUser();
        final String user = auth.getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("FRIEND").child("following").child(user);
        followref = database.getReference("FRIEND");
        adapter = new FollowListAdapter();
        // "custom_follow_list" Layout을 inflate하여 convertView 참조 획득.
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_follow_list,parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = convertView.findViewById(R.id.follow_image);
        TextView titleTextView = convertView.findViewById(R.id.follow_textView);
        final Button canclefollow = convertView.findViewById(R.id.cancle_follow);
        TextView followingNum = convertView.findViewById(R.id.following_num);
        TextView followerNum = convertView.findViewById(R.id.followers_num);
        FollowItem item = filteredItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(convertView).
                load(item.getIconDrawable()).
                centerCrop().
                into(iconImageView);
        titleTextView.setText(item.getNick());
        followingNum.setText(item.getFollwingNum());
        followerNum.setText(item.getFollowerNum());
        canclefollow.setTag(position);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                String uid = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    uid = snapshot.getKey();
                    Log.d("유저", uid + "position" + pos);

                    uidList.add(uid);
                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        canclefollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (Integer)v.getTag();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d("유저", uidList.get(pos));
                        followref.child("following").child((user)).child(uidList.get(pos)).setValue(null);
                        followref.child("follower").child(uidList.get(pos)).child((user)).setValue(null);
                        canclefollow.setText("팔로우");
                        canclefollow.setEnabled(false);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(context, "팔로잉이 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return convertView;

    }
    public void addItem(FollowItem item) {
        FollowItemList.add(item);
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null){
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends  Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length()==0){
                results.values = FollowItemList;
                results.count = FollowItemList.size();
            } else {
                ArrayList<FollowItem> itemList = new ArrayList<FollowItem>();

                for (FollowItem item : FollowItemList) {
                    if(item.getNick().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredItemList = (ArrayList<FollowItem>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
