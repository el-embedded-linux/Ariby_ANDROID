package com.el.ariby.ui.main.menu;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    private DustFragment dustFragment = new DustFragment();
    private ClubFragment clubFragment=new ClubFragment();
    private RankFragment rankFragment=new RankFragment();
    private RecommendCourseFragment recommendCourseFragment=new RecommendCourseFragment();
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
                return recommendCourseFragment;
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
