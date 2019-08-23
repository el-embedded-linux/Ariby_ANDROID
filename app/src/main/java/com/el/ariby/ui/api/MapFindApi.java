package com.el.ariby.ui.api;

import com.el.ariby.ui.api.response.MapFindRepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MapFindApi {
    String BASEURL = "https://api2.sktelecom.com/";

    @GET("/tmap/routes/pedestrian")
    Call<MapFindRepoResponse> getMapFind(@Header("appKey") String key,
                                         @Query("version") String version,
                                         @Query("startX") String startX,
                                         @Query("startY") String startY,
                                         @Query("endX") String endX,
                                         @Query("endY") String endY,
                                         @Query("startName") String startName,
                                         @Query("endName") String endName);

    Call<MapFindRepoResponse> getMapFind2(@Header("appKey") String key,
                                         @Query("version") String version,
                                         @Query("startX") String startX,
                                         @Query("startY") String startY,
                                         @Query("endX") String endX,
                                         @Query("endY") String endY,
                                         @Query("startName") String startName,
                                         @Query("endName") String endName,
                                          @Query("passList") String passList);
}
