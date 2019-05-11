package com.el.ariby.ui.main.menu;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.el.ariby.ui.main.menu.ranking.RankingAdapter;
import com.el.ariby.ui.main.menu.ranking.RankingItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.el.ariby.R;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

        btnTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });

        btnDistance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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

}