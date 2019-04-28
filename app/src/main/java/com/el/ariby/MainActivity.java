package com.el.ariby;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.el.ariby.databinding.ActivityMainBinding;
import com.el.ariby.ui.main.HomeFragment;
import com.el.ariby.ui.main.InfoFragment;
import com.el.ariby.ui.main.MenuFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewPager mainViewPager = new MainViewPager(getSupportFragmentManager());
        mBinding.vpMain.setAdapter(mainViewPager);

        mBinding.customBottomBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home: {
                                mBinding.vpMain.setCurrentItem(0);
                                break;
                            }
                            case R.id.navigation_info: {
                                mBinding.vpMain.setCurrentItem(2);
                                break;
                            }
                        }
                        return true;
                    }
                });
        mBinding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.vpMain.setCurrentItem(1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 메인 뷰 페이저
     * 홈, 메뉴, 인포
     */
    class MainViewPager extends FragmentStatePagerAdapter {
        private HomeFragment homeFragment = new HomeFragment();
        private MenuFragment menuFragment = new MenuFragment();
        private InfoFragment infoFragment = new InfoFragment();

        public MainViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return homeFragment;
                case 1:
                    return menuFragment;
                default:
                    return infoFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
