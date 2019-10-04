package com.el.ariby.ui.join;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityHeightBinding;

public class HeightActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    ActivityHeightBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 풀스크린으로 세팅(상단바, 메뉴바 없어짐)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_height);

        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
            }
        });
        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(mBinding.etHeight.getText().toString()))) { //빈칸이 아니면
                    setPreference("tall", Integer.parseInt(mBinding.etHeight.getText().toString()));
                }else if(TextUtils.isEmpty(mBinding.etHeight.getText().toString())){ //빈칸이면
                    mBinding.etHeight.setError("키를 입력해 주세요.");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), WeightActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.not_move_activity);
            }
        });

    }

    public void setPreference(String key, int value) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
