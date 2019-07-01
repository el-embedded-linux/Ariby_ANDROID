package com.el.ariby.ui.main.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    private DustFragment dustFragment = new DustFragment();
    private ClubFragment clubFragment=new ClubFragment();
    private RankFragment rankFragment=new RankFragment();
    private HealthFragment healthFragment=new HealthFragment();
    private CourseFragment courseFragment=new CourseFragment();

    public  ContentsPagerAdapter(FragmentManager fm,int pageCount) {
        super(fm);
        this.mPageCount=pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return dustFragment;
            case 1:
                return clubFragment;
            case 2:
                return rankFragment;
            case 3:
                return healthFragment;
            case 4:
                return courseFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
