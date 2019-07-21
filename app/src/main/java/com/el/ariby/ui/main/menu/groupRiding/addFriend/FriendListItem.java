package com.el.ariby.ui.main.menu.groupRiding.addFriend;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class FriendListItem implements Serializable {
    public String getFriend_nick() {
        return friend_nick;
    }


    public String getProfile() {
        return profile;
    }

   public String getfUid() {return fUid;}

    public FriendListItem(String friend_nick, String profile, String fUid) {
        this.friend_nick = friend_nick;
        this.profile = profile;
        this.fUid = fUid;
    }


    @Override
    public String toString() {
        return "FriendListItem{" +
                "friend_nick='" + friend_nick + '\'' +
                ", profile='" + profile + '\'' +
                ", fUid='" + fUid + '\'' +
                '}';
    }

    String friend_nick;
    String profile;
    String fUid;

}
