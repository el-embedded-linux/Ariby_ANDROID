package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.el.ariby.MainActivity;
import com.el.ariby.R;

public class GroupRideActivity extends AppCompatActivity {
    Button createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        createGroup = findViewById(R.id.btn_createG);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GroupRideActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

    }
}
