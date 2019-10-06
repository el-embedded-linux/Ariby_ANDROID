package com.el.ariby.ui.main.menu.navigation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityMapNavigationBinding;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapNavigationActivity extends AppCompatActivity implements
        MapView.CurrentLocationEventListener {
    ActivityMapNavigationBinding mBinding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("RECORD");
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef;

    MapView mapNaviView;
    String startX, startY, endX, endY;
    ArrayList<PointDouble> naviPoints = new ArrayList<>();
    int naviCount = 0;
    ProgressDialog progressDialog;
    ArrayList<NaviMember> naviMembers = new ArrayList<>();
    long time = 0; // 운행시간 체크
    Timer timer = new Timer(); // 운행시간 체크

    ArrayList<String> disList = new ArrayList<>();
    Date total = new Date(); //하루 누적 주행기록

    String nickname, userProfile;
    String weight = "50";
    Double kcal;

    //2019.09.30 선룡이가 추가함 라즈베리파이 통신용 변수들
    public static final String sIP = "192.168.100.1";
    public static final int sPORT = 3001;
    String raspTurnTypeStr = "";
    int raspDistance = 0;
    int raspTurnType = 0;
    String raspDescription = "";
    DatagramSocket socket;
    InetAddress serverAddr;

    final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    final String myUid = mUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_navigation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            socket = new DatagramSocket();
            serverAddr = InetAddress.getByName(sIP);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mapNaviView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_navi_view);
        mapViewContainer.addView(mapNaviView);
        mapNaviView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapNaviView.setMapTilePersistentCacheEnabled(true);
        //다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능
        mapNaviView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

        Intent intent = getIntent();

        startY = intent.getStringExtra("startY");
        startX = intent.getStringExtra("startX");
        endY = intent.getStringExtra("endY");
        endX = intent.getStringExtra("endX");
        progressDialog = new ProgressDialog(MapNavigationActivity.this);
        progressDialog.setMessage("데이터를 로딩중입니다.");

        userRef = database.getReference("USER");

        Log.e("myUid : ", myUid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nickname = dataSnapshot.child(myUid).child("nickname").getValue().toString();
                userProfile = dataSnapshot.child(myUid).child("userImageURL").getValue().toString();
                weight = dataSnapshot.child(myUid).child("weight").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getMapFind(startY, startX, endY, endX);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Log.d("timer : ", time + "");
                time++;
            }
        };

        timer.schedule(tt, 0, 1000);
    }


    private void send2rasp(){
        Thread mThread = new Thread(new Runnable(){
            @Override
            public void run(){
                String data = "";
                data += raspDescription+"/";
                data += raspTurnTypeStr+"/";
                data += Integer.toString(raspTurnType)+"/";
                data += Integer.toString(raspDistance);
                byte[] buf = data.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);

                //패킷 전송!
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    private void getMapFind(final String startX, final String startY,
                            final String endX, final String endY) {
        progressDialog.show();
        Retrofit retrofit = SelfCall.createRetrofit(MapFindApi.BASEURL);
        MapFindApi apiService = retrofit.create(MapFindApi.class);
        Call<MapFindRepoResponse> call =
                apiService.getMapFind("d7673b71-bc89-416a-9ac6-019e5d8f327a", "1",
                        startX, startY, endX, endY, "123", "234");
        final int[] totalDistance = new int[1];
        call.enqueue(new Callback<MapFindRepoResponse>() {
            @Override
            public void onResponse(Call<MapFindRepoResponse> call,
                                   Response<MapFindRepoResponse> response) {
                MapFindRepoResponse repo = response.body();
                int featuresSize = repo.getFeatures().size();

                MapPolyline polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(128, 255, 51, 0));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(
                        Double.parseDouble(startY), Double.parseDouble(startX)));

                Double kilo = repo.getFeatures().get(0).getProperties().
                        getTotalDistance().doubleValue() / 1000;
                Double time2 = (kilo / 13.0) * 60; // 자전거 소요시간 공식 (거리/속도)*분
                mBinding.txtNaviTime.setText("남은시간 : " + Math.round(time2) + "분");

                if (kilo >= 1.0) //
                    mBinding.txtNaviDistance.setText("남은거리 : " + Math.round(kilo * 10) / 10.0 + "km");
                else
                    mBinding.txtNaviDistance.setText("남은거리 : " + Math.round(kilo * 1000) + "m");

                NaviMember member = new NaviMember();
                for (int i = 0; i < featuresSize; i++) {
                    MapFindRepoResponse.Geometry geometry = repo.getFeatures().get(i).getGeometry();
                    MapFindRepoResponse.Properties properties = repo.getFeatures().get(i).getProperties();
                    PointDouble point;
                    Log.i("Navigation Array info", "index = " + properties.getIndex() + " type = " + geometry.getType());

                    if (geometry.getType().equals("Point")) {
                        point = new PointDouble(
                                (Double) geometry.getCoordinates().get(0),
                                (Double) geometry.getCoordinates().get(1));

                        MapPoint marketPoint3 = MapPoint.mapPointWithGeoCoord(point.getY(), point.getX());
                        naviPoints.add(point);
                        MapPOIItem marker5 = new MapPOIItem(); // 마커 생성
                        marker5.setItemName(repo.getFeatures().get(i).getProperties().getDescription());
                        marker5.setTag(4);
                        marker5.setMapPoint(marketPoint3);
                        marker5.setMarkerType(MapPOIItem.MarkerType.RedPin);

                        mapNaviView.addPOIItem(marker5);
                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(point.getY(), point.getX()));

                        if (i == (featuresSize - 1)) { // 도착지점 추가 (경로 중 마지막은 point로 찍힘)
                            member.setPoint(point);
                            member.setTime(0);
                            member.setDistance(0);
                            member.setDescription(properties.getDescription());
                            naviMembers.add(member);
                        }
                        Log.i("Navigation Array", "turnType = " + properties.getTurnType());

                        //이전 LineString중 TurnType정보가 없는 객체에 현재의 TurnType정보를 저장
                        for (int j = naviMembers.size() - 1; j >= 0; j--) {
                            NaviMember temp = naviMembers.get(j);
                            if (temp.getTurnType() == 0) {
                                temp.setTurnType(properties.getTurnType());
                                naviMembers.add(j, temp);
                            } else {
                                break;
                            }
                        }

                        //Log.e("NaviDescription", member.description);
                    } else if (geometry.getType().equals("LineString")) {
                        List<Object> list = geometry.getCoordinates();
                        for (int k = 0; k < list.size(); k++) { // k
                            String[] lit = String.valueOf(list.get(k)).split(" ");
                            Double lineX = Double.parseDouble(lit[0].substring(1, lit[0].length() - 1));
                            Double lineY = Double.parseDouble(lit[1].substring(0, lit[1].length() - 1));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lineY, lineX));
                            member.setPoint(new PointDouble(lineX, lineY));
                        }
                        member.setDescription(properties.getDescription());
                        member.setDistance(properties.getDistance());
                        member.setTime(properties.getTime());
                        member.setTurnType(0);
                        Log.i("Navigation Array", "description = " + member.getDescription()
                                + " Distance = " + member.getDistance() + " time = " + member.getTime());
                        naviMembers.add(member);
                    }
                    member = new NaviMember();
                }
                polyline.addPoint(
                        MapPoint.mapPointWithGeoCoord(Double.parseDouble(endY), Double.parseDouble(endX)));
                mapNaviView.addPolyline(polyline);
                double distanceKiloMeter =
                        distance(Double.valueOf(startY), Double.valueOf(startX),
                                naviMembers.get(0).getPoint().y, naviMembers.get(0).getPoint().x, "meter");
                mBinding.txtNaviMeter.setText((int) distanceKiloMeter + "m");
                mBinding.txtNaviMap.setText(naviMembers.get(0).description
                        + " 턴타입 : " + naviMembers.get(0).getTrunTypeByText());

                //2019.09.30 추가
                raspDescription =naviMembers.get(0).description;
                raspTurnTypeStr = naviMembers.get(0).getTrunTypeByText();
                raspTurnType = naviMembers.get(0).getTurnType();
                raspDistance = (int) distanceKiloMeter;
                send2rasp();

                totalDistance[0] = repo.getFeatures().get(0).getProperties().getTotalDistance();

                disList.add(0, String.valueOf(totalDistance[0])); // 총 거리 반환
                disList.add(1, startY);
                disList.add(2, startX);

                Log.d("테스트 : LAT", String.valueOf(startY));
                Log.d("테스트 : LONG", String.valueOf(startX));

                MapPoint markerPointStart = MapPoint.mapPointWithGeoCoord(
                        Double.parseDouble(startX), Double.parseDouble(startY));

                MapPoint markerPointEnd = MapPoint.mapPointWithGeoCoord(
                        Double.parseDouble(endX), Double.parseDouble(endY));


                MapPOIItem marker = new MapPOIItem(); // 마커 생성
                marker.setItemName("출발지");
                marker.setTag(0);
                marker.setMapPoint(markerPointStart);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                MapPOIItem marker2 = new MapPOIItem(); // 마커 생성
                marker2.setItemName("도착지");
                marker2.setTag(1);
                marker2.setMapPoint(markerPointEnd);
                marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                mapNaviView.addPOIItem(marker);
                mapNaviView.addPOIItem(marker2);

                mapNaviView.moveCamera(
                        CameraUpdateFactory.newMapPoint(markerPointStart, -1));
                mapNaviView.zoomIn(true);

                mapNaviView.setCurrentLocationTrackingMode(
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                mapNaviView.setCurrentLocationEventListener(MapNavigationActivity.this);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MapFindRepoResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("TEST : ", t.getMessage());
                totalDistance[0] = 0;
            }
        });
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        int naviMemberSize = naviMembers.size();
        // 킬로미터(Kilo Meter) 단위
        double distanceKiloMeter =
                distance(mapPointGeo.latitude, mapPointGeo.longitude,
                        naviMembers.get(naviCount).getPoint().y,
                        naviMembers.get(naviCount).getPoint().x, "meter");

        double distanceKiloMeter2 =
                distance(mapPointGeo.latitude, mapPointGeo.longitude,
                        naviMembers.get(naviMemberSize - 1).getPoint().y,
                        naviMembers.get(naviMemberSize - 1).getPoint().x, "meter");

        mBinding.txtNaviMeter.setText((int) distanceKiloMeter + "m");

        raspDistance = (int) distanceKiloMeter;
        send2rasp();


        if (distanceKiloMeter2 <= 1000)
            mBinding.txtNaviDistance.setText("남은거리 : " + (int) distanceKiloMeter2 + "m");
        else
            mBinding.txtNaviDistance.setText("남은거리 : " + (int) (distanceKiloMeter2 / 1000 * 10) / 10.0 + "km");

        if (distanceKiloMeter <= 9.0) {
            ++naviCount;

            if (naviMembers.get(naviCount).getDescription().equals("도착")) {
                Toast.makeText(getApplicationContext(), "도착했습니다. 안내를 종료합니다.", Toast.LENGTH_LONG).show();
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("도착했습니다. 안내를 종료합니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();*/
            } else {
                mBinding.txtNaviMap.setText("다음 " + naviMembers.get(naviCount).getDescription() + "/" +
                        naviMembers.get(naviCount).getTrunTypeByText());
                raspDescription = naviMembers.get(naviCount).getDescription();
                raspTurnType = naviMembers.get(naviCount).getTurnType();
                raspTurnTypeStr = naviMembers.get(naviCount).getTrunTypeByText();
                send2rasp();
            }
        }
        disList.add(3, String.valueOf(mapPointGeo.longitude));
        disList.add(4, String.valueOf(mapPointGeo.latitude));

        Log.d("currentDistance", String.valueOf(distanceKiloMeter));
        Log.d("테스트 : LAT", String.valueOf(mapPointGeo.latitude));
        Log.d("테스트 : LONG", String.valueOf(mapPointGeo.longitude));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapNavigationActivity.this);
        alertDialog.setTitle("네비게이션 종료");
        alertDialog.setMessage("네비게이션을 종료하시겠습니까?");
        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();

                Double myDistance = distance(
                        Double.valueOf(disList.get(4)), //start Y
                        Double.valueOf(disList.get(3)), //start X
                        Double.valueOf(disList.get(1)), //현재위치 Y
                        Double.valueOf(disList.get(2)), "kilometer"); // 현재위치 X

                Log.e("테스트", disList.get(3));
                Log.e("테스트", disList.get(4));
                Log.e("테스트", disList.get(1));
                Log.e("테스트", disList.get(2));

                final Double dis = Math.round(myDistance * 10) / 10.0;

                long currentTime = System.currentTimeMillis();
                final Date date = new Date(currentTime);
                Date rightNow = new Date(currentTime); //단일 주행기록
                Date month = new Date(currentTime);

                SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
                SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
                SimpleDateFormat sdfMin = new SimpleDateFormat("mm");
                SimpleDateFormat sdfSec = new SimpleDateFormat("ss");
                SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd");

                //나중에 사용
                final String today = todayFormat.format(date);
                String thisYear = sdfYear.format(date);
                String thisMonth = sdfMonth.format(date);
                String thisDay = sdfDay.format(date);
                String thisHour = sdfHour.format(date);
                String thisMin = sdfMin.format(date);
                String thisSec = sdfSec.format(date);
                final String now = thisYear + thisMonth + thisDay + thisHour;
                String rightNowStr = thisMonth + "-" + thisDay + "-" + thisYear + " " + thisHour + ":" + thisMin + ":" + thisSec; //단일 주행기록
                String dailyTotalStr = thisMonth + "-" + thisDay + "-" + thisYear + " 09:00:00"; //누적 주행기록
                String monthStr = thisMonth + "-01-" + thisYear + " 00:00:00";
                Log.e("right now : ", rightNowStr);
                Log.e("total : ", dailyTotalStr);
                DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                try {
                    rightNow = (Date) format.parse(rightNowStr); //단일
                    total = (Date) format.parse(dailyTotalStr); //누적
                    month = (Date) format.parse(monthStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long output = rightNow.getTime() / 1000L; //하루 단일 주행기록
                String str = Long.toString(output);
                final long nowTimestamp = Long.parseLong(str) * 1000;

                long totalOutPut = total.getTime() / 1000L; //누적 주행기록
                String totalStr = Long.toString(totalOutPut);
                final long totalTimestamp = Long.parseLong(totalStr) * 1000;

                long monthOutput = month.getTime() / 1000L;
                String monthString = Long.toString(monthOutput);
                final long monthTimestamp = Long.parseLong(monthString) * 1000;

                Log.e("rightNow? : ", String.valueOf(nowTimestamp));
                Log.e("누적 주행기록 : ", String.valueOf(totalTimestamp));
                Log.d("한달 누적기록 : ", String.valueOf(monthTimestamp));

                final String route = "dailyData/data/" + nowTimestamp + "/";

                final boolean[] uploadCheck = {false};
                if (myDistance > 0.0) {


                    /*SharedPreferences pref = getSharedPreferences("com.el.ariby_joining", MODE_PRIVATE);
                    int weight = pref.getInt("weight", 0);*/
                    String riding = getMin(time);
                    double returnTime = Double.parseDouble(riding);
                    Log.d("ridingTime : ", getMin(time));
                    Log.d("weight : ", String.valueOf(weight));
                    int inputWeight = Integer.parseInt(weight);

                    kcal =(3.0 * (3.5 * inputWeight * returnTime))*5;
                    Log.e("kcal : ", String.valueOf(kcal));

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                String uid = dataSnapshot1.getKey();
                                Log.d("uid : ", uid);
                                if (myUid.equals(uid)) { //해당 uid로 기존 기록이 있으면
                                    try { //누적기록 업데이트
                                        String preDis = dataSnapshot1.child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalDis").getValue().toString();
                                        String preTime = dataSnapshot1.child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalTime").getValue().toString();
                                        String monthPreDis = dataSnapshot1.child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyDis").getValue().toString();
                                        String monthPreTime = dataSnapshot1.child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyTime").getValue().toString();

                                        Log.e("preDis : ", preDis);
                                        if (preDis != null && monthPreDis != null) {
                                            //dailyData
                                            Double updateDis = Double.parseDouble(preDis) + dis;

                                            String[] times = preTime.split(":");
                                            String[] inputTime = getTime(time).split(":");

                                            int[] timeInt = new int[times.length];
                                            int[] inputTimeInt = new int[inputTime.length];
                                            for (int i = 0; i < times.length; i++) {
                                                timeInt[i] = Integer.parseInt(times[i]);
                                                inputTimeInt[i] = Integer.parseInt(inputTime[i]);
                                                timeInt[i] += inputTimeInt[i];
                                            }

                                            for (int z = 2; z >= 0; z--) {
                                                if (timeInt[z] >= 60) {
                                                    timeInt[z - 1] += timeInt[z] / 60;
                                                    timeInt[z] = timeInt[z] % 60;
                                                }
                                            }
                                            Log.d("timeInt", String.valueOf(timeInt[0]) + "시, " + timeInt[1] + ", " + timeInt[2]);
                                            String updateTime = timeInt[0] + ":" + timeInt[1] + ":" + timeInt[2];

                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalDis").setValue(updateDis);
                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalTime").setValue(updateTime);

                                            //monthlyData
                                            Double updateMonthDis = Double.parseDouble(monthPreDis) + dis;
                                            String[] monthTimes = monthPreTime.split(":");
                                            String[] inputMonthTime = getTime(time).split(":");
                                            int[] monthTimeInt = new int[monthTimes.length];
                                            int[] inputMonthTimeInt = new int[inputMonthTime.length];
                                            for (int i = 0; i < monthTimes.length; i++) {
                                                monthTimeInt[i] = Integer.parseInt(monthTimes[i]);
                                                inputMonthTimeInt[i] = Integer.parseInt(inputMonthTime[i]);
                                                monthTimeInt[i] += inputMonthTimeInt[i];
                                            }

                                            for (int z = 2; z >= 0; z--) {
                                                if (monthTimeInt[z] > 60) {
                                                    monthTimeInt[z - 1] += monthTimeInt[z] / 60;
                                                    monthTimeInt[z] = monthTimeInt[z] % 60;
                                                }
                                            }
                                            String updateMonthTime = monthTimeInt[0] + ":" + monthTimeInt[1] + ":" + monthTimeInt[2];
                                            ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyDis").setValue(updateMonthDis);
                                            ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyTime").setValue(updateMonthTime);

                                        } else { //당일 누적기록이 없으면
                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalTime").setValue(getTime(time));
                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalDis").setValue(dis);
                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("date").setValue(today);
                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("daily_dis_rank").setValue(0);
                                            ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("daily_time_rank").setValue(0);
                                            ref.child(myUid).child("dailyData").child("upDown").child("upDownDis").setValue(0);
                                            ref.child(myUid).child("dailyData").child("upDown").child("upDownTime").setValue(0);

                                            //유저 인포
                                            ref.child(myUid).child("userInfo").child("nickname").setValue(nickname);
                                            ref.child(myUid).child("userInfo").child("profile").setValue(userProfile);
                                            Log.e("nickname", nickname + ", " + userProfile);
                                            ref.child(myUid).child("userInfo").child("upDownImg").setValue(0);
                                            ref.child(myUid).child("userInfo").child("upDownTxt").setValue(0);

                                            //monthly 생성
                                            ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyDis").setValue(dis);
                                            ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyTime").setValue(getTime(time));
                                            ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("month_dis_rank").setValue(0);
                                            ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("month_time_rank").setValue(0);
                                            ref.child(myUid).child("monthlyData").child("upDown").child("upDownDis").setValue(0);
                                            ref.child(myUid).child("monthlyData").child("upDown").child("upDownTime").setValue(0);

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    uploadCheck[0] = true;
                                    break;
                                } else { //해당 uid로 기존 기록이 없으면 userInfo도 업로드

                                    Log.e("기존 uid 기록 없음 : ", "none");
                                    //누적기록
                                    ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalTime").setValue(getTime(time));
                                    ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("dailyTotalDis").setValue(dis);
                                    ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("date").setValue(today);
                                    ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("daily_dis_rank").setValue(0);
                                    ref.child(myUid).child("dailyData").child(String.valueOf(totalTimestamp)).child("daily_time_rank").setValue(0);
                                    ref.child(myUid).child("dailyData").child("upDown").child("upDownDis").setValue(0);
                                    ref.child(myUid).child("dailyData").child("upDown").child("upDownTime").setValue(0);

                                    //유저 인포
                                    ref.child(myUid).child("userInfo").child("nickname").setValue(nickname);
                                    ref.child(myUid).child("userInfo").child("profile").setValue(userProfile);
                                    ref.child(myUid).child("userInfo").child("upDownImg").setValue(0);
                                    ref.child(myUid).child("userInfo").child("upDownTxt").setValue(0);

                                    //monthly 생성
                                    ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyDis").setValue(dis);
                                    ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("monthlyTime").setValue(getTime(time));
                                    ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("month_dis_rank").setValue(0);
                                    ref.child(myUid).child("monthlyData").child(String.valueOf(monthTimestamp)).child("month_time_rank").setValue(0);
                                    ref.child(myUid).child("monthlyData").child("upDown").child("upDownDis").setValue(0);
                                    ref.child(myUid).child("monthlyData").child("upDown").child("upDownTime").setValue(0);

                                }

                            }

                            //유저 인포
                            ref.child(myUid).child("userInfo").child("nickname").setValue(nickname);
                            ref.child(myUid).child("userInfo").child("profile").setValue(userProfile);

                            //단일기록 저장
                            ref.child(myUid).child("dailyData").child("data").child(String.valueOf(nowTimestamp)).child("distance").setValue(dis);
                            ref.child(myUid).child("dailyData").child("data").child(String.valueOf(nowTimestamp)).child("time").setValue(getTime(time));
                            ref.child(myUid).child("dailyData").child("data").child(String.valueOf(nowTimestamp)).child("date").setValue(today);
                            ref.child(myUid).child("dailyData").child("data").child(String.valueOf(nowTimestamp)).child("kcal").setValue(kcal);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                } else {
                    Toast.makeText(getApplicationContext()
                            , "주행기록이 0km 입니다.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("아니오", null);
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public String getTime(long sec) {
        long min, hour;
        min = sec / 60;
        hour = min / 60;
        sec = sec % 60;
        min = min % 60;
        return hour + ":" + min + ":" + sec;
    }

    public String getMin(long sec){
        long min;
        min = sec/60;
        return String.valueOf(min);
    }
}

class NaviMember {
    String description;
    PointDouble point;
    int distance;
    int time;
    int turnType = 0;

    public int getTurnType() {
        return turnType;
    }

    public void setTurnType(int turnType) {
        this.turnType = turnType;
    }

    public String getTrunTypeByText() {
        switch (turnType) {
            case 0:
                return "초기화 오류";
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                return "안내 없음 ";
            case 11:
                return "직진";
            case 12:
                return "좌회전";
            case 13:
                return "우회전";
            case 14:
                return "U-turn";
            case 16:
                return "8시 방향 좌회전";
            case 17:
                return "10시 방향 좌회전";
            case 18:
                return "2시 방향 우회전";
            case 19:
                return "4시 방향 우회전";
            case 184:
                return "경유지";
            case 185:
                return "첫번째 경유지";
            case 186:
                return "두번째 경유지";
            case 187:
                return "세번째 경유지";
            case 188:
                return "네번째 경유지";
            case 189:
                return "다섯번째 경유지";
            case 125:
                return "육교";
            case 126:
                return "지하보도";
            case 127:
                return "계단 진입";
            case 128:
                return "경사로 진입";
            case 129:
                return "계단+경사로 진입";
            case 200:
                return "출발지";
            case 201:
                return "목적지";
            case 211:
                return "횡단보도 ";
            case 212:
                return "좌측 횡단보도";
            case 213:
                return "우측 횡단보도";
            case 214:
                return "8시 방향 횡단보도";
            case 215:
                return "10시 방향 횡단보도";
            case 216:
                return "2시 방향 횡단보도";
            case 217:
                return "4시 방향 횡단보도";
            case 218:
                return "엘리베이터";
            case 233:
                return "직진 임시";
            default:
                return "알수없는 코드";
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public PointDouble getPoint() {
        return point;
    }

    public void setPoint(PointDouble point) {
        this.point = point;
    }
}