package com.el.ariby.ui.main.menu.club;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.el.ariby.R;

public class CustomMemberView extends LinearLayout {
    ImageView imgProfile;
    TextView txtTitle;

    public CustomMemberView(Context context) {
        super(context);
        init(context);
    }

    public CustomMemberView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    private void init(Context view) {
        LayoutInflater inflater = (LayoutInflater)view.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_club_member,this,true);

        imgProfile = findViewById(R.id.img_mem_profile);
        txtTitle = findViewById(R.id.txt_mem_nickname);
    }

    public void setTxtTitle(String title) {
        txtTitle.setText(title);
    }

    public void setImgProfile(String image) {
        Glide.with(this)
                .load(image)
                .centerCrop()
                .into(this.imgProfile);
    }
}
