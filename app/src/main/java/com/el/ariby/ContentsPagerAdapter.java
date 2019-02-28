package com.el.ariby;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContentsPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;
    public ContentsPagerAdapter(FragmentManager fm,int pageCount) {
        super(fm);
        this.mPageCount=pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ClubFragment clubFragment=new ClubFragment();
                return clubFragment;
            case 1:
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            case 2:
                RankFragment rankFragment=new RankFragment();
                return rankFragment;
            case 3:
                HealthFragment healthFragment=new HealthFragment();
                return healthFragment;
            case 4:
                ChallengeFragment challengeFragment=new ChallengeFragment();
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
