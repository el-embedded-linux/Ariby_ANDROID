package com.el.ariby.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.el.ariby.BuildConfig;
import com.el.ariby.R;
import com.el.ariby.databinding.FragmentHomeBinding;
import com.el.ariby.ui.api.CoordApi;
import com.el.ariby.ui.api.DustApi;
import com.el.ariby.ui.api.GeoApi;
import com.el.ariby.ui.api.MeasureApi;
import com.el.ariby.ui.api.response.CoordRepoResponse;
import com.el.ariby.ui.api.response.DustRepoResponse;
import com.el.ariby.ui.api.response.GeoRepoResponse;
import com.el.ariby.ui.api.response.MeasureRepoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    String openKey = "vMgzVOM7K3D3t89QY%2F%2FtYxGc7fTDhMi3AkGC" +
            "qakZut7sDmQCzfeWtcT9NDRbrR4dK8OBJsR5d4QwhZkn%2FeTZ3w%3D%3D";
    String kakaoKey = "KakaoAK e880d656790ed7e10098f0742679154e";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentHomeBinding.bind(getView());

        startLocationService();

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

    private void getCoord(String x, String y) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(CoordApi.BASEURL)
                .client(createOkHttpClient())
                .build();

        CoordApi apiService = retrofit.create(CoordApi.class);
        Call<CoordRepoResponse> call = apiService.getCoord(kakaoKey, x, y, "WGS84", "TM");

        call.enqueue(new Callback<CoordRepoResponse>() {
            @Override
            public void onResponse(Call<CoordRepoResponse> call, Response<CoordRepoResponse> response) {
                if (response.isSuccessful()) {
                    CoordRepoResponse repo = response.body();
                    String x = Double.toString(repo.getDocuments().get(0).getX());
                    String y = Double.toString(repo.getDocuments().get(0).getY());
                    getMeasure(x, y, "json");
                }
            }

            @Override
            public void onFailure(Call<CoordRepoResponse> call, Throwable t) {

            }
        });
    }

    private void getGeo(String x, String y, String cord) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(GeoApi.BASEURL)
                .client(createOkHttpClient())
                .build();

        GeoApi apiService = retrofit.create(GeoApi.class);
        Call<GeoRepoResponse> call = apiService.getGeo(kakaoKey, x, y, cord);
        Log.d("call : ", call.toString());

        call.enqueue(new Callback<GeoRepoResponse>() {
            @Override
            public void onResponse(Call<GeoRepoResponse> call, Response<GeoRepoResponse> response) {
                if (response.isSuccessful()) {
                    GeoRepoResponse repo = response.body();
                    ArrayList<String> Region = new ArrayList<>();
                    Region.add(repo.getDocuments().get(0).getAddress().getRegion_1depth_name() + " ");
                    Region.add(repo.getDocuments().get(0).getAddress().getRegion_2depth_name() + " ");
                    Region.add(repo.getDocuments().get(0).getAddress().getRegion_3depth_name());

                    for (String name : Region) {
                        mBinding.weather.append(name);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeoRepoResponse> call, Throwable t) {
                Log.d("getGeo Error:", t.toString());
            }
        });
    }

    private void getWeather(int numOfRows, int pageNo, String stationName,
                            String dataTerm, String ver) { // 대기정보 받아옴
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(DustApi.BASEURL)
                .client(createOkHttpClient())
                .build();

        DustApi apiService = retrofit.create(DustApi.class);
        Call<DustRepoResponse> call = null;
        try {
            call = apiService.getWeather(
                    URLDecoder.decode(openKey, "utf-8"),
                    numOfRows,
                    pageNo,
                    stationName,
                    dataTerm,
                    ver,
                    "json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("call : ", call.toString());
        call.enqueue(new Callback<DustRepoResponse>() {
            @Override
            public void onResponse(Call<DustRepoResponse> call, Response<DustRepoResponse> response) {
                if (response.isSuccessful()) {
                    DustRepoResponse repo = response.body();
                    if (repo != null) {
                        mBinding.pm10.append("미세먼지 " + repo.getList().get(0).getPm10Value() + "㎍/㎥ \r\n");
                        mBinding.pm10.append("초미세먼지 " + repo.getList().get(0).getPm25Value() + "㎍/㎥ \r\n");
                        mBinding.pm10.append("이산화질소 " + repo.getList().get(0).getNo2Value() + "ppm \r\n");
                        mBinding.pm10.append("오존 " + repo.getList().get(0).getO3Value() + "ppm\r\n");
                        mBinding.pm10.append("일산화탄소 " + repo.getList().get(0).getCoValue()  + "ppm\r\n");
                        mBinding.pm10.append("아황산가스 " + repo.getList().get(0).getSo2Value() + "ppm\r\n");
                    }
                }
            }

            @Override
            public void onFailure(Call<DustRepoResponse> call, Throwable t) {
                Log.d("getWeather Error:", t.toString());
            }
        });
    }

    private void getMeasure(String x, String y, String returnType) { // 가까운 측정소 구해옴.
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(MeasureApi.BASEURL)
                .client(createOkHttpClient())
                .build();

        MeasureApi apiService = retrofit.create(MeasureApi.class);
        Call<MeasureRepoResponse> call = null;
        try {
            call = apiService.getMeasure(
                    URLDecoder.decode(openKey, "utf-8"),
                    x,
                    y,
                    returnType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("call : ", call.toString());
        call.enqueue(new Callback<MeasureRepoResponse>() {
            @Override
            public void onResponse(Call<MeasureRepoResponse> call, Response<MeasureRepoResponse> response) {
                if (response.isSuccessful()) {
                    MeasureRepoResponse repo = response.body();
                    getWeather(10, 1, repo.getList().get(0).getStationName(), "DAILY", "1.3");
                }
            }

            @Override
            public void onFailure(Call<MeasureRepoResponse> call, Throwable t) {
                Log.d("getMeasure Error:", t.toString());
            }
        });
    }

    private void startLocationService() {
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String msg = "Last Known Location -> Latitude : " +
                location.getLatitude() +
                "\nLongitude : " + location.getLongitude();
        Log.i("SampleLocation ", msg);

        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, gpsListener);
        getGeo(longitude.toString(), latitude.toString(), "WGS84");
        getCoord(longitude.toString(), latitude.toString());

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
