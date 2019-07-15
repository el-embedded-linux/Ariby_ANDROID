package com.el.ariby.ui.main.menu.groupRiding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.RecyclerView_Fragment;
import com.el.ariby.ui.main.menu.groupRiding.addFriend.ViewPagerAdapter;

public class AddFriendActivity extends AppCompatActivity {
    private static ViewPagerAdapter adapter;

    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);
        Toolbar toolbar =(Toolbar)findViewById(R.id.group_toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RecyclerView_Fragment(), "팔로잉 리스트");
        viewPager.setAdapter(adapter);
    }

    public Fragment getFragment(int pos){
        return adapter.getItem(pos);
    }



    }




