package com.el.ariby.ui.main.menu.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.el.ariby.R;

public class MapInputActivity extends AppCompatActivity {
    public static final int CODE_MAP_START_SEARCH = 3000;           // 출발지 인텐트 코드
    public static final int CODE_MAP_END_SEARCH = 4000;             // 도착지 인텐트 코드
    public static final int CODE_MAP_CURRENT_SEARCH = 5000;         // 도착지 인텐트 코드

    EditText edtStart;
    EditText edtEnd;
    Button btnFind;
    String startX;
    String startY;
    String endX;
    String endY;
    int result=0;

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
                startActivityForResult(intent, CODE_MAP_START_SEARCH);
            }
        });
        edtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapSearchActivity.class);
                startActivityForResult(intent, CODE_MAP_END_SEARCH);
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(edtStart.getText()))
                        && !(TextUtils.isEmpty(edtEnd.getText()))) {
                    String txtStart= edtStart.getText().toString();
                    if(!txtStart.equals(edtEnd.getText().toString())) {
                        Intent intent = new Intent(getApplicationContext(), MapFindLocationActivity.class);
                        intent.putExtra("startX", startX);
                        intent.putExtra("startY", startY);
                        intent.putExtra("endX", endX);
                        intent.putExtra("endY", endY);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "출발지와 도착지는 같을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getApplicationContext(),
                            "출발지 또는 도착지를 설정 하셔야 합니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CODE_MAP_START_SEARCH:                                 // 출발지 결과
                if (resultCode == RESULT_OK) {                          // 출발지를 누르고 리스트를 선택 시
                    setStartCoords(data);
                    edtStart.setText(data.getStringExtra("result_msg"));
                } else if (resultCode == CODE_MAP_CURRENT_SEARCH) {     // 출발지 누르고 현 위치를 선택 시
                    setStartCoords(data);
                    edtStart.setText("현위치");
                }
                break;
            case CODE_MAP_END_SEARCH:                                   // 도착지 결과
                if (resultCode == RESULT_OK) {                          // 도착지를 누르고 리스트를 선택 시
                    setEndCoords(data);
                    edtEnd.setText(data.getStringExtra("result_msg"));
                } else if (resultCode == CODE_MAP_CURRENT_SEARCH) {     // 도착지 누르고 현 위치를 선택 시
                    setEndCoords(data);
                    edtEnd.setText("현위치");
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!TextUtils.isEmpty(edtStart.getText().toString()) &&
            !TextUtils.isEmpty(edtEnd.getText().toString())) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(edtStart.getText().toString()) &&
                !TextUtils.isEmpty(edtEnd.getText().toString())) {
            result = 1;
        }
    }

    /**
     * 출발지 좌표 저장
     * @param data intent
     */
    private void setStartCoords(@Nullable Intent data) {
        startX = data.getStringExtra("X");
        startY = data.getStringExtra("Y");
        Log.d("startX", startX);
        Log.d("startY", startY);
    }

    /**
     * 도착지 좌표 저장
     * @param data intent
     */
    private void setEndCoords(@Nullable Intent data) {
        endX = data.getStringExtra("X");
        endY = data.getStringExtra("Y");
        Log.d("startX", endX);
        Log.d("startY", endY);
    }

    public void setPreference(String key, String value) {
        SharedPreferences pref = getSharedPreferences("com.el.ariby_mapInput", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("com.el.ariby_mapInput", MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
