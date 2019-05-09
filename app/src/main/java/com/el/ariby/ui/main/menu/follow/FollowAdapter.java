package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

import java.util.ArrayList;

public class FollowAdapter extends BaseAdapter implements Filterable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    ArrayList<FollowItem> FollowItemList = new ArrayList<FollowItem>();
    ArrayList<FollowItem> filteredItemList = FollowItemList;

    Filter listFilter;

    public FollowAdapter(){

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
        // "custom_follow_list" Layout을 inflate하여 convertView 참조 획득.

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_follow_list,parent, false);
        }
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        ImageView iconImageView = convertView.findViewById(R.id.imageView1);
        TextView titleTextView = convertView.findViewById(R.id.textView1);
        //TextView descTextView = convertView.findViewById(R.id.textView2);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        FollowItem item = filteredItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        Glide.with(convertView).load(item.getIconDrawable()).into(iconImageView);
        titleTextView.setText(item.getNick());
        //descTextView.setText(item.getDescStr());
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
