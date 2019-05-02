package com.el.ariby.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.el.ariby.R;
import com.el.ariby.BuildConfig;
import com.el.ariby.databinding.FragmentHomeBinding;
import com.el.ariby.ui.api.Api;
import com.el.ariby.ui.api.GeoApi;
import com.el.ariby.ui.api.data.CtprvnMesureLIstVo2Bean;
import com.el.ariby.ui.api.data.GeoRepoResponse;
import com.el.ariby.ui.api.data.RepoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding=FragmentHomeBinding.bind(getView());
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
        Call<GeoRepoResponse> call = apiService.getGeo("KakaoAK e880d656790ed7e10098f0742679154e",x,y,cord);
        Log.d("call : ", call.toString());

        call.enqueue(new Callback<GeoRepoResponse>() {
            @Override
            public void onResponse(Call<GeoRepoResponse> call, Response<GeoRepoResponse> response) {
                if (response.isSuccessful()) {
                    GeoRepoResponse repo = response.body();
                    mBinding.weather.append(repo.getDocuments().get(0).getAddress().getRegion_1depth_name() + " ");
                    mBinding.weather.append(repo.getDocuments().get(0).getAddress().getRegion_2depth_name() + " ");
                    mBinding.weather.append(repo.getDocuments().get(0).getAddress().getRegion_3depth_name());

                    //weather.setText(repo.getDocuments().get(0).get(0).getAddress_name());
                }
            }

            @Override
            public void onFailure(Call<GeoRepoResponse> call, Throwable t) {
                Log.d("123:", t.toString());
            }
        });
    }
    private void getWeather(int numOfRows, int pageNo, String umdName, String searchCondition, String returnType) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Api.BASEURL)
                .client(createOkHttpClient())
                .build();

        Api apiService = retrofit.create(Api.class);
        Call<RepoResponse> call = null;
        try {
            call = apiService.getWeather(
                    URLDecoder.decode("vMgzVOM7K3D3t89QY%2F%2FtYxGc7fTDhMi3AkGCqakZut7sDmQCzfeWtcT9NDRbrR4dK8OBJsR5d4QwhZkn%2FeTZ3w%3D%3D", "utf-8"),
                    numOfRows,
                    pageNo,
                    umdName,
                    searchCondition,
                    returnType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("call : ", call.toString());
        call.enqueue(new Callback<RepoResponse>() {
            @Override
            public void onResponse(Call<RepoResponse> call, Response<RepoResponse> response) {
                if (response.isSuccessful()) {
                    RepoResponse repo = response.body();
                    if (repo != null) {
                        mBinding.weather.setText("");
                        String test = "최고좋음";
                        for (CtprvnMesureLIstVo2Bean vo : repo.getList()) {
                            if (!TextUtils.isEmpty(vo.getPm10Value()) && Integer.parseInt(vo.getPm10Value()) < 30) {
                                test = "좋음";
                            } else if (!TextUtils.isEmpty(vo.getPm10Value()) && Integer.parseInt(vo.getPm10Value()) < 50) {
                                test = "양호";
                            } else if (!TextUtils.isEmpty(vo.getPm10Value()) && Integer.parseInt(vo.getPm10Value()) < 80) {
                                test = "보통";
                            }
                            mBinding.weather.append(vo.getCityName() + " " + vo.getCityNameEng() + test + "\r\n");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RepoResponse> call, Throwable t) {
                Log.d("123:", t.toString());
            }
        });
    }

    private void startLocationService() {
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String msg="Last Known Location -> Latitude : " +
                location.getLatitude() +
                "\nLongitude : " + location.getLongitude();
        Log.i("SampleLocation ", msg);

        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,minDistance,gpsListener);
        getGeo(longitude.toString(),latitude.toString(),"WGS84");
    }
    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
            Log.i("GPSListener",msg);

            mBinding.weather.append("내 위치 : " + latitude + ", " + longitude);
            Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
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
