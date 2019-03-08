package com.el.ariby;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.el.ariby.databinding.ActivityMainBinding;
import com.el.ariby.ui.main.HomeFragment;
import com.el.ariby.ui.main.InfoFragment;
import com.el.ariby.ui.main.MenuFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private MenuFragment menuFragment = new MenuFragment();
    private InfoFragment infoFragment = new InfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();

        mBinding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_menu: {
                        transaction.replace(R.id.frame_layout, menuFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_info: {
                        transaction.replace(R.id.frame_layout, infoFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
