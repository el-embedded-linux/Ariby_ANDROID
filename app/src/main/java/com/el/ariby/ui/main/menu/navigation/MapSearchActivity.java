package com.el.ariby.ui.main.menu.navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.el.ariby.R;
import com.el.ariby.databinding.ActivityMapSearchBinding;
import com.el.ariby.ui.main.menu.groupRiding.CreateGroupActivity;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapTapi;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 출발지, 도착지 검색 액티비티
 */
public class MapSearchActivity extends AppCompatActivity implements
        MapSearchAdapter.MapDataOnClickListener {

    MapSearchAdapter mAdapter;
    ArrayList<MapData> mArrayList;
    ArrayList<MapData> mArrayListTest;

    ArrayList<MapData> test;

    ActivityMapSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_search);

        mArrayList = new ArrayList<>();
        mArrayListTest = new ArrayList<>();

        test = new ArrayList<>();

        mAdapter = new MapSearchAdapter(this);
        binding.myRecyclerView.setAdapter(mAdapter);

        TMapTapi tMapTapi = new TMapTapi(this);
        tMapTapi.setSKTMapAuthentication("d7673b71-bc89-416a-9ac6-019e5d8f327a");
        final TMapData tmapdata = new TMapData();

        Disposable map = RxTextView.textChangeEvents(binding.etMapName)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(text -> text.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text ->
                        tmapdata.findAllPOI(text, item -> {
                            test.clear();
                            for (int i = 0; i < item.size(); i++) {
                                TMapPOIItem item2 = item.get(i);
                                test.add(new MapData(item2.getPOIName(), item2.getPOIPoint().toString()));
                            }

                            runOnUiThread(() ->
                                    mAdapter.replaceAll(test));
                        }));

        binding.btnLocation.setOnClickListener(v -> {
            ArrayList<Double> list = startLocationService();
            Intent intent = getIntent();
            intent.putExtra("X", Double.toString(list.get(0)));
            intent.putExtra("Y", Double.toString(list.get(1)));
            setResult(MapInputActivity.CODE_MAP_CURRENT_SEARCH, intent);
            setResult(CreateGroupActivity.CODE_MAP_CURRENT_SEARCH, intent);
            finish();
        });

        binding.btnBack.setOnClickListener(v -> finish());

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 키보드 띄우기
        binding.etMapName.requestFocus();
        InputMethodManager inputMethodManager =
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        inputMethodManager.showSoftInput(binding.etMapName, InputMethodManager.SHOW_IMPLICIT);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private ArrayList<Double> startLocationService() {
        MapSearchActivity.GPSListener gpsListener = new MapSearchActivity.GPSListener();
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

    /**
     * MapSearchAdapter 리스너 구현
     *
     * @param mapData MapData
     */
    @Override
    public void onClickAdapterItem(MapData mapData) {

        Intent intent = getIntent();
        String[] str = mapData.lat.split(" ");

        intent.putExtra("result_msg", mapData.mapName);
        intent.putExtra("X", str[1]);
        intent.putExtra("Y", str[3]);
        setResult(RESULT_OK, intent);
        Toast.makeText(MapSearchActivity.this, mapData.mapName, Toast.LENGTH_SHORT).show();
        finish();
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
