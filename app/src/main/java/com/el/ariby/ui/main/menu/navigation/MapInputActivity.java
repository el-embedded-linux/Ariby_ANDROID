package com.el.ariby.ui.main.menu.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.el.ariby.R;

public class MapInputActivity extends AppCompatActivity {
    EditText edtStart;
    EditText edtEnd;
    Button btnFind;
    String startX;
    String startY;
    String endX;
    String endY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_input);
        edtStart = findViewById(R.id.et_start);
        edtEnd = findViewById(R.id.et_end);
        btnFind = findViewById(R.id.btn_find);
        edtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapSearchActivity.class);
                startActivityForResult(intent, 3000);
            }
        });
        edtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapSearchActivity.class);
                startActivityForResult(intent, 4000);
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(edtStart.getText()))
                        && !(TextUtils.isEmpty(edtEnd.getText()))) {
                    Intent intent = new Intent(getApplicationContext(), MapFindLocationActivity.class);
                    intent.putExtra("startX", startX);
                    intent.putExtra("startY", startY);
                    intent.putExtra("endX", endX);
                    intent.putExtra("endY", endY);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(),
                            "출발지 또는 도착지를 설정 하셔야 합니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3000 && resultCode == 3000) { // 출발지를 누르고 리스트를 선택 시
            String a = data.getStringExtra("result_msg");
            startX = data.getStringExtra("X");
            startY = data.getStringExtra("Y");
            edtStart.setText(a);
        } else if (requestCode == 4000 && resultCode == 3000) { // 도착지를 누르고 리스트를 선택 시
            String a = data.getStringExtra("result_msg");
            endX = data.getStringExtra("X");
            endY = data.getStringExtra("Y");
            edtEnd.setText(a);
        } else if (resultCode == 5000) { // 출발지 또는 도착지를 누르고 현 위치를 선택 시

            if (requestCode == 3000) {
                startX = data.getStringExtra("X");
                startY = data.getStringExtra("Y");
                Log.d("startX", startX);
                Log.d("startY", startY);
                edtStart.setText("현위치");
            } else {
                endX = data.getStringExtra("X");
                endY = data.getStringExtra("Y");
                edtEnd.setText("현위치");
            }
        }
    }
}
