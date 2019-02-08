package com.el.ariby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoiningActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    FirebaseAuth firebaseAuth;
    Button joining_btn;
    EditText joining_email, joining_pw;
    int weight=0;
    boolean gender=true;
    String age="";
    int tall=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining);
        joining_btn = findViewById(R.id.joining_button);
        joining_email = findViewById(R.id.joining_email);
        joining_pw = findViewById(R.id.joining_pw);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        joining_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = joining_email.getText().toString().trim();
                final String password = joining_pw.getText().toString().trim();

                if(TextUtils.isEmpty(email)){ // 이메일 공백 체크
                    Toast.makeText(getApplicationContext(), "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) { // 패스워드 공백 체크
                    Toast.makeText(getApplicationContext(), "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)//파이어베이스 계정생성 함수
                        .addOnCompleteListener(JoiningActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference();

                                    if(email.isEmpty()) {
                                        Toast.makeText(getApplicationContext(),"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                                    }

                                    if(password.isEmpty()) {
                                        Toast.makeText(getApplicationContext(),"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                                    }

                                    try {
                                        weight=getPreferenceInt("weight"); // 저장된 값 불러오기 (몸무게)
                                        gender=getPreferenceBoolean("gender"); // 성별
                                        age=getPreferenceString("age"); // 나이
                                        tall=getPreferenceInt("tall"); // 키
                                    } catch (Exception e) { // 사용자가 정보(키,몸무게 등)를 입력하지 않았을 경우, 변수 값에 DEFAULT 값 설정해 두었기 때문에 비워둠

                                    }


                                    UserModel userInfo = new UserModel(weight,age,tall,gender);
                                    myRef.child("user").child(firebaseAuth.getUid()).setValue(userInfo);
                                    Intent intent = new Intent(JoiningActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    //Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });
    }
    public boolean getPreferenceBoolean(String key){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }
    public String getPreferenceString(String key){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getString(key, "");
    }
    public int getPreferenceInt(String key){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getInt(key, 0);
    }
}
class UserModel {
    String age;
    Boolean gender;
    int weight,tall;

    public UserModel(int weight, String age, int tall, Boolean gender) {
        this.weight=weight;
        this.age=age;
        this.tall=tall;
        this.gender=gender;
    }
}