package com.el.ariby.ui.main.menu.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityMapNavigationBinding;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapNavigationActivity extends AppCompatActivity implements
        MapView.CurrentLocationEventListener {
    ActivityMapNavigationBinding mBinding;
    MapView mapNaviView;
    String startX, startY, endX, endY;
    ArrayList<PointDouble> naviPoints = new ArrayList<>();
    int testCount = 0;
    int naviCount = 0;
    ProgressDialog progressDialog;
    ArrayList<NaviMember> naviMembers = new ArrayList<>();

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

        Intent intent = getIntent();

        startY = intent.getStringExtra("startY");
        startX = intent.getStringExtra("startX");
        endY = intent.getStringExtra("endY");
        endX = intent.getStringExtra("endX");
        progressDialog = new ProgressDialog(MapNavigationActivity.this);
        progressDialog.setMessage("데이터를 로딩중입니다.");
        progressDialog.show();

        getMapFind(startY, startX, endY, endX);

        MapPoint markerPointStart = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(startX), Double.parseDouble(startY));

        MapPoint markerPointEnd = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(endX), Double.parseDouble(endY));


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

        mapNaviView.addPOIItem(marker);
        mapNaviView.addPOIItem(marker2);

        mapNaviView.moveCamera(
                CameraUpdateFactory.newMapPoint(markerPointStart, -1));
        mapNaviView.zoomIn(true);

        mapNaviView.setCurrentLocationTrackingMode(
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        mapNaviView.setCurrentLocationEventListener(this);

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
                /**
                 * 01
                 * 23
                 * 45
                 * 67
                 * 89
                 *
                 * NaviMember
                 *     String name;
                 *     int distance;
                 *     int time;*/

                MapFindRepoResponse repo = response.body();
                int featuresSize = repo.getFeatures().size();

                MapPolyline polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(128, 255, 51, 0));
                // Polyline 컬러 지정.
                //mBinding.txtNaviMap.setText(repo.getFeatures().get(1).getProperties().getName());
                //mBinding.txtNaviMeter.setText(repo.getFeatures().get(1).getProperties().getDistance() + "m");
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(
                        Double.parseDouble(startY), Double.parseDouble(startX)));
                NaviMember member=new NaviMember();
                for (int i = 0; i < featuresSize; i++) {
                    String type = repo.getFeatures().get(i).getGeometry().getType();
                    PointDouble point;
                    if (type.equals("Point")) {
                        point = new PointDouble(
                                (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(0),
                                (Double) repo.getFeatures().get(i).getGeometry().getCoordinates().get(1));
                        String name = repo.getFeatures().get(i).getProperties().getName();
                        if(!TextUtils.isEmpty(name))
                            member.setName(name);

                        MapPoint marketPoint3 = MapPoint.mapPointWithGeoCoord(point.getY(), point.getX());
                        naviPoints.add(point);
                        MapPOIItem marker5 = new MapPOIItem(); // 마커 생성
                        marker5.setItemName(repo.getFeatures().get(i).getProperties().getDescription());
                        marker5.setTag(4);
                        marker5.setMapPoint(marketPoint3);
                        marker5.setMarkerType(MapPOIItem.MarkerType.RedPin);

                        mapNaviView.addPOIItem(marker5);

                        polyline.addPoint(MapPoint.mapPointWithGeoCoord(point.getY(), point.getX()));

                        Log.d("testX" + i, point.getX().toString());
                        Log.d("testY" + i, point.getY().toString());
                    } else if (type.equals("LineString")) {
                        List<Object> list = repo.getFeatures().get(i).getGeometry().getCoordinates();
                        for (int k = 0; k < list.size(); k++) { // k
                            String[] lit = String.valueOf(list.get(k)).split(" ");
                            Double lineX = Double.parseDouble(lit[0].substring(1, lit[0].length() - 1));
                            Double lineY = Double.parseDouble(lit[1].substring(0, lit[1].length() - 1));
                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(lineY, lineX));
                        }
                        String name=repo.getFeatures().get(i).getProperties().getName();
                        if(!TextUtils.isEmpty(name))
                            member.setName(name);
                        member.setDistance(repo.getFeatures().get(i).getProperties().getDistance());
                        member.setTime(repo.getFeatures().get(i).getProperties().getTime());
                        naviMembers.add(member);
                    }
                }
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(endY), Double.parseDouble(endX)));
                mapNaviView.addPolyline(polyline);
                double distanceKiloMeter =
                        distance(Double.valueOf(startY), Double.valueOf(startX),
                                naviPoints.get(naviCount).y, naviPoints.get(naviCount).x, "meter");
                mBinding.txtNaviMeter.setText((int)distanceKiloMeter+"m");
                progressDialog.dismiss();
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
                        naviPoints.get(naviCount).y, naviPoints.get(naviCount).x, "meter");
        mBinding.txtNaviMeter.setText((int)distanceKiloMeter+"m");
        if (distanceKiloMeter <= 2.5) {
            ++naviCount;
            mBinding.txtNaviMap.setText(naviMembers.get(naviCount).getName());
        }

        Log.e("firstPoint1", String.valueOf(mapPointGeo.latitude));
        Log.e("firstPoint2", String.valueOf(mapPointGeo.longitude));

        Log.d("currentDistance", String.valueOf(distanceKiloMeter));
        Log.d("currentLocation",
                String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                        mapPointGeo.latitude, mapPointGeo.longitude, v));

        Toast.makeText(getApplicationContext(), String.valueOf(testCount), Toast.LENGTH_SHORT).show();
        testCount++;
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
}

class NaviMember {
    String name;
    int distance;
    int time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}