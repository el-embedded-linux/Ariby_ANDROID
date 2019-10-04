package com.el.ariby.ui.main.menu.challenge.challenge_child;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class ChallengeAdapter extends FragmentStatePagerAdapter {
    private int chall_mPageCount;
//    private Challenge_ChildFragment challenge_ChildFragment=new Challenge_ChildFragment();
//    private Challenge_ChildFragment2 challenge_ChildFragment2 = new Challenge_ChildFragment2();
//    private Challenge_ChildFragment3 challenge_ChildFragment3=new Challenge_ChildFragment3();

    public  ChallengeAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.chall_mPageCount=pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Challenge_ChildFragment chall1 = new Challenge_ChildFragment();
                return chall1;
            case 1:
                Challenge_ChildFragment2 chall2 = new Challenge_ChildFragment2();
                return chall2;
            case 2:
                Challenge_ChildFragment3 chall3 = new Challenge_ChildFragment3();
                return chall3;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return chall_mPageCount;
    }
}

