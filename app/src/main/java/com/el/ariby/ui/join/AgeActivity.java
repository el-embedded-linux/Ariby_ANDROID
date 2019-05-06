package com.el.ariby.ui.join;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityAgeBinding;

import java.util.Calendar;

public class AgeActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";
    public final String PREFERENCE = "com.el.ariby_joining";
    ActivityAgeBinding mBinding;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date, preference_date="1995-01-01";//입력하지 않았을 경우 생년월일 기본값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 풀스크린으로 세팅(상단바, 메뉴바 없어짐)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_age);

        mBinding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AgeActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                date = year + " / " + month + " / " + day;
                preference_date = year + "-" + month + "-" + day;
                mBinding.tvDate.setText(date);
            }
        };
        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreference("birth", preference_date);
                Intent intent = new Intent(getApplicationContext(), HeightActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_in_right);
            }
        });

        mBinding.btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HeightActivity.class);
                startActivity(intent);
            }
        });

        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity);
            }
        });

    }

    public void setPreference(String key, String value) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}

