package com.el.ariby.ui.join;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityNicknameBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NicknameActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    ActivityNicknameBinding mBinding;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 풀스크린으로 세팅(상단바, 메뉴바 없어짐)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nickname);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("USER");

        mBinding.etNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {//무언가 바뀐 시점 전에

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {//무언가 바뀐 시점
                mBinding.txtAlram.setText("생성 가능합니다.");
                mBinding.btnNext.setEnabled(true);
                mBinding.btnNext.setBackgroundColor(Color.parseColor("#1E90FF"));
            }

            @Override
            public void afterTextChanged(Editable s) {//무언가 바뀐 이후
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String useNick = mBinding.etNickname.getText().toString();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if((useNick.equals(snapshot.child("nickname").getValue().toString())) ||
                                    TextUtils.isEmpty(useNick)) {
                                mBinding.txtAlram.setText("이미 사용중인 닉네임입니다.");
                                mBinding.btnNext.setEnabled(false);
                                mBinding.btnNext.setBackgroundColor(Color.parseColor("#FF979797"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
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
                if (!(TextUtils.isEmpty(mBinding.etNickname.getText().toString())))
                    setPreference("displayName", mBinding.etNickname.getText().toString());

                Intent intent = new Intent(getApplicationContext(), JoiningActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left, R.anim.anim_slide_in_right);
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
