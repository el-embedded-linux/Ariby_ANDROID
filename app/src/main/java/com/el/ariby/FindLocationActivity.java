package com.el.ariby;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.el.ariby.ui.api.CoordApi;
import com.el.ariby.ui.api.MapFindApi;
import com.el.ariby.ui.api.response.CoordRepoResponse;
import com.el.ariby.ui.api.response.MapFindRepoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindLocationActivity extends AppCompatActivity {
    MapView mapView;
    FloatingActionButton fab;
    String startX;
    String startY;
    String endX;
    String endY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        fab = findViewById(R.id.fab_focus);
        mapView = new MapView(this);
        Intent intent=getIntent();
        startX=intent.getStringExtra("startX");
        startY=intent.getStringExtra("startY");
        endX=intent.getStringExtra("endX");
        endY=intent.getStringExtra("endY");
        Log.d("Map : ", startX+startY+endX+endY);
        getMapFind(startY,startX,endY,endX);
        //ArrayList<Double> list = startLocationService();//list로 현재 위치 좌표를 받아옴.
        //MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(list.get(0), list.get(1));

        MapPOIItem marker = new MapPOIItem(); // 마커 생성
        marker.setItemName("Dafault Market");
        marker.setTag(0);
        //marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.addPOIItem(marker);
        mapView.setHDMapTileEnabled(true); // HD 타일 사용여부
        mapView.setMapTilePersistentCacheEnabled(true);//다운한 지도 데이터를 단말의 영구 캐쉬 영역에 저장하는 기능

        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(list.get(0), list.get(1)), true);
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
    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            // 디버그 버전은 로그 보여주고
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            // 출시된 앱은 로그를 가리고
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    private void getMapFind(String startX, String startY, String endX, String endY) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(MapFindApi.BASEURL)
                .client(createOkHttpClient())
                .build();

        MapFindApi apiService = retrofit.create(MapFindApi.class);
        Call<MapFindRepoResponse> call =
                apiService.getMapFind("d7673b71-bc89-416a-9ac6-019e5d8f327a","1",startX,startY,endX,endY,"123","234");

        call.enqueue(new Callback<MapFindRepoResponse>() {
            @Override
            public void onResponse(Call<MapFindRepoResponse> call,
                                   Response<MapFindRepoResponse> response) {
                if (response.isSuccessful()) {
                    MapFindRepoResponse repo = response.body();
                    Log.d("길안내 : ", Integer.toString(repo.getFeatures().get(0).getProperties().getTotalDistance()));
                    Toast.makeText(getApplicationContext(),Integer.toString(repo.getFeatures().get(0).getProperties().getTotalDistance()),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MapFindRepoResponse> call, Throwable t) {

            }
        });
    }
}
