package com.el.ariby.ui.main.menu.navigation;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.el.ariby.R;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.SelfCall;
import com.el.ariby.ui.api.response.MapFindRepoResponse;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapFindLocationActivity extends AppCompatActivity {
    MapView mapView;
    FloatingActionButton fab;
    String startX;
    String startY;
    String endX;
    String endY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_find_location);

        fab = findViewById(R.id.fab_focus);
        mapView = new MapView(this);

        Intent intent=getIntent();
        startX=intent.getStringExtra("startX");
        startY=intent.getStringExtra("startY");
        endX=intent.getStringExtra("endX");
        endY=intent.getStringExtra("endY");

        getMapFind(startY,startX,endY,endX);
        MapPoint marketPoint = MapPoint.mapPointWithGeoCoord(
                Double.parseDouble(startX), Double.parseDouble(startY));

        mapView.setMapCenterPoint(marketPoint, true);
        MapPOIItem marker = new MapPOIItem(); // 마커 생성
        marker.setItemName("Dafault Market");
        marker.setTag(0);
        marker.setMapPoint(marketPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.addPOIItem(marker);
        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                } else if(mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                } else if(mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading) {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                }
            }
        });
    }

    private void getMapFind(String startX, String startY, String endX, String endY) {
        Retrofit retrofit = SelfCall.createRetrofit(MapFindApi.BASEURL);

        MapFindApi apiService = retrofit.create(MapFindApi.class);
        Call<MapFindRepoResponse> call =
                apiService.getMapFind("d7673b71-bc89-416a-9ac6-019e5d8f327a","1",
                        startX,startY,endX,endY,"123","234");

        call.enqueue(new Callback<MapFindRepoResponse>() {
            @Override
            public void onResponse(Call<MapFindRepoResponse> call,
                                   Response<MapFindRepoResponse> response) {
                    MapFindRepoResponse repo = response.body();
            }

            @Override
            public void onFailure(Call<MapFindRepoResponse> call, Throwable t) {
                Log.d("TEST : ",t.getMessage());
            }
        });
    }
}
