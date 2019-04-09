package com.el.ariby.ui.main.menu.club;

public class ClubItem {
    String mainLogo; // 클럽 대표사진
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
    public ClubItem(String title, long num, String map) {
        this.title = title;
        this.number = num;
        this.map=map;
    }
    public ClubItem(String mainLogo,String title, long num, String map){
        this.mainLogo=mainLogo;
        this.title = title;
        this.number = num;
        this.map=map;
    }

    public String getMainLogo() {
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

}
