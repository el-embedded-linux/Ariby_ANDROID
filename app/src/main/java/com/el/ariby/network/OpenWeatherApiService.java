package com.el.ariby.network;

import com.el.ariby.model.OpenWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApiService {

    @GET("data/2.5/forecast")
    Call<OpenWeather.WeatherResult> getForecast(@Query("id") int id,
                                                @Query("appid") String appId);
}
