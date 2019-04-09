package com.el.ariby.ui.main.menu.club;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

public class CustomClub extends LinearLayout {
    ImageView imgNickMain; // 클럽 대표사진
    TextView txtTitle; // 클럽명
    TextView txtNickname; // 닉네임
    TextView txtNumber; // 인원수
    TextView txtMap; // 위치명

    public CustomClub(Context context) {
        super(context);
        init(context);
    }

    public CustomClub(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setImgNickMain(String url) {
        Glide.with(this).load(url).into(imgNickMain);
    }

    public void setTxtTitle(String title) {
        txtTitle.setText(title);
    }

    public void setTxtNickname(String nickName) {
        txtNickname.setText(nickName);
    }

    public void setTxtNumber(String num) {
        txtNumber.setText(num);
    }

    public void setTxtMap(String mapName) {
        txtMap.setText(mapName);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_club, this, true);
        imgNickMain = findViewById(R.id.img_main);
        txtTitle = findViewById(R.id.txt_title);
        txtNickname = findViewById(R.id.txt_nickname);
        txtNumber = findViewById(R.id.txt_number);
        txtMap = findViewById(R.id.txt_map);
    }

}
