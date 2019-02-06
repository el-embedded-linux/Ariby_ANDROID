package com.el.ariby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.security.acl.Group;

public class GenderActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    Button back,next,jump;
    RadioGroup gendergrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        gendergrp=findViewById(R.id.gendergrp);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        jump = findViewById(R.id.jump);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.not_move_activity,R.anim.rightout_activity);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreference("gender",gendergrp.isSelected());
                Intent intent= new Intent(getApplicationContext(), JoiningActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);

            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), JoiningActivity.class);
                startActivity(intent);

            }
        });
    }
    public void setPreference(String key, boolean value){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}
