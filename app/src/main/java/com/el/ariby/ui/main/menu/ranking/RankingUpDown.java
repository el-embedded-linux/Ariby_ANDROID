package com.el.ariby.ui.main.menu.ranking;

import android.icu.text.SymbolTable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RankingUpDown {

    /*public RankingUpDown(String lastSortTime) {
        this.lastSortTime = lastSortTime;
        Log.i("lastSortTime : ",lastSortTime);
        Log.i("rightNow : ",rightNow);
        calTimeDiff(this.lastSortTime);
    }*/

    public long calTimeDiff(String lastSortTime, String now){

         String last = lastSortTime;
         String current = now;

         Date curDate = null;
         Date reqDate = null;

         SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddHH");
        try {
            reqDate = dateFormat.parse(last);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long reqDateTime = reqDate.getTime();

        try {
            curDate = dateFormat.parse(current);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curDateTime = curDate.getTime();

        long minute = (curDateTime - reqDateTime)/60000;
        Log.d("분 차이 : ", String.valueOf(minute));
        return minute;
    }

    String lastSortTime;
    String rightNow;

}
