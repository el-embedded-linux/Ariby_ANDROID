package com.el.ariby.ui.join;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.el.ariby.MainActivity;
import com.el.ariby.R;
import com.el.ariby.databinding.ActivityJoiningBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoiningActivity extends AppCompatActivity {
    public final String PREFERENCE = "com.el.ariby_joining";
    FirebaseAuth firebaseAuth;
    ActivityJoiningBinding mBinding;
    int weight = 0;
    boolean gender = true;
    String age = "";
    int tall = 0;
    String nickName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_joining);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        mBinding.btnJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mBinding.etEmail.getText().toString().trim();
                final String password = mBinding.etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) { // 이메일 공백 체크
                    Toast.makeText(getApplicationContext(), "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) { // 패스워드 공백 체크
                    Toast.makeText(getApplicationContext(), "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)//파이어베이스 계정생성 함수
                        .addOnCompleteListener(JoiningActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference();

                                    if (email.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                    if (password.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                    try {
                                        weight = getPreferenceInt("weight"); // 저장된 값 불러오기 (몸무게)
                                        gender = getPreferenceBoolean("gender"); // 성별
                                        age = getPreferenceString("age"); // 나이
                                        tall = getPreferenceInt("tall"); // 키
                                        nickName = getPreferenceString("displayName");
                                    } catch (Exception e) { // 사용자가 정보(키,몸무게 등)를 입력하지 않았을 경우, 변수 값에 DEFAULT 값 설정해 두었기 때문에 비워둠

                                    }


                                    UserModel userInfo = new UserModel(weight, age, tall, gender, nickName);
                                    myRef.child("user").child(firebaseAuth.getUid()).setValue(userInfo);
                                    Intent intent = new Intent(JoiningActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    //Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(nickName).setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/displayImage.png?alt=media&token=210079dc-d9af-43b4-a364-d7b5deb47a05")).build();

                                user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });


            }
        });
    }

    public boolean getPreferenceBoolean(String key) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public int getPreferenceInt(String key) {
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        return pref.getInt(key, 0);
    }
}

class UserModel {
    String age, nickname;
    Boolean gender;
    int weight, tall;

    public UserModel(int weight, String age, int tall, Boolean gender, String nickname) {
        this.weight = weight;
        this.age = age;
        this.tall = tall;
        this.gender = gender;
        this.nickname = nickname;
    }
}