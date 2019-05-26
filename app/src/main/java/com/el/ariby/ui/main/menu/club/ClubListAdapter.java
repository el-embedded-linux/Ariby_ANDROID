package com.el.ariby.ui.main.menu.club;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

class ClubListAdapter extends BaseAdapter {
    ArrayList<ClubItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public void addItem(ClubItem item) {
        items.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomClubView view = new CustomClubView(parent.getContext());
        ClubItem item = items.get(position);
        view.setImgNickMain(item.getMainLogo());
        view.setTxtTitle(item.getTitle());
        view.setTxtNickname(item.getNick());
        view.setTxtNumber(String.valueOf(item.getNumber()));
        view.setTxtMap(item.getMap());
        return view;
    }

    public void clearItem() {
        items.clear();
    }
}