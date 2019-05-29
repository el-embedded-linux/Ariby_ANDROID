package com.el.ariby.ui.main.menu.follow;

public class FollowItem {
    private String iconDrawable;
    private String nick;
    private String descStr;
    private String follwingNum, followerNum;



    public FollowItem(String nick){
        this.nick=nick;

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

    public String getDescStr() {
        return descStr;
    }

    public String getFollwingNum() {
        return follwingNum;
    }

    public String getFollowerNum() {
        return followerNum;
    }
}
