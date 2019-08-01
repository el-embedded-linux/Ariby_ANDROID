package com.el.ariby.ui.main.menu.navigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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

/**
 * 지도 어플
 */
public class MapFindLocationActivity extends AppCompatActivity implements
        MapView.CurrentLocationEventListener {
    MapView mapView;
    FloatingActionButton fab;
    String startX;
    String startY;
    String endX;
    String endY;
    TextView txtTakeTime;
    TextView txtTakeKilo;
    TextView txtTakeKcal;
    ArrayList<PointDouble> naviPoints = new ArrayList<>();
    ImageButton imgBtnStart;
    RelativeLayout layout;
    Double kilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_find_location);

        fab = findViewById(R.id.fab_focus);
        txtTakeTime = findViewById(R.id.txt_take_time);
        txtTakeKilo = findViewById(R.id.txt_take_kilo);

        txtTakeKcal = findViewById(R.id.txt_take_kcal);
        imgBtnStart = findViewById(R.id.imgbtn_navi_start);
        layout=findViewById(R.id.relativeLayout);
        mapView = new MapView(this);

        Intent intent = getIntent();
        startX = intent.getStringExtra("startX");
        startY = intent.getStringExtra("startY");
        endX = intent.getStringExtra("endX");
        endY = intent.getStringExtra("endY");

        getMapFind(startY, startX, endY, endX);
        MapPoint markerPointStart = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(startX), Double.parseDouble(startY));

        MapPoint markerPointEnd = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(endX), Double.parseDouble(endY));


        mapView.setMapCenterPoint(markerPointStart, true);
        MapPOIItem marker = new MapPOIItem(); // 마커 생성
        marker.setItemName("Dafault Market");
        marker.setTag(0);
        marker.setMapPoint(markerPointStart);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        MapPOIItem marker2 = new MapPOIItem(); // 마커 생성
        marker2.setItemName("Dafault Market");
        marker2.setTag(1);
        marker2.setMapPoint(markerPointEnd);
        marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        final ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.addPOIItem(marker);
        mapView.addPOIItem(marker2);
        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능
        Log.d("size", String.valueOf(naviPoints.size()));
        mapView.setCurrentLocationEventListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *

                 AlertDialog.Builder builder = new AlertDialog.Builder(MapFindLocationActivity.this);
                 builder.setTitle("네비게이션");
                 builder.setMessage("주행을 시작하시겠습니까?");
                 builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                if (mapNaviView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                mapNaviView.setZoomLevel(-1, true);
                mapNaviView.zoomIn(true);
                mapNaviView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                }
                 }
                });
                 builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {

                }
                });
                 builder.show();
                 */
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                    mapView.setZoomLevel(-1, true);
                    mapView.zoomIn(true);

                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                } else if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                } else if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                }
            }
        });

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
                        naviPoints.add(points);
                        MapPOIItem marker3 = new MapPOIItem(); // 마커 생성
                        marker3.setItemName(repo.getFeatures().get(i).getProperties().getDescription());
                        marker3.setTag(3);
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

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        // 킬로미터(Kilo Meter) 단위
        double distanceKiloMeter =
                distance(mapPointGeo.latitude, mapPointGeo.longitude,
                        37.47356391969749, 126.89078163446104, "meter");

        Log.d("currentDistance", String.valueOf(distanceKiloMeter));
        Log.d("currentLocation",
                String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                        mapPointGeo.latitude, mapPointGeo.longitude, v));
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
                    Intent intent = new Intent(getApplicationContext(),MapNavigationActivity.class);
                    intent.putExtra("startX",startX);
                    intent.putExtra("startY",startY);
                    intent.putExtra("endX", endX);
                    intent.putExtra("endY",endY);
                    intent.putExtra("kilo",kilo);
                    layout.removeAllViews();
                    startActivity(intent);

                } else if(which == 1) {
                    Intent intent = new Intent(getApplicationContext(),MapNavigationRaspberryActivity.class);
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
