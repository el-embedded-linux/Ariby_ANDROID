package com.el.ariby.ui.main.menu.groupRiding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.navigation.MapSearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends AppCompatActivity {
    Button makeGroup;
    Button addFriend;
    EditText groupName;
    EditText inputStart;
    EditText inputEnd;

    String start_lat, start_lon;
    String end_lat, end_lon;
    String leader_nick;
    String member_nick;
    String uid;
    FirebaseDatabase database;
    DatabaseReference ref;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        makeGroup = findViewById(R.id.btn_makeGroup);
        addFriend = findViewById(R.id.btn_add_rider);
        groupName = findViewById(R.id.input_groupName);
        inputEnd = findViewById(R.id.input_end);
        inputStart = findViewById(R.id.input_start);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadGroupInfoToFirebase();
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
                    inputStart.setText(startPoint);
                }
                break;
            case 6:
                if(resultCode == RESULT_OK)
                {
                    String endPoint = data.getStringExtra("result_msg");
                    inputEnd.setText(endPoint);
                }
        }
    }

    public void UploadGroupInfoToFirebase(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = database.getReference("GROUP_RIDING").child(mUser.getUid());
        final String group_name = groupName.getText().toString();
        String start = inputStart.getText().toString();
        String end = inputEnd.getText().toString();
        ref.child("GROUP_RIDING").child(group_name).child("endPoint").child("lat").setValue(end_lat);

    }

}
