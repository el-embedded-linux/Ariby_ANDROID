package com.el.ariby.ui.api;

import com.el.ariby.ui.api.response.GeoRepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GeoApi {
    String BASEURL = "https://dapi.kakao.com/";
    @GET("v2/local/geo/coord2address.json")
    Call<GeoRepoResponse> getGeo(@Header("Authorization") String key,
                                @Query("x") String x,
                                @Query("y") String y,
                                @Query("input_coord") String cord);
}
