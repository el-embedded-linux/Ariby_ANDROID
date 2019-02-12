package com.el.ariby.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiProvider {

    public static OpenWeatherApiService provideWeatherApi() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApiService.class);
    }
}
