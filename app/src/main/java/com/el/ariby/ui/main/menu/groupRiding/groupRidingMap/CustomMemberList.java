package com.el.ariby.ui.main.menu.groupRiding.groupRidingMap;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

public class CustomMemberList extends LinearLayout {
    TextView nickname;
    ImageView profile;

    public CustomMemberList(Context context) {
        super(context);
        init(context);
    }
    public CustomMemberList(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }
    public void setNickname(String nickname) {
        this.nickname.setText(nickname);
    }

    public void setProfile(String url) {
        Glide.with(this).load(url).into(profile);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_member_list, this, true);
        nickname =  findViewById(R.id.member_nick1);
        profile = findViewById(R.id.member_Prof1);
    }

}
