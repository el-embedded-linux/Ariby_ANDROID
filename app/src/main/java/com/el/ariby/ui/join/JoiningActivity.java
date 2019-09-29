package com.el.ariby.ui.join;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.el.ariby.MainActivity;
import com.el.ariby.R;
import com.el.ariby.databinding.ActivityJoiningBinding;
import com.el.ariby.ui.login.LoginActivity;
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
    String gender = "M";
    String birth = "";
    int height = 0;
    String nickName = "";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 풀스크린으로 세팅(상단바, 메뉴바 없어짐)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_joining);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        final SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);

        mBinding.btnJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(JoiningActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("가입 진행 중 입니다.");
                final String email = mBinding.etEmail.getText().toString().trim();
                final String password = mBinding.etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) { // 이메일 공백 체크
                    Toast.makeText(getApplicationContext(), "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) { // 패스워드 공백 체크
                    Toast.makeText(getApplicationContext(), "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email, password)//파이어베이스 계정생성 함수
                        .addOnCompleteListener(JoiningActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference();

                                    if (email.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "이메일를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                    if (password.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }


                                        weight = pref.getInt("weight",0);
                                    //getPreferenceInt("weight"); // 저장된 값 불러오기 (몸무게)
                                        gender = pref.getString("gender","");
                                        //gender = getPreferenceString("gender"); // 성별
                                        birth = pref.getString("birth", "");
                                        height = pref.getInt("tall", 0);
                                        nickName = pref.getString("displayName","");

                                        /*birth = getPreferenceString("birth"); // 나이
                                        height = getPreferenceInt("tall"); // 키
                                        nickName = getPreferenceString("displayName");*/
                                        Log.d("info : ", String.valueOf(weight)+", "+height+", "+nickName);

                                    UserModel userInfo = new UserModel(weight, birth, height, gender, nickName);
                                    myRef.child("USER").child(firebaseAuth.getUid()).setValue(userInfo);
                                    myRef.child("FRIEND").child("following").child(firebaseAuth.getUid()).setValue("false");
                                    myRef.child("FRIEND").child("follower").child(firebaseAuth.getUid()).setValue("false");
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
                                    firebaseAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(JoiningActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent intent = new Intent(JoiningActivity.this, MainActivity.class);
                                                                progressDialog.dismiss();
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                            } else {
                                                                Toast.makeText(JoiningActivity.this, "아이디,비밀번호가 틀리거나 없는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                            );
                                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "이미 있는 아이디거나 생성 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    return;
                                }
                            }
                        });
            }
        });
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
    public String birth, nickname;
    public String gender, userImageURL;
    public int weight, height;
    public int level, exp;
    public int followNum, followerNum;
    public UserModel(int weight, String birth, int height, String gender, String nickname) {
        this.weight = weight;
        this.birth = birth;
        this.height = height;
        this.gender = gender;
        this.nickname = nickname;
        this.level = 1;
        this.exp = 0;
        this.userImageURL = "https://firebasestorage.googleapis.com/v0/b/elandroid.appspot.com/o/displayImage.png?alt=media&token=210079dc-d9af-43b4-a364-d7b5deb47a05";
        this.followNum = 0;
        this.followerNum= 0;
    }
}