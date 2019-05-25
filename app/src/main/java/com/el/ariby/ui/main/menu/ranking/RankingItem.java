package com.el.ariby.ui.main.menu.ranking;

import android.util.Log;

public class RankingItem {
    public String getImgProfile() { return imgProfile; }

    public String getNickname() { return nickname; }

    public String getRidingDis() { return ridingDis; }

    public String getRidingTime() { return ridingTime; }

    public String getRank() { return rank; }


    public RankingItem(String imgProfile, String nickname, String ridingDis, String ridingTime, String rank, String changed, String imgChanged) {
        this.imgProfile = imgProfile;
        this.nickname = nickname;
        this.ridingDis = ridingDis;
        Log.e("RankItem_Distance : ",this.ridingDis);
        this.ridingTime = ridingTime;
       this.rank = rank;
        this.txtUpDown = changed;
        this.imgUpDown = imgChanged;

    }

    public RankingItem(String nickname){
        this.nickname = nickname;
    }


    String imgProfile; //랭킹 프로필 사진
    String nickname; //닉네임
    String ridingDis; //주행거리
    String ridingTime; //주행시간



    public String getImgUpDown() {
        return imgUpDown;
    }

    public String getTxtUpDown() {
        return txtUpDown;
    }

    /*public RankingItem(String txtUpDown, String imgUpDown) {
        this.txtUpDown = txtUpDown;
        this.imgUpDown = imgUpDown;
    }*/

    String txtUpDown;

    public void setTxtUpDown(String txtUpDown) {
        this.txtUpDown = txtUpDown;
    }

    public void setImgUpDown(String imgUpDown) {
        this.imgUpDown = imgUpDown;
    }

    String imgUpDown;
    public void setRank(String rank) {
        this.rank = rank;
    }

    String rank; //등수

}
