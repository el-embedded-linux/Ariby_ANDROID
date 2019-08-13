package com.el.ariby.ui.main.menu.recommend;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;
import com.el.ariby.ui.main.menu.groupRiding.groupRidingMap.Group_MapActivity;
import com.el.ariby.ui.main.menu.navigation.MapNavigationActivity;
import com.el.ariby.ui.main.menu.navigation.MapNavigationRaspberryActivity;
import com.el.ariby.ui.main.menu.navigation.PointDouble;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CourseMapActivity extends AppCompatActivity {
    MapView mapView;
    FloatingActionButton fab;
    ArrayList<PointDouble> memberPoints = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference ref;
    String courseName;
    TextView txtTakeTime;
    TextView txtTakeKilo;
    TextView txtTakeKcal;
    ImageButton imgBtnStart;
    RelativeLayout layout;
    Double kilo;

    Intent intent;
    String startX, startY, endX, endY;

    int memberTag =1;
    MapPOIItem[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_map);
        mapView = new MapView(this);
        final ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        txtTakeTime = findViewById(R.id.txt_take_time);
        txtTakeKilo = findViewById(R.id.txt_take_kilo);

        txtTakeKcal = findViewById(R.id.txt_take_kcal);
        imgBtnStart = findViewById(R.id.imgbtn_navi_start);
        layout=findViewById(R.id.relative);

        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능



        intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        startX = intent.getStringExtra("startX");
        startY = intent.getStringExtra("startY");
        endX = intent.getStringExtra("endX");
        endY = intent.getStringExtra("endY");
        Log.e("intents : ", startX+", "+startY);


        getMapFind(startX, startY, endX, endY);
        MapPoint markerPointStart = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(startY), Double.parseDouble(startX));
        Log.e("MapPoint : ", "Y: "+startY);

        MapPoint markerPointEnd = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(endY), Double.parseDouble(endX));

        mapView.setMapCenterPoint(markerPointStart, true);
        MapPOIItem startMarker = new MapPOIItem(); // 마커 생성
        startMarker.setItemName("start");
        startMarker.setTag(100); //start pin tag
        startMarker.setMapPoint(markerPointStart);
        startMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        startMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        MapPOIItem endMarker = new MapPOIItem(); // 마커 생성
        endMarker.setItemName("end");
        endMarker.setTag(101); //end pin tag
        endMarker.setMapPoint(markerPointEnd);
        endMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        endMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(startMarker);
        mapView.addPOIItem(endMarker);

//        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(coordinates.get(0), coordinates.get(1));
//        MapPOIItem myMarker = new MapPOIItem();
//        myMarker.setItemName("내 위치");
//        myMarker.setTag(0); //내 위치 pin tag : 0
//        myMarker.setMapPoint(myPoint);
//        myMarker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
//        myMarker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
//        mapView.addPOIItem(myMarker);
        imgBtnStart.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               choiceNavigation();
                                           }
                                       }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getMapFind(final String startX, final String startY, final String endX, final String endY) {
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
                polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(startY), Double.parseDouble(startX)));
                for (int i = 0; i < featuresSize; i++) {
                    String type = repo.getFeatures().get(i).getGeometry().getType();
                    PointDouble points;
                    if (type.equals("Point")) {
                        points = new PointDouble(
                                (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(0),
                                (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(1));
                        MapPoint marketPoint3 = MapPoint.mapPointWithGeoCoord(points.getY(), points.getX());
                        Log.d("marketPoint3", String.valueOf((Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(0))+"\r"+ (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(1));
                        memberPoints.add(points);
                        MapPOIItem marker3 = new MapPOIItem(); // 마커 생성
                        marker3.setItemName(repo.getFeatures().get(i).getProperties().getDescription());
                        marker3.setTag(103);
                        marker3.setMapPoint(marketPoint3);
                        marker3.setMarkerType(MapPOIItem.MarkerType.RedPin);
                        mapView.addPOIItem(marker3);

                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(points.getY(), points.getX()));

                    } else if (type.equals("LineString")) {
                        List<Object> list = repo.getFeatures().get(i).getGeometry().getCoordinates();
                        for (int k = 0; k < list.size(); k++) { // k
                            String[] lit = String.valueOf(list.get(k)).split(" ");
                            Double lineX = Double.parseDouble(lit[0].substring(1, lit[0].length() - 1));
                            Double lineY = Double.parseDouble(lit[1].substring(0, lit[1].length() - 1));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lineY, lineX));
                        }
                    }
                }
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(endY), Double.parseDouble(endX)));
                mapView.addPolyline(polyline);
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 150; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                kilo = repo.getFeatures().get(0).getProperties().
                        getTotalDistance().doubleValue()/1000;
                Double time2=(kilo/13.0)*60; // 자전거 소요시간 공식 (거리/속도)*분

                int kcal = (int)(72*0.0939*Math.round(time2)); // 평균 몸무게 62 + 자전거 무게 10 * 속도칼로리소비계수*분
                txtTakeTime.setText(Math.round(time2)+"분");
                if(kilo>=1.0) //
                    txtTakeKilo.setText(Math.round(kilo*10)/10.0+"km");
                else
                    txtTakeKilo.setText(Math.round(kilo*1000)+"m");

                txtTakeKcal.setText(kcal+"kcal");
            }

            @Override
            public void onFailure(Call<MapFindRepoResponse> call, Throwable t) {
                Log.d("TEST : ", t.getMessage());
            }
        });
    }
    private ArrayList<Double> startLocationService() {
        CourseMapActivity.GPSListener gpsListener = new CourseMapActivity.GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }

        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        ArrayList<Double> list = new ArrayList<>();
        list.add(latitude);
        list.add(longitude);

        String msg = "Group_Last Known Location -> Latitude : " +
                location.getLatitude() +
                "\nLongitude : " + location.getLongitude();
        Log.i("Group_SampleLocation ", msg);

        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, gpsListener);

        return list;
    }

    public class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
            Toast.makeText(CourseMapActivity.this, msg, Toast.LENGTH_SHORT).show();
            Log.i("Group_GPSListener11", msg);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }



    private void choiceNavigation() {
        ArrayList<String> list = new ArrayList<>();
        list.add("스마트폰");
        list.add("라즈베리파이");

        final CharSequence[] items = list.toArray(new String[list.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("디바이스 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    Intent intent = new Intent(getApplicationContext(), MapNavigationActivity.class);
                    intent.putExtra("startX",startY);
                    intent.putExtra("startY",startX);
                    intent.putExtra("endX", endY);
                    intent.putExtra("endY",endX);
                    intent.putExtra("kilo",kilo);
                    layout.removeAllViews();
                    startActivity(intent);

                } else if(which == 1) {
                    Intent intent = new Intent(getApplicationContext(), MapNavigationRaspberryActivity.class);
                    intent.putExtra("startX",startX);
                    intent.putExtra("startY",startY);
                    intent.putExtra("endX", endX);
                    intent.putExtra("endY",endY);
                    intent.putExtra("kilo",kilo);
                    layout.removeAllViews();
                    startActivity(intent);
                }

            }
        });
        builder.show();
    }
}
