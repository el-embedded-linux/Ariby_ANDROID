package com.el.ariby.ui.main.menu.challenge;

public class ChallengeItem {
    String chall_img;
    String comment;
    String exp;
    String unit;

    public ChallengeItem(String chall_img, String comment, String exp, String unit) {
        this.chall_img = chall_img;
        this.comment = comment;
        this.exp = exp;
        this.unit = unit;
    }

    public String getChall_img() {
        return chall_img;
    }

    public void setChall_img(String chall_img) {
        this.chall_img = chall_img;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
