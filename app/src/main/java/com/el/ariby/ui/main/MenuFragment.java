package com.el.ariby.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.el.ariby.MainActivity;
import com.el.ariby.R;
import com.el.ariby.databinding.FragmentMenuBinding;
import com.el.ariby.ui.main.menu.ContentsPagerAdapter;

public class MenuFragment extends Fragment {
    private ContentsPagerAdapter mContentPagerAdapter;
    private FragmentMenuBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentMenuBinding.bind(getView());
        mBinding.layoutTab.addTab(mBinding.layoutTab.newTab().setText("클럽"));
        mBinding.layoutTab.addTab(mBinding.layoutTab.newTab().setText("친구"));
        mBinding.layoutTab.addTab(mBinding.layoutTab.newTab().setText("랭킹"));
        mBinding.layoutTab.addTab(mBinding.layoutTab.newTab().setText("헬스"));
        mBinding.layoutTab.addTab(mBinding.layoutTab.newTab().setText("도전과제"));

        mContentPagerAdapter = new ContentsPagerAdapter(
                getActivity().getSupportFragmentManager(), mBinding.layoutTab.getTabCount());
        mBinding.pagerContent.setAdapter(mContentPagerAdapter);
        mBinding.pagerContent.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mBinding.layoutTab));
        mBinding.layoutTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.pagerContent.setCurrentItem(tab.getPosition());
                Toast.makeText(getActivity(),"선택됨" + tab.getPosition(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mBinding.pagerContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mBinding.pagerContent.setCurrentItem(tab.getPosition());
            }
        });
    }
}
