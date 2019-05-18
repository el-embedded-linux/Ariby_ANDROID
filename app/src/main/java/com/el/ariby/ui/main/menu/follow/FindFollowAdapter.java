package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
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

public class FindFollowAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    ArrayList<FollowItem> FollowItemList = new ArrayList<FollowItem>();
    ArrayList<FollowItem> filteredItemList = FollowItemList;
    DatabaseReference ref, followref;
    FirebaseDatabase database;
    FirebaseUser auth;
    Filter listFilter;

    public FindFollowAdapter(){

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
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("USER");
        followref = database.getReference("FRIEND");
        // "custom_follow_list" Layout을 inflate하여 convertView 참조 획득.

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_user_list,parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        ImageView iconImageView = convertView.findViewById(R.id.imageView1);
        TextView titleTextView = convertView.findViewById(R.id.textView1);
        final Button addfollow = convertView.findViewById(R.id.add_friend);
        //TextView descTextView = convertView.findViewById(R.id.textView2);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        FollowItem item = filteredItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(convertView).load(item.getIconDrawable()).into(iconImageView);
        titleTextView.setText(item.getNick());
        addfollow.setTag(position);
        //descTextView.setText(item.getDescStr());
        addfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (Integer)v.getTag();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i=0;
                        String uid = null, user;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            uid = snapshot.getKey();
                            Log.d("nickname", uid + "position" + pos);
                            if(i == pos)break;
                            i++;
                        }
                        auth = FirebaseAuth.getInstance().getCurrentUser();
                        user = auth.getUid();
                        Log.d("유저", String.valueOf(user));
                        followref.child("following").child(String.valueOf(user)).child(uid).setValue("true");
                        followref.child("follower").child(uid).child(String.valueOf(user)).setValue("true");

                        addfollow.setText("팔로잉");
                        Toast.makeText(context, "팔로잉 되었습니다.", Toast.LENGTH_SHORT).show();
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
