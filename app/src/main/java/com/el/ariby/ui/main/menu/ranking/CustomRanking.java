package com.el.ariby.ui.main.menu.ranking;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.el.ariby.R;
import com.bumptech.glide.Glide;

public class CustomRanking extends LinearLayout {
    ImageView imgRankProfile;
    TextView txtRank;
    TextView txtRankNick;
    TextView txtRidingDis;
    TextView txtRidingTime;

    public CustomRanking(Context context){
        super(context);
        init(context);
    }

    public CustomRanking(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_ranking,this,true);
        imgRankProfile = findViewById(R.id.img_rankProf);
        txtRankNick = findViewById(R.id.txt_rankNick);
        txtRank = findViewById(R.id.txt_rank);
        txtRidingTime = findViewById(R.id.txt_ridingTime);
        txtRidingDis = findViewById(R.id.txt_ridingDis);
    }

    public void setImgRankProfile(String url) {
        Glide.with(this).load(url).into(imgRankProfile);
    }

    public void setTxtRank(String txtRank) {
        String rankStr = txtRank+"등";

        if(Integer.parseInt(txtRank) == 1){
            this.txtRank.setTextColor(Color.RED);
        }
        if(Integer.parseInt(txtRank)==2){
            this.txtRank.setTextColor(Color.BLUE);
        }
        if(Integer.parseInt(txtRank)==3){
            this.txtRank.setTextColor(Color.YELLOW);
        }
        this.txtRank.setText(rankStr);

        Log.e("customRanking - Rank : ",txtRank);
    }

    public void setTxtRankNick(String txtRankNick) {
        this.txtRankNick.setText(txtRankNick);
    }

    public void setTxtRidingDis(String txtRidingDis) {
        String disStr = txtRidingDis+"km";
        this.txtRidingDis.setText(disStr); }

    public void setTxtRidingTime(String txtRidingTime) { this.txtRidingTime.setText(txtRidingTime); }

}
