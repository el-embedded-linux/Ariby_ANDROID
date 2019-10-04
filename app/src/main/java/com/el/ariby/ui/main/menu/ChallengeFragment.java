package com.el.ariby.ui.main.menu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.el.ariby.R;
import com.el.ariby.databinding.FragmentChallengeBinding;
import com.el.ariby.ui.main.menu.challenge.challenge_child.ChallengeAdapter;


public class ChallengeFragment extends Fragment {
    private ChallengeAdapter chall_mContentPagerAdapter;
    private FragmentChallengeBinding chall_mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chall_mBinding = FragmentChallengeBinding.bind(getView());
        chall_mBinding.challLayoutTab.addTab(chall_mBinding.challLayoutTab.newTab().setText("     챌린지          "));

        chall_mBinding.challLayoutTab.addTab(chall_mBinding.challLayoutTab.newTab().setText("   도전가능   "));

        chall_mBinding.challLayoutTab.addTab(chall_mBinding.challLayoutTab.newTab().setText("          도전완료     "));

        chall_mContentPagerAdapter = new ChallengeAdapter(
                getActivity().getSupportFragmentManager(), chall_mBinding.challLayoutTab.getTabCount());
        chall_mBinding.challPagerContent.setAdapter(chall_mContentPagerAdapter);
        chall_mBinding.challPagerContent.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(chall_mBinding.challLayoutTab));
        chall_mBinding.challLayoutTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chall_mBinding.challPagerContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
