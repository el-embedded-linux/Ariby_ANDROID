package com.el.ariby.ui.main.menu.follow;

import java.util.ArrayList;

public class FollowItem{
    private String iconDrawable;
    private String nick;
    private String follwingNum, followerNum;
    private String uid;

    public void setUid(String uid) { this.uid = uid; }

    public void setIconDrawable(String iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setFollwingNum(String follwingNum) {
        this.follwingNum = follwingNum;
    }

    public void setFollowerNum(String followerNum) {
        this.followerNum = followerNum;
    }

    public FollowItem(String iconDrawable, String nick, String follwingNum, String followerNum, String uid){
        this.iconDrawable=iconDrawable;
        this.nick=nick;
        this.follwingNum = follwingNum;
        this.followerNum = followerNum;
        this.uid=uid;
    }

    public FollowItem(String nick){
        this.nick=nick;
    }

    public String getUid() {
        return uid;
    }

    public String getIconDrawable() {
        return iconDrawable;
    }

    public String getNick() {
        return nick;
    }

    public String getFollwingNum() {
        return follwingNum;
    }

    public String getFollowerNum() {
        return followerNum;
    }
}
