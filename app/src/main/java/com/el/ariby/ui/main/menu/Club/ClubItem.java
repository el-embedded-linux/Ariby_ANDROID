package com.el.ariby.ui.main.menu.Club;

public class ClubItem {
    int mainLogo; // 클럽 대표사진
    String title; // 클럽명
    String nick; // 닉네임
    long number; // 인원수
    String map; // 위치명

    public ClubItem(String title) {
        this.title = title;
    }

    public ClubItem(String title, long num) {
        this.title = title;
        this.number = num;
    }
    public ClubItem(String title, long num, String location) {
        this.title = title;
        this.number = num;
        this.map=location;
    }
    public ClubItem(String nick, String title, long num, String location) {
        this.nick = nick;
        this.title = title;
        this.number = num;
        this.map=location;
    }

    public ClubItem(int mainLogo, String title, String nick, long number, String map) {
        this.mainLogo = mainLogo;
        this.title = title;
        this.nick = nick;
        this.number = number;
        this.map = map;
    }

    public int getMainLogo() {
        return mainLogo;
    }

    public String getTitle() {
        return title;
    }

    public String getNick() {
        return nick;
    }

    public long getNumber() {
        return number;
    }

    public String getMap() {
        return map;
    }

    public void setMainLogo(int mainLogo) {
        this.mainLogo = mainLogo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
