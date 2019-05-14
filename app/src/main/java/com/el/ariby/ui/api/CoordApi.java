package com.el.ariby.ui.api;

import com.el.ariby.ui.api.response.CoordRepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CoordApi {
    String BASEURL = "https://dapi.kakao.com/";

    @GET("/v2/local/geo/transcoord.json")
    Call<CoordRepoResponse> getCoord(@Header("Authorization") String key,
                                     @Query("x") String x,
                                     @Query("y") String y,
                                     @Query("input_coord") String input_coord,
                                     @Query("output_coord") String output_coord);
}
