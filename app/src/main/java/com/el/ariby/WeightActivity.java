package com.el.ariby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WeightActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    EditText weight;
    Button back,next,jump;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        jump = findViewById(R.id.jump);
        weight=findViewById(R.id.weight);

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
                if(!(TextUtils.isEmpty(weight.getText().toString())))
                    setPreference("tall",Integer.parseInt(weight.getText().toString()));

                Intent intent = new Intent(getApplicationContext(),GenderActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),GenderActivity.class);
                startActivity(intent);

            }
        });
    }
    public void setPreference(String key, int value){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
