package com.el.ariby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tall);

        Button back,next,jump;
        EditText cm;

        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        jump = findViewById(R.id.jump);
        cm = findViewById(R.id.cm);

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
                Intent intent= new Intent(getApplicationContext(),WeightActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),WeightActivity.class);
                startActivity(intent);
            }
        });

    }
}
