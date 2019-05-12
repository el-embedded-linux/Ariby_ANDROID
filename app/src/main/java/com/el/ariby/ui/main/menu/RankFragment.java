package com.el.ariby.ui.main.menu;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.ui.main.menu.ranking.CustomRanking;
import com.el.ariby.ui.main.menu.ranking.RankingItem;
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
    ArrayList<RankingItem> rankingItems = new ArrayList<RankingItem>();
    ArrayList<String> setRank = new ArrayList<String>();
    int sortFlag=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rank, container,false);
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


        Date date1=null;

        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(mcontext, R.array.sort, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(sortAdapter);
        dropDown.setPopupBackgroundResource(R.color.colorPrimary);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dropDown.getSelectedItem().equals("오늘의 랭킹")){
                    sortFlag = 0;
                    Toast.makeText(mcontext, "sortFlag = "+sortFlag, Toast.LENGTH_SHORT).show();
                }
                if(dropDown.getSelectedItem().equals("이번 달의 랭킹")) {
                    sortFlag = 1;
                    Toast.makeText(mcontext, "sortFlag = "+sortFlag, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sortFlag = 0;
            }
        });

        //현재 날짜/시간 가져오기
        long currentTime = System.currentTimeMillis();
        final Date date = new Date(currentTime);
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


        //TODO. 나중에 아랫줄 지우고 this~이용한 스트링 만들기
        String targetData = "05-09-2019 09:00:00";
        DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        try {
            date1 = (Date)format.parse(targetData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output = date1.getTime()/1000L;
        String str = Long.toString(output);
        final long timestamp = Long.parseLong(str) * 1000;
        Log.d("Rank_timeStamp : ", String.valueOf(timestamp));
        Log.d("getYear, month, day : ", thisYear+", "+thisMonth+", "+thisDay);

        final String check = "dailyData/1557360000000/";

        btnTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });

        btnDistance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clearItem();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String dailyTotalDis;
                                String dailyRank;
                                String dailyTotalTime;
                                String dailyRankCheck;
                                String nickname;
                                String profile;
                                try {
                                    dailyRankCheck = snapshot.child(check).getValue().toString();
                                    if (dailyRankCheck.isEmpty()) {
                                        continue;
                                    } else {
                                        Log.d("거리별", dailyRankCheck);
                                        nickname = snapshot.child("userInfo/nickname").getValue().toString();
                                        profile = snapshot.child("userInfo/profile").getValue().toString();
                                        dailyRank = snapshot.child(check).child("dailyRank").getValue().toString();
                                        dailyTotalDis = snapshot.child(check).child("dailyTotalDis").getValue().toString();
                                        dailyTotalTime = snapshot.child(check).child("dailyTotalTime").getValue().toString();
                                        Log.e("거리별 랭킹", dailyRank);

                                        adapter.addItem(new RankingItem(profile, nickname, dailyTotalDis, dailyTotalTime, dailyRank));
                                    }
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                            }
                        }
                        Collections.sort(rankingItems, adapter.sortByDis);
                        Log.d("sortByDis완료됨","done");
                        //
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (int i = 0; i < rankingItems.size(); i++) {
                                        Log.d("출력 : ", rankingItems.get(i).getRank() + ",  " + rankingItems.get(i).getRidingDis());
                                        //Todo. 나중에 데이터베이스의 기존 순위 읽어와서 바뀐 순위와의 변동 구하기
                                        final int rank = i + 1;

                                        String getRank = rankingItems.get(i).getRank();
                                        rankingItems.get(i).setRank(String.valueOf(rank));
                                        final String getDis = rankingItems.get(i).getRidingDis();
                                        Log.e("받아온 디스턴스 : ", getDis);

                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                String dailyTotalDis;
                                                String putRank;
                                                String dailyRankCheck;
                                                String uid;
                                                try {
                                                    dailyRankCheck = snapshot.child(check).getValue().toString();
                                                    Log.d("RankCheck2222 : ", dailyRankCheck);
                                                    if (dailyRankCheck.isEmpty()) {
                                                        continue;
                                                    } else {
                                                        dailyTotalDis = snapshot.child("dailyData/1557360000000/").child("dailyTotalDis").getValue().toString();
                                                        Log.d("TotalDis2222 : ", dailyTotalDis);
                                                        if (getDis.equals(dailyTotalDis)) {
                                                            uid = snapshot.getKey();
                                                            Log.e("MainUid : ", uid);
                                                            ref.child(uid).child("/dailyData/1557360000000/").child("dailyRank").setValue(String.valueOf(rank));
                                                            putRank = snapshot.child(check).child("dailyRank").getValue().toString();

                                                            rankingItems.get(i).setRank(String.valueOf(putRank));
                                                            Log.d("addition end","done");
                                                            break;
                                                        }
                                                    }
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    }
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
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                if(snapshot1.exists()) {
                    for(DataSnapshot snapshot : snapshot1.getChildren()) {

                        String dailyTotalDis;
                        String dailyRank;
                        String dailyTotalTime;
                        String dailyRankCheck;
                        String nickname;
                        String profile ;

                        //String check = "dailyData/"+timestamp+"/";
                        String check = "dailyData/1557360000000/";
                        Log.e("check : ",check);
                        try {
                            dailyRankCheck = snapshot.child(check).getValue().toString();
                            if(dailyRankCheck.isEmpty())
                            {
                                Log.d("오늘 날짜에 대한 데이터는 없음",dailyRankCheck);
                                continue;
                            }else {
                                nickname = snapshot.child("userInfo/nickname").getValue().toString();
                                Log.e("nickname22: ", nickname);
                                profile = snapshot.child("userInfo/profile").getValue().toString();
                                Log.e("profile22 : ", profile);
                                Log.e("dailyRankCheck : ", dailyRankCheck);
                                dailyRank = snapshot.child(check).child("dailyRank").getValue().toString();
                                dailyTotalDis = snapshot.child(check).child("dailyTotalDis").getValue().toString();
                                dailyTotalTime = snapshot.child(check).child("dailyTotalTime").getValue().toString();
                                Log.e("dailyRank : ",dailyRank);
                                adapter.addItem(new RankingItem(profile, nickname, dailyTotalDis, dailyTotalTime, dailyRank));
                            }
                        }catch (Exception e){
                            e.getStackTrace();
                        }
                    }
                }
                Collections.sort(rankingItems, adapter.rankComparator);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        adapter = new RankingAdapter();
        listRank.setAdapter(adapter);
        return v;
    }


    public class RankingAdapter extends BaseAdapter {


        @Override
        public int getCount() { return rankingItems.size(); }
        @Override
        public Object getItem(int position) { return rankingItems.get(position);}
        @Override
        public long getItemId(int position) { return position; }
        public void addItem(RankingItem item){ rankingItems.add(item); }

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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            CustomRanking view = new CustomRanking(context);
            RankingItem item = rankingItems.get(position);
            view.setTxtRankNick(item.getNickname());
            view.setTxtRidingTime(item.getRidingTime());
            Log.d("getRidingTime : ", item.getRidingTime());
            view.setTxtRank(item.getRank());
            view.setImgRankProfile(item.getImgProfile());
            view.setTxtRidingDis(item.getRidingDis());

            //ToDo. RankingItem  채우고 마저 하기.
            return view;
        }

        public void clearItem(){rankingItems.clear();}
    }

}