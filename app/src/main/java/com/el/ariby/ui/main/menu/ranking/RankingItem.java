package com.el.ariby.ui.main.menu.ranking;

import android.util.Log;

public class RankingItem {
    public String getImgProfile() { return imgProfile; }

    public String getNickname() { return nickname; }

    public String getRidingDis() { return ridingDis; }

    public String getRidingTime() { return ridingTime; }

    public String getRank() { return rank; }


    public RankingItem(String imgProfile, String nickname, String ridingDis, String ridingTime, String rank) {
        this.imgProfile = imgProfile;
        this.nickname = nickname;
        this.ridingDis = ridingDis;
        Log.e("RankItem_Distance : ",this.ridingDis);
        this.ridingTime = ridingTime;
        this.rank = rank;
    }

    public RankingItem(String nickname){
        this.nickname = nickname;
    }

    String imgProfile; //랭킹 프로필 사진
    String nickname; //닉네임
    String ridingDis; //주행거리
    String ridingTime; //주행시간
    String rank; //등수

}
