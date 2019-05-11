package com.el.ariby.ui.main.menu.ranking;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class RankingAdapter extends BaseAdapter {

    ArrayList<RankingItem> rankingItems = new ArrayList<RankingItem>();
    @Override
    public int getCount() { return rankingItems.size(); }
    @Override
    public Object getItem(int position) { return rankingItems.get(position);}
    @Override
    public long getItemId(int position) { return position; }
    public void addItem(RankingItem item){ rankingItems.add(item); }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        CustomRanking view = new CustomRanking(context);
        RankingItem item = rankingItems.get(position);
        view.setTxtRankNick(item.getNickname());
        view.setTxtRidingTime(item.getRidingTime());
        Log.d("getRidingTime : ", item.getRidingTime());
        view.setTxtRank(item.getRank());
        view.setImgRankProfile(item.getImgProfile());
        view.setTxtRidingDis(item.getRidingDis());

        //ToDo. RankingItem  채우고 마저 하기.
        return view;
    }

    public void clearItem(){rankingItems.clear();}
}

