package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.CustomFriendList;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.FriendListItem;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.FriendListRecyclerAdapter;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {
    TextView nickname;
    ImageView profile;
    Button invite;
    ArrayList<FriendListItem> friendListItems = new ArrayList<FriendListItem>();
    AddFriendAdapter adapter;

    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_to_group);
        adapter = new AddFriendAdapter();
        RecyclerView recyclerView = findViewById(R.id.follower_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        nickname = findViewById(R.id.txt_friend_nick);
        profile = findViewById(R.id.img_Prof);
        invite = findViewById(R.id.btn_invite);

        while(i<10){
            friendListItems.add(new FriendListItem("원경","https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/profile%2FIMG_20180805_155532958.jpg?alt=media&token=56479dfa-dc24-4bb7-9366-31034448899c"));
            i = i+1;
        }
        recyclerView.setAdapter(new FriendListRecyclerAdapter(getApplicationContext(), friendListItems, R.layout.activity_add_friend_to_group));
    }


    public class AddFriendAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            CustomFriendList view = new CustomFriendList(context);
            return view;
        }
        public void addItem(FriendListItem item){friendListItems.add(item);}
    }

}
