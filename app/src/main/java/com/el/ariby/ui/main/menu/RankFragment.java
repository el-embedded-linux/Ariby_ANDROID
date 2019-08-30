package com.el.ariby.ui.main.menu;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.ui.main.menu.ranking.CustomRanking;
import com.el.ariby.ui.main.menu.ranking.RankingAlarm;
import com.el.ariby.ui.main.menu.ranking.RankingItem;
import com.el.ariby.ui.main.menu.ranking.RankingTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.el.ariby.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RankFragment extends Fragment {

    private Context mcontext;
    String[] arrayDatas;
    ListView listRank;
    Button btnTime;
    Button btnDistance;
    Spinner dropDown;
    TextView txtRidingTime;
    TextView txtRidingDis;
    TextView txtRank;
    List<String> dropDownItems = new ArrayList<String>();
    DatabaseReference ref;
    RankingAdapter adapter;
    TextView txtUpDown;
    ImageView imgUpDown;
    ArrayList<RankingItem> rankingItems = new ArrayList<RankingItem>();
    ArrayList<String> setRank = new ArrayList<String>();
    int setTimeFlag = 0; //0이면 오늘, 1이면 이번달
    int setStandardFlag = 0; //0이면 거리(default), 1이면 거리, 2이면 시간
    private PendingIntent alarmIntent;
    private AlarmManager alarmManager;
    int reset = 0;

    int swipe_mode = 0; //0 : dailyRank(), 1:monthlyRank(), 2:timeRank(), 3:disRank();
    RankingTime rankingTime = new RankingTime();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rank, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("RECORD");
        final SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_layout_rank);
        btnDistance = v.findViewById(R.id.btn_ridingDis);
        btnTime = v.findViewById(R.id.btn_ridingTime);
        dropDown = v.findViewById(R.id.dropDown);
        listRank = v.findViewById(R.id.list_rank);
        txtRidingDis = v.findViewById(R.id.txt_ridingDis);
        txtRidingTime = v.findViewById(R.id.txt_ridingTime);
        txtRank = v.findViewById(R.id.txt_rank);
        txtUpDown = v.findViewById(R.id.changed);
        imgUpDown = v.findViewById(R.id.up_down);

        Date date1 = null;

        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(mcontext, R.array.sort, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropDown.setAdapter(sortAdapter);
        dropDown.setPopupBackgroundResource(R.color.colorWhite);

        //현재 날짜/시간 가져오기
        long currentTime = System.currentTimeMillis();
        final Date date = new Date(currentTime);
        Date month = new Date(currentTime);
        Date rightNow = new Date(currentTime);
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        SimpleDateFormat sdfMin = new SimpleDateFormat("mm");
        SimpleDateFormat sdfSec = new SimpleDateFormat("ss");

        //나중에 사용
        String thisYear = sdfYear.format(date);
        String thisMonth = sdfMonth.format(date);
        String thisDay = sdfDay.format(date);
        String thisHour = sdfHour.format(date);
        String thisMin = sdfMin.format(date);
        String thisSec = sdfSec.format(date);
        String monthCheckStr = thisMonth + "-01-" + thisYear + " 00:00:00";
        final String now = thisYear + thisMonth + thisDay + thisHour;
        String rightNowStr = thisMonth + "-" + thisDay + "-" + thisYear + " " + thisHour + ":" + thisMin + ":" + thisSec;
        Log.e("right now : ", rightNowStr);

        //TODO. 나중에 아랫줄 지우고 this~이용한 스트링 만들기
        String dailyCheckStr = "05-09-2019 09:00:00";

        DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        try {
            date1 = (Date) format.parse(dailyCheckStr);
            month = (Date) format.parse(monthCheckStr);
            rightNow = (Date) format.parse(rightNowStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output = date1.getTime() / 1000L;
        String str = Long.toString(output);
        final long timestamp = Long.parseLong(str) * 1000;

        long nowOutPut = rightNow.getTime() / 1000L;
        String nowStr = Long.toString(nowOutPut);
        final long nowTimestamp = Long.parseLong(nowStr) * 1000;

        Log.e("rightNow? : ", String.valueOf(nowTimestamp));

        long monthOutput = month.getTime() / 1000L;
        String monthStr = Long.toString(monthOutput);
        final long monthTimestamp = Long.parseLong(monthStr) * 1000;
        final String check = "dailyData/1557360000000/";
        final String check2 = "/dailyData/1557360000000/";
        //TODO.나중에 monthTimestamp이용한 monthCheck로 바꾸기.
        //final String monthCheck = "monthlyData/"+String.valueOf(monthTimestamp)+"/";
        //Test용.
        final String monthCheck = "monthlyData/1556636400000/";
        //final String monthCheck = "monthlyData/1556636400000";


        rankingTime.setDailyDate(check);
        rankingTime.setMonthlyDate(monthCheck);

        Log.d("monthCheck check : ", monthCheck);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dropDown.getSelectedItem().equals("오늘의 랭킹")) {
                    setTimeFlag = 0;
                    swipe_mode=0;
                    dailyRank();
                }

                if (dropDown.getSelectedItem().equals("이번 달의 랭킹")) {
                    setTimeFlag = 1;
                    swipe_mode=1;
                    monthlyRank();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setTimeFlag = 0;
            }
        });


        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnTime.setBackgroundColor(getResources().getColor(R.color.colorGreenLight2));
                //btnTime.setBackgroundColor(Color.CYAN);
                btnTime.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ranking_selected));
                btnDistance.setBackgroundColor(Color.WHITE);
                setStandardFlag = 2;
                Log.d("setTimeFlag = ", String.valueOf(setTimeFlag));
                swipe_mode=2;
                timeRank();
            }

        });

        btnDistance.setOnClickListener(new View.OnClickListener() {
            // void disRank(){
            @Override
            public void onClick(View v) {
                //btnDistance.setBackgroundColor(getResources().getColor(R.color.colorGreenLight2));
                btnDistance.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ranking_selected));
                btnTime.setBackgroundColor(Color.WHITE);
                setStandardFlag = 1;
                swipe_mode=3;
                disRank();
            }
        });

        adapter = new RankingAdapter();
        listRank.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if(swipe_mode==0)
                {
                    dailyRank();
                }else if(swipe_mode==1)
                {
                    monthlyRank();
                }else if(swipe_mode==2)
                {
                    timeRank();
                }else if(swipe_mode==3)
                {
                    disRank();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return v;

    }

    public void monthlyRank(){
        final String check = rankingTime.getDailyDate();
        final String monthCheck = rankingTime.getMonthlyDate();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clearItem();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String dataCheck, rank = null;
                        String nickname, profile, runningDistance, runningTime, changed, imgUpDown;

                        try {
                            dataCheck = snapshot.child(monthCheck).getValue().toString();
                            if (dataCheck.isEmpty()) {
                                continue;
                            } else {
                                nickname = snapshot.child("userInfo/nickname").getValue().toString();
                                profile = snapshot.child("userInfo/profile").getValue().toString();
                                //rank = snapshot.child(monthCheck).child("monthlyRank").getValue().toString();
                                runningDistance = snapshot.child(monthCheck).child("monthlyDis").getValue().toString();
                                runningTime = snapshot.child(monthCheck).child("monthlyTime").getValue().toString();
                                changed = snapshot.child("userInfo/upDownImg").getValue().toString();
                                imgUpDown = snapshot.child("userInfo/upDownTxt").getValue().toString();
                                //Log.d("RankFragment : ",nickname+",  "+rank+",  "+runningDistance+",  "+runningTime);
                                //TODO. 나중에 monthly로 바꾸기!!
                                if (setStandardFlag == 0 || setStandardFlag == 1) {
                                    rank = snapshot.child(monthCheck).child("month_dis_rank").getValue().toString(); //데일리 거리순
                                } else if (setStandardFlag == 2) {
                                    rank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString(); //데일리 시간
                                }
                                adapter.addItem(new RankingItem(profile, nickname, runningDistance, runningTime, rank, changed, imgUpDown));
                            }
                        } catch (Exception e4) {

                        }
                    }
                }
                if (setStandardFlag == 0 || setStandardFlag == 1) {
                    Collections.sort(rankingItems, adapter.sortByDis);
                } else if (setStandardFlag == 2) {
                    Collections.sort(rankingItems, adapter.sortByTime);
                }
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (int i = 0; i < rankingItems.size(); i++) {
                            int rank = i + 1;
                            String getDis = rankingItems.get(i).getRidingDis();

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String monthlyTotalDis;
                                    String monthlyTotalTime;
                                    String uid;
                                    String putRank;
                                    String monthlyRankCheck;
                                    String month_dis_rank;
                                    String month_time_rank;
                                    String getPreRank;
                                    String upDownDis = null;
                                    String upDownTime = null;
                                    int changedRank = 0;

                                    try {
                                        monthlyRankCheck = snapshot.child(monthCheck).getValue().toString();
                                        if (monthlyRankCheck.isEmpty()) {
                                            continue;
                                        } else {
                                            monthlyTotalDis = snapshot.child(monthCheck).child("monthlyDis").getValue().toString();
                                            if (getDis.equals(monthlyTotalDis)) {
                                                uid = snapshot.getKey();
                                                Log.e("플래그 : ", String.valueOf(setStandardFlag));
                                                Log.e("왜 안되냐 : ", uid + ", " + rank);
                                                if (setStandardFlag == 0 || setStandardFlag == 1) {
                                                    Log.e("if문 통과 : ", uid + ", " + rank);
                                                    ref.child(uid).child("/" + monthCheck).child("month_dis_rank").setValue(String.valueOf(rank));
                                                    rankingItems.get(i).setRank(String.valueOf(rank));
                                                    month_dis_rank = snapshot.child(uid).child(monthCheck).child("month_dis_rank").getValue().toString();

                                                    if (Integer.parseInt(month_dis_rank) == 0) {
                                                        Log.e("왜 안되냐2 : ", uid + ", " + rank);
                                                        ref.child(uid).child("/" + monthCheck).child("month_dis_rank").setValue(String.valueOf(rank));
                                                        putRank = snapshot.child(monthCheck).child("month_dis_rank").getValue().toString();
                                                        rankingItems.get(i).setRank(putRank);
                                                        rankingItems.get(i).setImgUpDown("0");
                                                        rankingItems.get(i).setTxtUpDown("0");
                                                    } else if (Integer.parseInt(month_dis_rank) != 0) {
                                                        getPreRank = snapshot.child(monthCheck).child("month_dis_rank").getValue().toString();
                                                        if (getPreRank.equals(rank)) {
                                                            upDownDis = snapshot.child("monthlyData/upDown").child("upDownDis").getValue().toString();
                                                            if (Integer.valueOf(upDownDis) > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (Integer.valueOf(upDownDis) == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (Integer.valueOf(upDownDis) < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                        } else if (!getPreRank.equals(rank)) {
                                                            changedRank = Integer.parseInt(getPreRank) - rank;
                                                            upDownDis = snapshot.child("monthlyData/upDown").child("upDownDis").getValue().toString();
                                                            changedRank = changedRank + Integer.parseInt(upDownDis);
                                                            if (changedRank > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (changedRank == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (changedRank < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                            ref.child(uid).child("/monthlyData/upDown").child("upDownDis").setValue(changedRank);
                                                        }
                                                    }
                                                    //ref.child(uid).child("/"+monthCheck).child("month_dis_rank").setValue(String.valueOf(rank));
                                                    //rankingItems.get(i).setRank(String.valueOf(rank));
                                                } else if (setStandardFlag == 2) {
                                                    month_time_rank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString();
                                                    if (Integer.parseInt(month_time_rank) == 0) {
                                                        ref.child(uid).child("/" + monthCheck).child("month_time_rank").setValue(String.valueOf(rank));
                                                        putRank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString();
                                                        rankingItems.get(i).setRank(putRank);
                                                        rankingItems.get(i).setImgUpDown("0");
                                                        rankingItems.get(i).setTxtUpDown("0");
                                                    } else if (Integer.parseInt(month_time_rank) != 0) {
                                                        getPreRank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString();
                                                        if (getPreRank.equals(rank)) {
                                                            upDownTime = snapshot.child("monthlyData/upDown").child("upDownTime").getValue().toString();
                                                            if (Integer.valueOf(upDownTime) > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (Integer.valueOf(upDownTime) == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (Integer.valueOf(upDownTime) < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(upDownTime));
                                                        } else if (!getPreRank.equals(rank)) {
                                                            changedRank = Integer.parseInt(getPreRank) - rank;
                                                            upDownTime = snapshot.child("monthlyData/upDown").child("upDownTime").getValue().toString();
                                                            changedRank = changedRank + Integer.parseInt(upDownTime);
                                                            if (Integer.valueOf(upDownTime) > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (Integer.valueOf(upDownTime) == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (Integer.valueOf(upDownTime) < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(upDownTime));
                                                            ref.child(uid).child("/monthlyData/upDown").child("upDownTime").setValue(changedRank);
                                                        }
                                                    }


                                                }

                                            }
                                        }
                                    } catch (Exception e3) {

                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

public void dailyRank(){
    final String check = rankingTime.getDailyDate();
    final String monthCheck = rankingTime.getMonthlyDate();
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            adapter.clearItem();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dataCheck, rank = null;
                    String nickname, profile, runningDistance, runningTime, changed, imgUpDown;
                    try {
                        dataCheck = snapshot.child(check).getValue().toString();
                        if (dataCheck.isEmpty()) {
                            continue;
                        } else {
                            nickname = snapshot.child("userInfo/nickname").getValue().toString();
                            profile = snapshot.child("userInfo/profile").getValue().toString();
                            //rank = snapshot.child(check).child("dailyRank").getValue().toString();
                            runningDistance = snapshot.child(check).child("dailyTotalDis").getValue().toString();
                            runningTime = snapshot.child(check).child("dailyTotalTime").getValue().toString();
                            changed = snapshot.child("userInfo/upDownTxt").getValue().toString(); //Todo. 나중에 필요 없어짐...?
                            imgUpDown = snapshot.child("userInfo/upDownImg").getValue().toString();
                            if (setStandardFlag == 0 || setStandardFlag == 1) {
                                rank = snapshot.child(check).child("daily_dis_rank").getValue().toString(); //데일리 거리순
                            } else if (setStandardFlag == 2) {
                                rank = snapshot.child(check).child("daily_time_rank").getValue().toString(); //데일리 시간
                            }
                            adapter.addItem(new RankingItem(profile, nickname, runningDistance, runningTime, rank, changed, imgUpDown));
                        }
                    } catch (Exception e2) {

                    }
                }
            }
            if (setStandardFlag == 0 || setStandardFlag == 1) {
                Collections.sort(rankingItems, adapter.sortByDis);
                Log.e("거리 순 정렬--", "done");
            } else if (setStandardFlag == 2) {
                Collections.sort(rankingItems, adapter.sortByTime);
                Log.e("시간 순 정렬", "done");
            }
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String lastSort_dailyDis, lastSort_dailyTime;
                    lastSort_dailyDis = String.valueOf(dataSnapshot.child("lastSort").child("daily_dis").getValue()); //데일리 거리순
                    lastSort_dailyTime = String.valueOf(dataSnapshot.child("lastSort").child("daily_time").getValue()); //데일리 시간

                    for (int i = 0; i < rankingItems.size(); i++) {
                        int rank = i + 1;
                        String getDis = rankingItems.get(i).getRidingDis();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String dailyTotalDis;
                                String dailyTotalTime;
                                String uid;
                                String putRank;
                                String dailyRankCheck;
                                String daily_dis_rank;
                                String daily_time_rank;
                                String getPreRank;
                                String dailyRank, upDownDis = null;
                                String upDownTime = null;
                                int changedRank = 0;

                                try {
                                    dailyRankCheck = snapshot.child(check).getValue().toString();
                                    if (dailyRankCheck.isEmpty()) {
                                        continue;
                                    } else {
                                        dailyTotalDis = snapshot.child(check).child("dailyTotalDis").getValue().toString();
                                        if (getDis.equals(dailyTotalDis)) {
                                            uid = snapshot.getKey();
                                            rankingItems.get(i).setRank(String.valueOf(rank));
                                            Log.e("setStandardFlag : ", String.valueOf(setStandardFlag));
                                            if (setStandardFlag == 0 || setStandardFlag == 1) {//거리순 정렬
                                                daily_dis_rank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                //ref.child(uid).child(check2).child("daily_dis_rank").setValue(String.valueOf(rank));
                                                //putRank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                //Log.d("putRank : ",putRank);


                                                if (Integer.parseInt(daily_dis_rank) == 0) {////정렬이 안된 상태이면 다시 정렬해서 랭킹 매기기
                                                    ref.child(uid).child("/"+check).child("daily_dis_rank").setValue(String.valueOf(rank));
                                                    putRank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                    rankingItems.get(i).setRank(putRank);
                                                    rankingItems.get(i).setImgUpDown("0");
                                                    rankingItems.get(i).setTxtUpDown("0");
                                                } else if (Integer.parseInt(daily_dis_rank) != 0) { //Todo. 숫자 조정하기. 5시간 이내는 업다운 변동 없도록.
                                                    // if (upDownCheck >= 50) { //업데이트

                                                    getPreRank = snapshot.child(check).child("daily_dis_rank").getValue().toString();

                                                    if (getPreRank.equals(rank)) {
                                                        Log.d("순위 변동 없음 : ", "done");
                                                        upDownDis = snapshot.child("dailyData/upDown").child("upDownDis").getValue().toString();
                                                        if (Integer.valueOf(upDownDis) > 0) {
                                                            Log.e("변동x changedRank : ", String.valueOf(changedRank));
                                                            rankingItems.get(i).setImgUpDown("1");
                                                        } else if (Integer.valueOf(upDownDis) == 0) {
                                                            rankingItems.get(i).setImgUpDown("0");
                                                        } else if (Integer.valueOf(upDownDis) < 0) {
                                                            rankingItems.get(i).setImgUpDown("-1");
                                                        }

                                                        rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                    } else if (!getPreRank.equals(rank)) {
                                                        Log.e("순위 변동 있음 : ", "done");
                                                        changedRank = Integer.parseInt(getPreRank) - rank;
                                                        Log.e("changedRank : ", String.valueOf(changedRank));
                                                        upDownDis = snapshot.child("dailyData/upDown").child("upDownDis").getValue().toString();
                                                        Log.d("upDownDis : ", upDownDis);
                                                        changedRank = changedRank + Integer.parseInt(upDownDis);

                                                        Log.d("changedRank + upDown ", String.valueOf(changedRank));
                                                        if (changedRank > 0) {
                                                            Log.e("changedRank : ", String.valueOf(changedRank));
                                                            rankingItems.get(i).setImgUpDown("1");
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                        } else if (changedRank == 0) {
                                                            rankingItems.get(i).setImgUpDown("0");
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                        } else if (changedRank < 0) {
                                                            rankingItems.get(i).setImgUpDown("-1");
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                        }

                                                        ref.child(uid).child("/dailyData/upDown").child("upDownDis").setValue(changedRank);

                                                    }


                                                }

                                                ref.child(uid).child("/"+check).child("daily_dis_rank").setValue(String.valueOf(rank));
                                                //putRank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                //Log.d("putRank : ",putRank);
                                                rankingItems.get(i).setRank(String.valueOf(rank));

                                            } else if (setStandardFlag == 2) { //시간 순 정렬

                                                daily_time_rank = snapshot.child(check).child("daily_time_rank").getValue().toString();

                                                if (Integer.parseInt(daily_time_rank) == 0) { //정렬이 아직 안된 데이터이면 정렬해서 랭킹 매기기
                                                    ref.child(uid).child("/"+check).child("daily_time_rank").setValue(String.valueOf(rank));
                                                    putRank = snapshot.child(check).child("daily_time_rank").getValue().toString();
                                                    rankingItems.get(i).setRank(putRank);
                                                    rankingItems.get(i).setImgUpDown("0");
                                                    rankingItems.get(i).setTxtUpDown("0");
                                                } else if (Integer.parseInt(daily_time_rank) != 0) { //0이 아닐때

                                                    getPreRank = snapshot.child(check).child("daily_time_rank").getValue().toString();
                                                    if (getPreRank.equals(rank)) {
                                                        Log.d("데일리 시간 순 정렬", "순위 변동 없음");
                                                        upDownDis = snapshot.child("dailyData/upDown").child("upDownTime").getValue().toString();
                                                        if (Integer.valueOf(upDownDis) > 0) {
                                                            rankingItems.get(i).setImgUpDown("1");
                                                        } else if (Integer.valueOf(upDownDis) == 0) {
                                                            rankingItems.get(i).setImgUpDown("0");
                                                        } else if (Integer.valueOf(upDownDis) < 0) {
                                                            rankingItems.get(i).setImgUpDown("-1");
                                                        }
                                                        rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                    } else if (!getPreRank.equals(rank)) {
                                                        changedRank = Integer.parseInt(getPreRank) - rank;
                                                        upDownDis = snapshot.child("dailyData/upDown").child("upDownTime").getValue().toString();
                                                        changedRank = changedRank + Integer.parseInt(upDownDis);
                                                        if (changedRank > 0) {
                                                            rankingItems.get(i).setImgUpDown("1");
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                        } else if (changedRank == 0) {
                                                            rankingItems.get(i).setImgUpDown("0");
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                        } else if (changedRank < 0) {
                                                            rankingItems.get(i).setImgUpDown("-1");
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                        }
                                                        ref.child(uid).child("/dailyData/upDown").child("upDownTime").setValue(changedRank);
                                                    }

                                                }
                                            }
                                        }
                                    }

                                } catch (Exception e3) {

                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
    public void timeRank() {
        final String check = rankingTime.getDailyDate();
        final String monthCheck = rankingTime.getMonthlyDate();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                           adapter.clearItem();
                                                           if (dataSnapshot.exists()) {
                                                               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                   int[] time;
                                                                   String dataCheck = null;
                                                                   String nickname, profile, runningDistance, runningTime, rank, changed, imgUpDown;
                                                                   try {
                                                                       dataCheck = snapshot.child(check).getValue().toString();
                                                                       if (dataCheck.isEmpty()) {

                                                                           continue;
                                                                       } else {
                                                                           nickname = snapshot.child("userInfo/nickname").getValue().toString();
                                                                           profile = snapshot.child("userInfo/profile").getValue().toString();

                                                                           if (setTimeFlag == 0) {
                                                                               //rank = snapshot.child(check).child("dailyRank").getValue().toString();
                                                                               runningDistance = snapshot.child(check).child("dailyTotalDis").getValue().toString();
                                                                               runningTime = snapshot.child(check).child("dailyTotalTime").getValue().toString();
                                                                               changed = snapshot.child("userInfo/upDownImg").getValue().toString();
                                                                               imgUpDown = snapshot.child("userInfo/upDownTxt").getValue().toString();
                                                                               rank = snapshot.child(check).child("daily_time_rank").getValue().toString(); //데일리 시간
                                                                               adapter.addItem(new RankingItem(profile, nickname, runningDistance, runningTime, rank, changed, imgUpDown));
                                                                           } else if (setTimeFlag == 1) {
                                                                               //rank = snapshot.child(monthCheck).child("monthlyRank").getValue().toString();
                                                                               runningDistance = snapshot.child(monthCheck).child("monthlyDis").getValue().toString();
                                                                               runningTime = snapshot.child(monthCheck).child("monthlyTime").getValue().toString();
                                                                               changed = snapshot.child("userInfo/upDownImg").getValue().toString();
                                                                               imgUpDown = snapshot.child("userInfo/upDownTxt").getValue().toString();
                                                                               //TODO. 나중에 먼슬리 타임 랭크 불러오는걸로 바꾸기.
                                                                               rank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString(); //데일리 시간
                                                                               adapter.addItem(new RankingItem(profile, nickname, runningDistance, runningTime, rank, changed, imgUpDown));
                                                                           }
                                                                       }
                                                                   } catch (Exception e1) {

                                                                   }
                                                               }
                                                           }
                                                           Collections.sort(rankingItems, adapter.sortByTime);
                                                           ref.addValueEventListener(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                   for (int i = 0; i < rankingItems.size(); i++) {
                                                                       //Todo. 나중에 데이터베이스의 기존 순위 읽어와서 바뀐 순위와의 변동 구하기

                                                                       final int rank = i + 1;
                                                                       rankingItems.get(i).setRank(String.valueOf(rank));
                                                                       String getRank = rankingItems.get(i).getRank();
                                                                       // rankingItems.get(i).setRank(String.valueOf(rank));
                                                                       final String getDis = rankingItems.get(i).getRidingDis(); //데일리
                                                                       final String getMonthDis = rankingItems.get(i).getRidingDis(); //먼슬리

                                                                       if (dataSnapshot.exists()) {
                                                                           for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                               String dailyTotalDis;
                                                                               String monthlyTotalDis;
                                                                               String putRank;
                                                                               String dailyRankCheck, month_dis_rank;
                                                                               String uid;
                                                                               String daily_dis_rank;
                                                                               String getPreRank;
                                                                               String dailyRank, upDownDis = null;
                                                                               String upDownTime = null;
                                                                               int changedRank = 0;

                                                                               try {
                                                                                   dailyRankCheck = snapshot.child(check).getValue().toString();
                                                                                   if (dailyRankCheck.isEmpty()) {
                                                                                       continue;
                                                                                   } else {
                                                                                       dailyTotalDis = snapshot.child("/"+check).child("dailyTotalTime").getValue().toString();
                                                                                       monthlyTotalDis = snapshot.child(monthCheck).child("monthlyTime").getValue().toString();
                                                                                       rankingItems.get(i).setRank(String.valueOf(rank));
                                                                                       if (setTimeFlag == 0) { //당일 랭킹
                                                                                           if (getDis.equals(dailyTotalDis)) {
                                                                                               uid = snapshot.getKey();

                                                                                               daily_dis_rank = snapshot.child(check).child("daily_time_rank").getValue().toString();
                                                                                               if (Integer.parseInt(daily_dis_rank) == 0) {

                                                                                                   ref.child(uid).child("/"+check).child("daily_time_rank").setValue(String.valueOf(rank));
                                                                                                   putRank = snapshot.child(check).child("daily_time_rank").getValue().toString();
                                                                                                   rankingItems.get(i).setRank(putRank);
                                                                                                   rankingItems.get(i).setImgUpDown("0");
                                                                                                   rankingItems.get(i).setTxtUpDown("0");
                                                                                               } else if (Integer.parseInt(daily_dis_rank) != 0) {
                                                                                                   getPreRank = snapshot.child(check).child("daily_time_rank").getValue().toString();
                                                                                                   if (getPreRank.equals(rank)) {
                                                                                                       upDownDis = snapshot.child("dailyData/upDown").child("upDownTime").getValue().toString();
                                                                                                       if (Integer.valueOf(upDownDis) > 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("1");
                                                                                                       } else if (Integer.valueOf(upDownDis) == 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("0");
                                                                                                       } else if (Integer.valueOf(upDownDis) < 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("-1");
                                                                                                       }
                                                                                                       rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                                                                   } else if (!getPreRank.equals(rank)) {
                                                                                                       changedRank = Integer.parseInt(getPreRank) - rank;
                                                                                                       upDownDis = snapshot.child("dailyData/upDown").child("upDownTime").getValue().toString();
                                                                                                       changedRank = changedRank + Integer.valueOf(upDownDis);
                                                                                                       if (changedRank > 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("1");
                                                                                                       } else if (changedRank == 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("0");
                                                                                                       } else if (changedRank < 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("-1");
                                                                                                       }
                                                                                                       rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                                                                       ref.child(uid).child("/monthlyData/upDown").child("upDownTime").setValue(changedRank);
                                                                                                   }
                                                                                               }
                                                                                               ref.child(uid).child("/"+check).child("daily_time_rank").setValue(String.valueOf(rank));
                                                                                               rankingItems.get(i).setRank(String.valueOf(rank));
                                                                                           }
                                                                                       }
                                                                                       if (setTimeFlag == 1) { //이번달 랭킹
                                                                                           if (getDis.equals(monthlyTotalDis)) {
                                                                                               uid = snapshot.getKey();

                                                                                               month_dis_rank = snapshot.child(uid).child(monthCheck).child("month_time_rank").getValue().toString();
                                                                                               if (Integer.parseInt(month_dis_rank) == 0) {
                                                                                                   ref.child(uid).child("/" + monthCheck).child("month_time_rank").setValue(String.valueOf(rank));
                                                                                                   putRank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString();
                                                                                                   rankingItems.get(i).setRank(putRank);
                                                                                                   rankingItems.get(i).setImgUpDown("0");
                                                                                                   rankingItems.get(i).setTxtUpDown("0");
                                                                                               } else if (Integer.parseInt(month_dis_rank) != 0) {
                                                                                                   getPreRank = snapshot.child(monthCheck).child("month_time_rank").getValue().toString();
                                                                                                   if (getPreRank.equals(rank)) {
                                                                                                       upDownDis = snapshot.child("monthlyData/upDown").child("upDownTime").getValue().toString();
                                                                                                       if (Integer.valueOf(upDownDis) > 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("1");
                                                                                                       } else if (Integer.valueOf(upDownDis) == 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("0");
                                                                                                       } else if (Integer.valueOf(upDownDis) < 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("-1");
                                                                                                       }
                                                                                                       rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                                                                   } else if (!getPreRank.equals(rank)) {
                                                                                                       changedRank = Integer.parseInt(getPreRank) - rank;
                                                                                                       upDownDis = snapshot.child("monthlyData/upDown").child("upDownTime").getValue().toString();
                                                                                                       changedRank = changedRank + Integer.parseInt(upDownDis);
                                                                                                       if (changedRank > 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("1");
                                                                                                       } else if (changedRank == 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("0");
                                                                                                       } else if (changedRank < 0) {
                                                                                                           rankingItems.get(i).setImgUpDown("-1");
                                                                                                       }
                                                                                                       rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                                                                       ref.child(uid).child("/monthlyData/upDown").child("upDownTime").setValue(changedRank);
                                                                                                   }
                                                                                               }
                                                                                               ref.child(uid).child("/" + monthCheck).child("month_time_rank").setValue(String.valueOf(rank));
                                                                                               rankingItems.get(i).setRank(String.valueOf(rank));
                                                                                           }
                                                                                       }

                                                                                   }
                                                                               } catch (Exception e) {

                                                                               }
                                                                           }
                                                                       }
                                                                   }
                                                                   adapter.notifyDataSetChanged();
                                                               }

                                                               @Override
                                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                                               }
                                                           });
                                                           adapter.notifyDataSetChanged();
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                                       }
                                                   });
    }


    public void disRank(){
        final String check = rankingTime.getDailyDate();
        final String monthCheck = rankingTime.getMonthlyDate();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapter.clearItem();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String rank;
                        String dailyRankCheck;
                        String nickname;
                        String profile;
                        String monthlyRank;
                        String runningDistance;
                        String runningTime, changed, imgUpDown;

                        try {
                            dailyRankCheck = snapshot.child(check).getValue().toString();
                            if (dailyRankCheck.isEmpty()) {
                                continue;
                            } else {
                                nickname = snapshot.child("userInfo/nickname").getValue().toString();
                                profile = snapshot.child("userInfo/profile").getValue().toString();
                                if (setTimeFlag == 0) { //당일데이터
                                    //dailyRank = snapshot.child(check).child("dailyRank").getValue().toString();
                                    runningDistance = snapshot.child(check).child("dailyTotalDis").getValue().toString();
                                    runningTime = snapshot.child(check).child("dailyTotalTime").getValue().toString();
                                    changed = snapshot.child("userInfo/upDownImg").getValue().toString();
                                    imgUpDown = snapshot.child("userInfo/upDownTxt").getValue().toString();
                                    //TODO. 나중에 바꾸기
                                    rank = snapshot.child(check).child("daily_dis_rank").getValue().toString(); //데일리 시간
                                    adapter.addItem(new RankingItem(profile, nickname, runningDistance, runningTime, rank, changed, imgUpDown));
                                } else if (setTimeFlag == 1) { //이번달 데이터
                                    //monthlyRank = snapshot.child(monthCheck).child("monthlyRank").getValue().toString();
                                    runningDistance = snapshot.child(monthCheck).child("monthlyDis").getValue().toString();
                                    runningTime = snapshot.child(monthCheck).child("monthlyTime").getValue().toString();
                                    changed = snapshot.child("userInfo/upDownImg").getValue().toString();
                                    imgUpDown = snapshot.child("userInfo/upDownTxt").getValue().toString();
                                    //TODO. 나중에 바꾸기.
                                    rank = snapshot.child(monthCheck).child("month_dis_rank").getValue().toString(); //데일리 시간
                                    adapter.addItem(new RankingItem(profile, nickname, runningDistance, runningTime, rank, changed, imgUpDown));
                                }

                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }
                Collections.sort(rankingItems, adapter.sortByDis);
                //
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (int i = 0; i < rankingItems.size(); i++) {
                            final int rank = i + 1;
                            rankingItems.get(i).setRank(String.valueOf(rank));
                            String getRank = rankingItems.get(i).getRank();
                            // rankingItems.get(i).setRank(String.valueOf(rank));
                            final String getDis = rankingItems.get(i).getRidingDis(); //데일리
                            final String getMonthDis = rankingItems.get(i).getRidingDis(); //먼슬리

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String dailyTotalDis;
                                    String monthlyTotalDis;
                                    String putRank;
                                    String dailyRankCheck;
                                    String uid, daily_dis_rank, getPreRank, month_dis_rank;
                                    String upDownDis = null;
                                    String upDownTime = null;
                                    int changedRank = 0;

                                    try {
                                        dailyRankCheck = snapshot.child(check).getValue().toString();
                                        if (dailyRankCheck.isEmpty()) {
                                            continue;
                                        } else {
                                            dailyTotalDis = snapshot.child("/"+check).child("dailyTotalDis").getValue().toString();
                                            monthlyTotalDis = snapshot.child(monthCheck).child("monthlyDis").getValue().toString();
                                            rankingItems.get(i).setRank(String.valueOf(rank));
                                            if (setTimeFlag == 0) { //당일 랭킹
                                                if (getDis.equals(dailyTotalDis)) {
                                                    uid = snapshot.getKey();
                                                    rankingItems.get(i).setRank(String.valueOf(rank));
                                                    daily_dis_rank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                    if (Integer.parseInt(daily_dis_rank) == 0) {

                                                        ref.child(uid).child("/"+check).child("daily_dis_rank").setValue(String.valueOf(rank));
                                                        putRank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                        rankingItems.get(i).setRank(putRank);
                                                        rankingItems.get(i).setImgUpDown("0");
                                                        rankingItems.get(i).setTxtUpDown("0");
                                                    } else if (Integer.parseInt(daily_dis_rank) != 0) {
                                                        getPreRank = snapshot.child(check).child("daily_dis_rank").getValue().toString();
                                                        if (getPreRank.equals(rank)) {
                                                            upDownDis = snapshot.child("dailyData/upDown").child("upDownDis").getValue().toString();
                                                            if (Integer.valueOf(upDownDis) > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (Integer.valueOf(upDownDis) == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (Integer.valueOf(upDownDis) < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                        } else if (!getPreRank.equals(rank)) {
                                                            changedRank = Integer.parseInt(getPreRank) - rank;
                                                            upDownDis = snapshot.child("dailyData/upDown").child("upDownDis").getValue().toString();
                                                            changedRank = changedRank + Integer.valueOf(upDownDis);
                                                            if (changedRank > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (changedRank == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (changedRank < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                            ref.child(uid).child("/monthlyData/upDown").child("upDownDis").setValue(changedRank);
                                                        }
                                                    }
                                                    ref.child(uid).child("/"+check).child("daily_dis_rank").setValue(String.valueOf(rank));
                                                    rankingItems.get(i).setRank(String.valueOf(rank));
                                                }
                                            }
                                            if (setTimeFlag == 1) { //이번달 랭킹
                                                if (getDis.equals(monthlyTotalDis)) {
                                                    uid = snapshot.getKey();
                                                    rankingItems.get(i).setRank(String.valueOf(rank));
                                                    month_dis_rank = snapshot.child(uid).child(monthCheck).child("month_dis_rank").getValue().toString();
                                                    if (Integer.parseInt(month_dis_rank) == 0) {
                                                        ref.child(uid).child("/" + monthCheck).child("month_dis_rank").setValue(String.valueOf(rank));
                                                        putRank = snapshot.child(monthCheck).child("month_dis_rank").getValue().toString();
                                                        rankingItems.get(i).setRank(putRank);
                                                        rankingItems.get(i).setImgUpDown("0");
                                                        rankingItems.get(i).setTxtUpDown("0");
                                                    } else if (Integer.parseInt(month_dis_rank) != 0) {
                                                        getPreRank = snapshot.child(monthCheck).child("month_dis_rank").getValue().toString();
                                                        if (getPreRank.equals(rank)) {
                                                            upDownDis = snapshot.child("monthlyData/upDown").child("upDownDis").getValue().toString();
                                                            if (Integer.valueOf(upDownDis) > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (Integer.valueOf(upDownDis) == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (Integer.valueOf(upDownDis) < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(upDownDis));
                                                        } else if (!getPreRank.equals(rank)) {
                                                            changedRank = Integer.parseInt(getPreRank) - rank;
                                                            upDownDis = snapshot.child("monthlyData/upDown").child("upDownDis").getValue().toString();
                                                            changedRank = changedRank + Integer.parseInt(upDownDis);
                                                            if (changedRank > 0) {
                                                                rankingItems.get(i).setImgUpDown("1");
                                                            } else if (changedRank == 0) {
                                                                rankingItems.get(i).setImgUpDown("0");
                                                            } else if (changedRank < 0) {
                                                                rankingItems.get(i).setImgUpDown("-1");
                                                            }
                                                            rankingItems.get(i).setTxtUpDown(String.valueOf(changedRank));
                                                            ref.child(uid).child("/monthlyData/upDown").child("upDownDis").setValue(changedRank);
                                                        }
                                                    }
                                                    ref.child(uid).child("/" + monthCheck).child("month_dis_rank").setValue(String.valueOf(rank));
                                                    rankingItems.get(i).setRank(String.valueOf(rank));

                                                }
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    };

    public class RankingAdapter extends BaseAdapter {
        @Override
        public int getCount() { return rankingItems.size(); }
        @Override
        public Object getItem(int position) { return rankingItems.get(position);}
        @Override
        public long getItemId(int position) { return position; }
        public void addItem(RankingItem item){ rankingItems.add(item); }
        public void addRankChanged (RankingItem items){rankingItems.add(items);}
        Comparator<RankingItem> rankComparator = new Comparator<RankingItem>() {
            @Override
            public int compare(RankingItem item1, RankingItem item2) {
                int ret;
                int item1Rank = Integer.parseInt(item1.getRank());
                int item2Rank = Integer.parseInt(item2.getRank());
                if (item1Rank < item2Rank) {
                    ret = -1;
                } else if (item1Rank == item2Rank) {
                    ret = 0;
                } else {
                    ret = 1;
                }
                return ret;
            }
        };

        Comparator<RankingItem> sortByDis = new Comparator<RankingItem>() {
            @Override
            public int compare(RankingItem item1, RankingItem item2) {
                int ret=0;
                final float item1Dis = Float.parseFloat(item1.getRidingDis());
                float item2Dis = Float.parseFloat(item2.getRidingDis());

                if(item1Dis < item2Dis){
                    ret = 1;
                }else if(item1Dis == item2Dis){
                    ret = 0;
                }else {
                    ret = -1;
                }
                return ret;
            }
        };

        Comparator<RankingItem> sortByTime = new Comparator<RankingItem>() {
            @Override
            public int compare(RankingItem item1, RankingItem item2) {
                int ret = 0;
                final String item1Data = item1.getRidingTime();
                final String item2Data = item2.getRidingTime();

                String [] item1Time = item1Data.split(":");
                String [] item2Time = item2Data.split(":");
                int [] item1TimeInt = new int[item1Time.length];
                int [] item2TimeInt = new int[item2Time.length];
                for(int i=0; i<item1Time.length; i++){
                    item1TimeInt[i] = Integer.parseInt(item1Time[i]);
                    item2TimeInt[i] = Integer.parseInt(item2Time[i]);
                }
                if(item1TimeInt[0] > item2TimeInt[0]){
                    ret = -1;
                }else if(item1TimeInt[0] == item2TimeInt[0]) { //hour이 같을 때

                    if (item1TimeInt[1] > item2TimeInt[1]) { //분확인
                        ret = -1;
                    } else if (item1TimeInt[1] == item2TimeInt[1]) { //분이 같으면
                        if (item1TimeInt[2] > item2TimeInt[2]) { //초 확인
                            ret = -1;
                        } else if (item1TimeInt[2] == item2TimeInt[2]) {
                            ret = 0;
                        } else if (item1TimeInt[2] < item2TimeInt[2]) {
                            ret = 1;
                        }
                    } else if (item1TimeInt[1] < item2TimeInt[1]) { //분이 더 작으면 -1
                        ret = 1;
                    }
                }else if(item1TimeInt[0] < item2TimeInt[0]){
                    ret = 1;
                }

                return ret;
            }
        };

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            CustomRanking view = new CustomRanking(context);
            RankingItem item = rankingItems.get(position);
            view.setTxtRankNick(item.getNickname());
            view.setTxtRidingTime(item.getRidingTime());
            view.setTxtRank(item.getRank());
            view.setImgRankProfile(item.getImgProfile());
            view.setTxtRidingDis(item.getRidingDis());
            view.setTxtUpDown(item.getTxtUpDown());
            view.setImgUpDown(item.getImgUpDown());

            //ToDo. RankingItem  채우고 마저 하기.
            return view;
        }

        public void clearItem(){rankingItems.clear();}
    }

}