package com.el.ariby.ui.main.menu.club;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ClubMemberAdapter extends BaseAdapter {
    ArrayList<ClubMemberItem> items = new ArrayList<>();

    public void addItem(ClubMemberItem test) {
        items.add(test);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomMemberView view = new CustomMemberView(parent.getContext());
        ClubMemberItem item = items.get(position);
        view.setImgProfile(item.getResId());
        view.setTxtTitle(item.getTxtNickname());

        return view;
    }
}
