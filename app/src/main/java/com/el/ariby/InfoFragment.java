package com.el.ariby;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class InfoFragment extends Fragment {

    TextView displayName;
    ImageView photo;
    Button profile_more;
    Button add_friend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container,false);
        displayName = v.findViewById(R.id.fragment_info_profile_displayName);
        photo = v.findViewById(R.id.fragment_info_profile_photo);
        profile_more = v.findViewById(R.id.fragment_info_profile_more);
        add_friend = v.findViewById(R.id.fragment_info_profile_friend);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            Uri uri = user.getPhotoUrl();
            Glide.with(this).load(uri).apply(RequestOptions.circleCropTransform()).into(photo); //이미지를 둥글게 처리
            displayName.setText(name);
            photo.setImageURI(uri);
        }

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 이미지 파이어베이스에 업로드
                Toast.makeText(getContext(),"클릭했습니다.",Toast.LENGTH_LONG).show();
            }
        });

        profile_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 더보기 이벤트
                Toast.makeText(getContext(),"클릭했습니다.",Toast.LENGTH_LONG).show();
            }
        });

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 친구찾기 이벤트
                Toast.makeText(getContext(),"클릭했습니다.",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
