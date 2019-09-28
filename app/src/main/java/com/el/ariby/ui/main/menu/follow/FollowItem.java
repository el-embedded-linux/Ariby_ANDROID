package com.el.ariby.ui.main.menu.follow;

import java.util.ArrayList;

public class FollowItem extends ArrayList<String> {
    private String iconDrawable;
    private String nick;
    private String follwingNum, followerNum;

    public FollowItem(String nick){
        this.nick=nick;
    }

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

    public FollowItem(String iconDrawable, String nick, String follwingNum, String followerNum){
        this.iconDrawable=iconDrawable;
        this.nick=nick;
        this.follwingNum = follwingNum;
        this.followerNum = followerNum;
    }

    public FollowItem(String iconDrawable, String nick){
        this.iconDrawable=iconDrawable;
        this.nick=nick;
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
