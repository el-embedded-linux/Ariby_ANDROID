package com.el.ariby.ui.main.menu.club;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

import java.util.ArrayList;
import java.util.List;

public class ClubSearchAdapter extends BaseAdapter implements Filterable {
    ArrayList<ClubItem> listViewItemList = new ArrayList<ClubItem>();
    ArrayList<ClubItem> filteredItemList = listViewItemList;
    Filter listFilter;

    ImageView imgNickMain; // 클럽 대표사진
    TextView txtTitle; // 클럽명
    TextView txtNickname; // 닉네임
    TextView txtNumber; // 인원수
    TextView txtMap; // 위치명

    public ClubSearchAdapter() {
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_club,parent,false);
        }
        /*
        *     String mainLogo; // 클럽 대표사진
    String title; // 클럽명
    String nick; // 닉네임
    long number; // 인원수
    String map; // 위치명*/


        imgNickMain = convertView.findViewById(R.id.img_main);
        txtTitle = convertView.findViewById(R.id.txt_title);
        txtNickname = convertView.findViewById(R.id.txt_nickname);
        txtNumber = convertView.findViewById(R.id.txt_number);
        txtMap = convertView.findViewById(R.id.txt_map);

        ClubItem item = filteredItemList.get(position);

        Glide.with(convertView).load(item.getMainLogo()).into(imgNickMain);
        txtTitle.setText(item.getTitle());
        txtNickname.setText(item.getNick());
        txtNumber.setText(String.valueOf(item.getNumber()));
        txtMap.setText(item.getMap());
        return convertView;


    }

    public void addItem(ClubItem item) {
        listViewItemList.add(item);
    }


    @Override
    public Filter getFilter() {
        if(listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            } else {
                ArrayList<ClubItem> itemList = new ArrayList<ClubItem>();
                for (ClubItem item : listViewItemList) {
                    if(item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
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
            filteredItemList = (ArrayList<ClubItem>) results.values;

            if(results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
