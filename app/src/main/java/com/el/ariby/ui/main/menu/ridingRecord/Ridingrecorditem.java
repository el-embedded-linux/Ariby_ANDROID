package com.el.ariby.ui.main.menu.ridingRecord;

public class Ridingrecorditem {

    String date;
    String ridingtime;
    String ridingkm;
    String ridingkcal;

    public String getRidingkm() {
        return ridingkm;
    }

    public void setRidingkm(String ridingkm) {
        this.ridingkm = ridingkm;
    }

    public String getRidingkcal() {
        return ridingkcal;
    }

    public void setRidingkcal(String ridingkcal) {
        this.ridingkcal = ridingkcal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRidingtime() {
        return ridingtime;
    }

    public void setRidingtime(String ridingtime) {
        this.ridingtime = ridingtime;
    }


    public Ridingrecorditem(String date, String ridingtime) {
        this.date = date;
        this.ridingtime = ridingtime;

    }

}
