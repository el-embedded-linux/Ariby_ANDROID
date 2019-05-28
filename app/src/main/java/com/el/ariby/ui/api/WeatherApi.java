package com.el.ariby.ui.api;

import com.el.ariby.ui.api.response.CoordRepoResponse;
import com.el.ariby.ui.api.response.WeatherRepoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WeatherApi {
    String BASEURL = "http://newsky2.kma.go.kr/";

    @GET("/service/SecndSrtpdFrcstInfoService2/ForecastTimeData")
    Call<WeatherRepoResponse> getWeather(@Query("serviceKey") String serviceKey,
                                         @Query("base_date") String baseDate,
                                         @Query("base_time") String baseTime,
                                         @Query("nx") String nx,
                                         @Query("ny") String ny,
                                         @Query("numOfRows") String numOfRows,
                                         @Query("pageNo") String pageNo,
                                         @Query("_type") String type);
}
