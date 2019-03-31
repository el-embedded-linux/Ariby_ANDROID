package com.el.ariby.ui.main.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.el.ariby.ui.main.menu.Club.ClubFragment;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    private ClubFragment clubFragment=new ClubFragment();
    private FriendFragment friendFragment = new FriendFragment();
    private RankFragment rankFragment=new RankFragment();
    private HealthFragment healthFragment=new HealthFragment();
    private ChallengeFragment challengeFragment=new ChallengeFragment();

    public ContentsPagerAdapter(FragmentManager fm,int pageCount) {
        super(fm);
        this.mPageCount=pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return clubFragment;
            case 1:
                return friendFragment;
            case 2:
                return rankFragment;
            case 3:
                return healthFragment;
            case 4:
                return challengeFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
