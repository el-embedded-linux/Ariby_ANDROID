package com.el.ariby;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentPagerAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container,false);
        mTabLayout = v.findViewById(R.id.layout_tab);
        mViewPager = v.findViewById(R.id.pager_content);
        mTabLayout.addTab(mTabLayout.newTab().setText("클럽"));
        mTabLayout.addTab(mTabLayout.newTab().setText("친구"));
        mTabLayout.addTab(mTabLayout.newTab().setText("랭킹"));
        mTabLayout.addTab(mTabLayout.newTab().setText("헬스"));
        mTabLayout.addTab(mTabLayout.newTab().setText("도전과제"));

        mContentPagerAdapter = new ContentsPagerAdapter(
                getActivity().getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mContentPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return v;
    }

}
