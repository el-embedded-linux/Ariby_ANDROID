package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.navigation.MapSearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateGroupActivity extends AppCompatActivity {
    public static final int CODE_MAP_CURRENT_SEARCH = 5000;
    Button makeGroup;
    Button addFriend;
    Button checkName;
    EditText groupName;
    EditText inputStart;
    EditText inputEnd;
    EditText inputInfo;

    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userRef;
    DatabaseReference memberRef;

    String startX;
    String startY;
    String endX;
    String endY;
    int duplicateCheck = 2; //체크 안함


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        makeGroup = findViewById(R.id.btn_makeGroup);
        addFriend = findViewById(R.id.btn_add_rider);
        groupName = findViewById(R.id.input_groupName);
        inputEnd = findViewById(R.id.input_end);
        inputStart = findViewById(R.id.input_start);
        inputInfo = findViewById(R.id.input_info);
        checkName = findViewById(R.id.btnCheck);

        database = FirebaseDatabase.getInstance();
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = database.getReference("USER").child(mUser.getUid());
        ref = database.getReference();
        memberRef = database.getReference("GROUP_RIDING_MEMBERS");

        final double lon = 127.0442899;
        final double lat = 37.6675547;

        checkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = groupName.getText().toString();
                duplicateCheck = 0; //중복없음
                Log.d("for문 안에서 체크1 : ", String.valueOf(duplicateCheck));
                    ref.child("GROUP_RIDING").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("snapshot : ", snapshot.getKey());
                                if (duplicateCheck==3) {
                                    break;
                                }
                                    if (name.equals(snapshot.getKey())) {
                                        makeGroup.setEnabled(false);
                                        makeGroup.setBackgroundColor(Color.parseColor("#FF979797"));
                                        duplicateCheck = 1; //중복 발견
                                        Log.d("for문 안에서 체크2 : ", String.valueOf(duplicateCheck));
                                        Toast.makeText(CreateGroupActivity.this, "이미 존재하는 그룹 이름입니다.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                            }
                            if (duplicateCheck == 0) { //중복 없음
                                Toast.makeText(CreateGroupActivity.this, "사용 가능한 그룹 이름입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }
        });

        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences getList = getSharedPreferences("com.el.ariby_preferences", MODE_PRIVATE);
                int count = getList.getInt("count", 0);
                String str = getList.getString("members", "none");
                Log.d("members : ", str);
                Log.d("count : ", String.valueOf(count));
                final String group_name = groupName.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();
                String note = inputInfo.getText().toString();

                if(TextUtils.isEmpty(group_name)){
                    groupName.setError("그룹 이름을 입력해 주세요");
                    return;
                }else if(TextUtils.isEmpty(start)){
                    inputStart.setError("출발지를 지정해 주세요");
                    return;
                }else if(TextUtils.isEmpty(end)){
                    inputEnd.setError("도착지를 지정해 주세요");
                    return;
                }
                Log.d("duplicateCheck : ", String.valueOf(duplicateCheck));
                if(duplicateCheck==1) {
                    Toast.makeText(CreateGroupActivity.this, "이미 존재하는 그룹입니다", Toast.LENGTH_SHORT).show();
                    return;
                }else if(duplicateCheck == 2) {
                    Toast.makeText(CreateGroupActivity.this, "그룹 이름 중복체크를 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String userInfo[] = new String[3];

                final String[] array = str.split("\\*");
                //출발지
                ref.child("GROUP_RIDING").child(group_name).child("startPoint").child("name").setValue(start);
                ref.child("GROUP_RIDING").child(group_name).child("startPoint").child("lat").setValue(startY);
                ref.child("GROUP_RIDING").child(group_name).child("startPoint").child("lon").setValue(startX);

                //도착지
                ref.child("GROUP_RIDING").child(group_name).child("endPoint").child("name").setValue(end);
                ref.child("GROUP_RIDING").child(group_name).child("endPoint").child("lat").setValue(endY);
                ref.child("GROUP_RIDING").child(group_name).child("endPoint").child("lon").setValue(endX);

                //info
                ref.child("GROUP_RIDING").child(group_name).child("info").child("note").setValue(note);

                //생성자 닉네임

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final long[] group_num1 = new long[1];
                        //멤버 정보 (리더, 멤버)
                        Log.d("userRef", "userRef works");

                        userInfo[0]= mUser.getUid();
                        userInfo[1]= dataSnapshot.child("nickname").getValue().toString();
                        userInfo[2] = dataSnapshot.child("userImageURL").getValue().toString();

                        Log.d("userRef str : ", String.valueOf(userInfo));

                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("uid").setValue(userInfo[0]);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("nickname").setValue(userInfo[1]);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("state").setValue("leader");
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("profile").setValue(userInfo[2]);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("lat").setValue(lat);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("lon").setValue(lon);
                        ref.child("GROUP_RIDING_MEMBERS").child(mUser.getUid()).child(group_name).setValue("true");
                        //group_riding_members
                        //long groupCount = returnGroupCount(userInfo[0]);

                        /*memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String uidStr = dataSnapshot1.getKey();
                                    Log.d("uidStr : ", uidStr);
                                    if(uidStr.equals(userInfo[0])) {
                                        group_num1[0] = dataSnapshot1.getChildrenCount();
                                        Log.e("group_num : ", userInfo[0] + ",  "+ String.valueOf(group_num1[0]));
                                        ref.child("GROUP_RIDING_MEMBERS").child(mUser.getUid()).child(String.valueOf(group_num1[0])).setValue(group_name);
                                        break;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                int a = 1;
                //TODO. lat, lon 자동완성 지우기
                double lon1 = 127.0442899;
                double lat1 = 37.6675547;
                for (int i = 0; i < count*3 ; i += 3)
                {
                    final long[] group_num2 = new long[1];
                    final int index = i;

                    Log.e("print", array[i]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("uid").setValue(array[i+1]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("nickname").setValue(array[i]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("state").setValue("true");
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("profile").setValue(array[i+2]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("lat").setValue(lat1);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("lon").setValue(lon1);
                    ref.child("GROUP_RIDING_MEMBERS").child(array[index+1]).child(group_name).setValue("true");

                    //group_riding_members
                    /*memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                String uidStr = dataSnapshot1.getKey();
                                Log.d("uidStr : ", uidStr);
                                if(uidStr.equals(array[index+1])) {
                                    group_num2[0] = dataSnapshot1.getChildrenCount();
                                    Log.d("groupCount : ", array[index+1]+",,  "+group_num2[0]);
                                    ref.child("GROUP_RIDING_MEMBERS").child(array[index+1]).child(String.valueOf(group_num2[0])).setValue(group_name);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                    lat1 = lat1+0.1;
                    lon1 = lon1+0.1;
                    a++;
                }

                Toast.makeText(CreateGroupActivity.this, "그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                duplicateCheck=3;
                finish();

            }
        });


        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

        inputStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupActivity.this, MapSearchActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        inputEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupActivity.this, MapSearchActivity.class);
                startActivityForResult(intent, 6);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 5: //출발
                if(resultCode == RESULT_OK)
                {
                    String startPoint = data.getStringExtra("result_msg");
                    startX = data.getStringExtra("X");
                    startY = data.getStringExtra("Y");
                    inputStart.setText(startPoint);
                }else
                if(resultCode == CODE_MAP_CURRENT_SEARCH){
                    startX = data.getStringExtra("X");
                    startY = data.getStringExtra("Y");
                    inputStart.setText("현위치");
                }
                break;
            case 6: //도착
                if(resultCode == RESULT_OK)
                {
                    String endPoint = data.getStringExtra("result_msg");
                    endX = data.getStringExtra("X");
                    endY = data.getStringExtra("Y");
                    inputEnd.setText(endPoint);
                }else if(resultCode == CODE_MAP_CURRENT_SEARCH){
                    startX = data.getStringExtra("X");
                    startY = data.getStringExtra("Y");
                    inputEnd.setText("현위치");
                }
        }
    }




}
