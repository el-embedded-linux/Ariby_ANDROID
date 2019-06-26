package com.el.ariby.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.el.ariby.R;
import com.el.ariby.ui.main.menu.navigation.MapInputActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    MapView mapView;
    FloatingActionButton fab;
    ImageView img_navi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        img_navi = findViewById(R.id.img_navi);
        fab = findViewById(R.id.fab_focus);

        mapView = new MapView(this);
        img_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapInputActivity.class);
                startActivity(intent);
            }
        });
        ArrayList<Double> list = startLocationService();//list로 현재 위치 좌표를 받아옴.
        MapPoint markerPoint = MapPoint.mapPointWithGeoCoord(list.get(0), list.get(1));

        MapPOIItem marker = new MapPOIItem(); // 마커 생성
        marker.setItemName("Dafault Market");
        marker.setTag(0);
        marker.setMapPoint(markerPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.addPOIItem(marker);
        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(list.get(0), list.get(1)), true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                } else if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                } else if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                }
            }
        });

    }

    private ArrayList<Double> startLocationService() {
        GPSListener gpsListener = new GPSListener();
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

        String msg = "Last Known Location -> Latitude : " +
                location.getLatitude() +
                "\nLongitude : " + location.getLongitude();
        Log.i("SampleLocation ", msg);

        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, gpsListener);

        return list;
    }


    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
            Log.i("GPSListener", msg);
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
}
