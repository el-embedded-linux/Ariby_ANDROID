package com.el.ariby;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.el.ariby.databinding.FragmentHomeBinding;
import com.el.ariby.model.OpenWeather;
import com.el.ariby.network.OpenWeatherApiService;
import com.el.ariby.network.WeatherApiProvider;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getWeatherData();
    }

    private void getWeatherData() {
        OpenWeatherApiService service = WeatherApiProvider.provideWeatherApi();

        // todo appid 에 다원이꺼로 키발급받아서 넣으면 됨 id 는 서울로 되어 있음
        service.getForecast(1835848, "e5a440f46a8dd194c22fc4a15b083799")
                .enqueue(new Callback<OpenWeather.WeatherResult>() {
                    @Override
                    public void onResponse(@NonNull Call<OpenWeather.WeatherResult> call,
                                           @NonNull Response<OpenWeather.WeatherResult> response) {

                        if (response.code() == 200 && response.body() != null) {
                            List<OpenWeather.TimeWeather> timeWeathers;
                            timeWeathers = response.body().getList();

                            // todo 여기서 timeWeathers 데이터 가지고 시간마다 뷰로 띄워주면 됨
                            String text = new Gson().toJson(timeWeathers.get(0));
                            Log.e("HomeFragment", text);
                            binding.tvText.setText(text);

                        } else {
                            Log.e("HomeFragment",
                                    "onResponse: " + response.code() + " " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<OpenWeather.WeatherResult> call,
                                          @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
