package com.el.ariby.ui.main.menu.navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.el.ariby.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapTapi;

import java.util.ArrayList;

public class MapSearchActivity extends AppCompatActivity {
    EditText etMapName;
    RecyclerView mRecycle;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayout;
    ArrayList<MapData> mArrayList;
    ArrayList<MapData> mArrayListTest;
    Button btnLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        btnLocation=findViewById(R.id.btn_location);
        etMapName=findViewById(R.id.et_map_name);
        mRecycle=findViewById(R.id.my_recycler_view);
        mLayout = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(mLayout);
        mArrayList=new ArrayList<>();
        mArrayListTest=new ArrayList<>();
        mAdapter=new MapSearchAdapter(mArrayList);
        mRecycle.setAdapter(mAdapter);

        TMapTapi tMapTapi = new TMapTapi(this);
        tMapTapi.setSKTMapAuthentication("d7673b71-bc89-416a-9ac6-019e5d8f327a");
        final TMapData tmapdata = new TMapData();
        etMapName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() { // POI검색은 쓰레드를 사용해야 나옴.
                        String str = etMapName.getText().toString();


                        tmapdata.findAllPOI(str, new TMapData.FindAllPOIListenerCallback() {
                            @Override
                            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                                mArrayListTest.clear();
                                /*
                                 java.lang.IndexOutOfBoundsException: Inconsistency detected
                                너무 빠른 요청은 에러를 발생시키기 때문에 새로운 ArrayList를 만들어서
                                목록을 받고 기존 ArrayList에 추가시키는 방식으로 하면 해결된다.
                                 */
                                for (int i = 0; i < poiItem.size(); i++) {
                                    TMapPOIItem item = (TMapPOIItem) poiItem.get(i);
                                    //if(!(item.getPOIAddress().length()<0))
                                    mArrayListTest.add(new MapData(item.getPOIName(),item.getPOIPoint().toString()));
                                }

                            }
                        });
                    }
                });
                mArrayList.clear();
                mArrayList.addAll(mArrayListTest);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Double> list = startLocationService();
                Intent intent = getIntent();
                intent.putExtra("X",Double.toString(list.get(0)));
                intent.putExtra("Y",Double.toString(list.get(1)));
                setResult(5000,intent);
                finish();
            }
        });

    }

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
