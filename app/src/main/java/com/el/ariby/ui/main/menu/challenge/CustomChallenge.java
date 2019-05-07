package com.el.ariby.ui.main.menu.challenge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

public class CustomChallenge extends LinearLayout {
    ImageView chall_img;
    TextView chall_txtComment;
    TextView chall_txtExp;
    TextView chall_txtUnit;

    public CustomChallenge(Context context) {
        super(context);
        init(context);
    }

    public CustomChallenge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setChall_img(String url) {
        Glide.with(this).load(url).into(chall_img);
    }

    public void setChall_txtComment(String comment) {
        chall_txtComment.setText(comment);
    }

    public void setChall_txtExp(String exp) {
        chall_txtExp.setText(exp);
    }

    public void setChall_txtUnit(String unit) {
        chall_txtUnit.setText(unit);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_challenge, this, true);
        chall_img = findViewById(R.id.chall_img_main);
        chall_txtComment = findViewById(R.id.chall_txt_comment);
        chall_txtExp = findViewById(R.id.chall_txt_exp);
        chall_txtUnit = findViewById(R.id.chall_txt_unit);
    }

}
