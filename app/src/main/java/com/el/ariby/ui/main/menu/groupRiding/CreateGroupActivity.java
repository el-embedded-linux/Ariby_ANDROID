package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {
    Button makeGroup;
    Button addFriend;
    EditText groupName;
    EditText inputStart;
    EditText inputEnd;
    EditText inputInfo;

    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userRef;

    String startX;
    String startY;

    String endX;
    String endY;

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

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UploadGroupInfoToFirebase();
                final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                userRef = database.getReference("USER").child(mUser.getUid());

                SharedPreferences getList = getSharedPreferences("com.el.ariby_preferences", MODE_PRIVATE);
                int count = getList.getInt("count", 0);
                String str = getList.getString("members", "none");
                Log.d("members : ", str);
                Log.d("count : ", String.valueOf(count));
                final String group_name = groupName.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();
                String note = inputInfo.getText().toString();

                final String userInfo[] = new String[3];


                String[] array = str.split("\\*");
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
                        //멤버 정보 (리더, 멤버)
                        Log.d("userRef", "userRef works");

                        userInfo[0]= mUser.getUid();
                        userInfo[1]= dataSnapshot.child("nickname").getValue().toString();
                        userInfo[2] = dataSnapshot.child("userImageURL").getValue().toString();

                        Log.d("userRef str : ", String.valueOf(userInfo));

                        ref.child("GROUP_RIDING").child(group_name).child("leader_nick").setValue(userInfo[1]);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("uid").setValue(userInfo[0]);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("nickname").setValue(userInfo[1]);
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("state").setValue("leader");
                        ref.child("GROUP_RIDING").child(group_name).child("members").child("0").child("profile").setValue(userInfo[2]);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





                int a = 1;
                //TODO. lat, lon 자동완성 지우기
                double lon = 127.0442899;
                double lat = 37.6675547;

                for (int i = 0; i < count+6 ; i=i+3)
                {
                    Log.e("print", array[i]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("uid").setValue(array[i+1]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("nickname").setValue(array[i]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("state").setValue("true");
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("profile").setValue(array[i+2]);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("lat").setValue(lat);
                    ref.child("GROUP_RIDING").child(group_name).child("members").child(String.valueOf(a)).child("lon").setValue(lon);
                    lat = lat+0.1;
                    lon = lon+0.1;
                    a++;
                }

                Toast.makeText(CreateGroupActivity.this, "그룹이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(CreateGroupActivity.this, GroupRideActivity.class);
                startActivity(intent);
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
            case 5:
                if(resultCode == RESULT_OK)
                {
                    String startPoint = data.getStringExtra("result_msg");
                    startX = data.getStringExtra("X");
                    startY = data.getStringExtra("Y");
                    inputStart.setText(startPoint);
                }
                break;
            case 6:
                if(resultCode == RESULT_OK)
                {
                    String endPoint = data.getStringExtra("result_msg");
                    endX = data.getStringExtra("X");
                    endY = data.getStringExtra("Y");
                    inputEnd.setText(endPoint);
                }
        }
    }

    public void UploadGroupInfoToFirebase() {

    }
}
