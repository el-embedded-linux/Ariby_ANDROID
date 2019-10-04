package com.el.ariby;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.el.ariby.databinding.ActivityMainBinding;
import com.el.ariby.ui.main.InfoFragment;
import com.el.ariby.ui.main.MenuFragment;
import com.el.ariby.ui.main.MapActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewPager mainViewPager = new MainViewPager(getSupportFragmentManager());
        mBinding.vpMain.setAdapter(mainViewPager);

        // 해시키를 저장
        String hash = getKeyHash(this);
        Log.e("MainActivity", "hashKey : " + hash);

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
                                mBinding.vpMain.setCurrentItem(1);
                                break;
                            }
                        }
                        return true;
                    }
                });
        mBinding.floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if ( pressedTime == 0 ) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = 0 ;
            }
            else {
                super.onBackPressed();
//                finish(); // app 종료 시키기
            }
        }
    }

    /**
     * 카카오 api 사용시  저장해야 하는 해시키를 가져온다
     * @param context context
     * @return hash key
     */
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
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
        private MenuFragment menuFragment = new MenuFragment();
        private InfoFragment infoFragment = new InfoFragment();

        public MainViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return menuFragment;
                default:
                    return infoFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
