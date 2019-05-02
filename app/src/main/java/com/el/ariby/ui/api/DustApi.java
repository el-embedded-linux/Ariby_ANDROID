package com.el.ariby.ui.api;

import com.el.ariby.ui.api.response.DustRepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DustApi {
    String BASEURL = "http://openapi.airkorea.or.kr/";

    @GET("openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
    Call<DustRepoResponse> getWeather(@Query("serviceKey") String serviceKey,
                                      @Query("numOfRows") int numOfRows,
                                      @Query("pageNo") int pageNo,
                                      @Query("stationName") String stationName,
                                      @Query("dataTerm") String dataTerm,
                                      @Query("ver") String ver,
                                      @Query("_returnType") String _returnType);
}
