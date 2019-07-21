package com.el.ariby.ui.main.menu.groupRiding.groupRidingMap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.el.ariby.R;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;
import com.el.ariby.ui.main.menu.navigation.PointDouble;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class Group_MapActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener {
    MapView mapView;
    FloatingActionButton fab;
    ArrayList<PointDouble> memberPoints = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_map);
        mapView = new MapView(this);

        final ViewGroup mapViewContainer = findViewById(R.id.group_map_view);
        mapViewContainer.addView(mapView);

        Intent intent = getIntent();
        final String groupName = intent.getStringExtra("groupName");
        String startX = intent.getStringExtra("startY");
        String startY = intent.getStringExtra("startX");
        String endX = intent.getStringExtra("endY");
        String endY = intent.getStringExtra("endX");
        Log.e("intents : ", startX+", "+startY);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("GROUP_RIDING");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameCom = null;
                int i=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    nameCom = snapshot.getKey();
                    PointDouble points = null;
                    if(groupName.equals(nameCom)){
                        Log.e("group_activity :",nameCom);
                        i++;
                        //String nick = snapshot.child(groupName).child("members").child().getKey();
                       // Log.e("show nicknames : ", nick);
                        String getLat = snapshot.child("members").child("leader").child("lat").getValue().toString();
                        String getLon = snapshot.child("members").child("leader").child("lon").getValue().toString();
                        Log.e("getLat + getLon : ", getLat+",   "+getLon);
                        MapPoint markPoint3 = MapPoint.mapPointWithCONGCoord(Double.parseDouble(getLat), Double.parseDouble(getLon));
                        //memberPoints.add(points);
                        MapPOIItem marker2 = new MapPOIItem();
                        marker2.setItemName(String.valueOf(i));
                        marker2.setTag(3);
                        marker2.setMapPoint(markPoint3);
                        marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
                        mapView.addPOIItem(marker2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getMapFind(startX, startY, endX, endY);

        //getMapFind(startY, startY, endY, endX);
        MapPoint markerPointStart = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(startY), Double.parseDouble(startX));

        MapPoint markerPointEnd = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(endY), Double.parseDouble(endX));

        mapView.setMapCenterPoint(markerPointStart, true);
        MapPOIItem marker = new MapPOIItem(); // 마커 생성
        marker.setItemName("start");
        marker.setTag(0);
        marker.setMapPoint(markerPointStart);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        MapPOIItem marker1 = new MapPOIItem(); // 마커 생성
        marker1.setItemName("end");
        marker1.setTag(1);
        marker1.setMapPoint(markerPointEnd);
        marker1.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker1.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(marker);
        mapView.addPOIItem(marker1);
        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능
        mapView.setCurrentLocationEventListener(this);

        /*MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128,255,51,0));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(startY), Double.parseDouble(startX)));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(endY), Double.parseDouble(endX)));

        mapView.addPolyline(polyline);

        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100;
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));*/
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
                        memberPoints.add(points);
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

}
