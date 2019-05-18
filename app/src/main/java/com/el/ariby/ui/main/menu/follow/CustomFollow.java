package com.el.ariby.ui.main.menu.follow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

public class CustomFollow extends LinearLayout {
    ImageView imgProfile;
    TextView Nickname;
    String descStr;
    String follwingNum, followerNum;

    public CustomFollow(Context context) {
        super(context);
        init(context);
    }

    public CustomFollow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setImgProfile(String url) {
        Glide.with(this).load(url).into(imgProfile);
    }

    public void setNickname(String nickname) {
        Nickname.setText(nickname);
    }

    public void setDescStr(String descStr) {
        this.descStr = descStr;
    }

    public void setFollwingNum(String follwingNum) {
        this.follwingNum = follwingNum;
    }

    public void setFollowerNum(String followerNum) {
        this.followerNum = followerNum;
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_user_list,this,true);
        imgProfile = findViewById(R.id.imageView1);
        Nickname = findViewById(R.id.textView1);
    }
}
