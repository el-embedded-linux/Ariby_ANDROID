package com.el.ariby.ui.main.menu.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityMapNavigationBinding;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;
import com.el.ariby.ui.main.menu.groupRiding.groupRidingMap.MemberListItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_navigation);


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

        getMapFind(startY, startX, endY, endX);

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

        progressDialog.show();
        try {
            Thread.sleep(2000);
            mapNaviView.setCurrentLocationTrackingMode(
                    MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            mapNaviView.setCurrentLocationEventListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Log.d("timer : ", time + "");
                time++;
            }
        };

        timer.schedule(tt, 0, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();

        userRef = database.getReference("USER").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nickname = dataSnapshot.child("nickname").getValue().toString();
                String userImg = dataSnapshot.child("userImageURL").getValue().toString();

                //현재 날짜/시간 가져오기
                long currentTime = System.currentTimeMillis();
                final Date date = new Date(currentTime);
                Date month = new Date(currentTime);
                SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");

                //나중에 사용
                String thisYear = sdfYear.format(date);
                String thisMonth = sdfMonth.format(date);
                String monthCheckStr = thisMonth + "-01-" + thisYear + " 00:00:00";

                DateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                try {
                    month = format.parse(monthCheckStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long monthOutput = month.getTime() / 1000L;
                String monthStr = Long.toString(monthOutput);
                final long monthTimestamp = Long.parseLong(monthStr) * 1000;

                Log.e("스탬프", String.valueOf(monthTimestamp));

                SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");
                String totalTime = format2.format(new Date(time));

                Log.e("테스트:걸린시간 = ", totalTime);

                ref.child(firebaseUser.getUid())
                        .child("userInfo")
                        .setValue(new MemberListItem(userImg, nickname));

                ref.child(firebaseUser.getUid())
                        .child("dailyData")
                        .child(String.valueOf(monthTimestamp))
                        .child("dailyTotalTime")
                        .setValue(getTime(time));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getMapFind(final String startX, final String startY,
                            final String endX, final String endY) {
        Retrofit retrofit = SelfCall.createRetrofit(MapFindApi.BASEURL);
        MapFindApi apiService = retrofit.create(MapFindApi.class);
        Call<MapFindRepoResponse> call =
                apiService.getMapFind("d7673b71-bc89-416a-9ac6-019e5d8f327a", "1",
                        startX, startY, endX, endY, "123", "234");

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
                        + "턴타입 : " + naviMembers.get(0).getTurnType());
            }

            @Override
            public void onFailure(Call<MapFindRepoResponse> call, Throwable t) {
                Log.d("TEST : ", t.getMessage());
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

        if (distanceKiloMeter2 <= 1000)
            mBinding.txtNaviDistance.setText("남은거리 : " + (int) distanceKiloMeter + "m");
        else
            mBinding.txtNaviDistance.setText("남은거리 : " + (int) (distanceKiloMeter2 / 1000 * 10) / 10.0 + "km");

        if (distanceKiloMeter <= 3.0) {
            ++naviCount;

            if (naviMembers.get(naviCount).getDescription().equals("도착")) {
                Toast.makeText(getApplicationContext(), "도착", Toast.LENGTH_LONG).show();
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
            }
        }

        Log.d("currentDistance", String.valueOf(distanceKiloMeter));
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