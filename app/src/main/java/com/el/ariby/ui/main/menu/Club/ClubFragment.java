package com.el.ariby.ui.main.menu.Club;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.el.ariby.ui.main.menu.Club.ClubItem;
import com.el.ariby.ui.main.menu.Club.CustomClub;
import com.el.ariby.R;

import java.util.ArrayList;

public class ClubFragment extends Fragment {
    ListView listClub;
    ClubAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_club, container,false);
        listClub = v.findViewById(R.id.list_club);
        adapter=new ClubAdapter();

        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));
        adapter.addItem(new ClubItem(R.drawable.testimage, "아리비 자전거 회원들", "아리비","14","서울시 구로구"));

        listClub.setAdapter(adapter);

        return v;
    }

    class ClubAdapter extends BaseAdapter {
        ArrayList<ClubItem> items =new ArrayList<ClubItem>();

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
            CustomClub view = new CustomClub(getActivity());
            ClubItem item = items.get(position);
            view.setImgNickMain(item.getMainLogo());
            view.setTxtTitle(item.getTitle());
            view.setTxtNickname(item.getNick());
            view.setTxtNumber(item.getNumber());
            view.setTxtMap(item.getMap());
            return view;
        }
    }
}