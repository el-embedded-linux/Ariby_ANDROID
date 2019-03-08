package com.el.ariby.ui.join;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityGenderBinding;

public class GenderActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    ActivityGenderBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_gender);

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
                setPreference("gender", mBinding.grpGender.isSelected());
                Intent intent = new Intent(getApplicationContext(), NicknameActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_in_right);

            }
        });
        mBinding.btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NicknameActivity.class);
                startActivity(intent);

            }
        });
    }

    public void setPreference(String key, boolean value) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
