package com.el.ariby.ui.main.menu.groupRiding.groupRidingMap;

import com.bumptech.glide.Glide;

public class MemberListItem {
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public MemberListItem(String profile, String nickname) {
        this.profile = profile;
        this.nickname = nickname;
    }

    String profile;
    String nickname;

}
