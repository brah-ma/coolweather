package com.example.brahma.coolweather.presenter;

import com.example.brahma.coolweather.util.HttpUtil;
import com.example.brahma.coolweather.util.Utility;
import com.example.brahma.coolweather.weather.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 10750 on 2017/8/8.
 */

public class WeatherPresenter implements IWeatherContact.IWeatherPresenter {

    private IWeatherContact.IWeatherView mWeatherView;

    public WeatherPresenter(IWeatherContact.IWeatherView weatherView){
        mWeatherView=weatherView;
    }

    @Override
    public void getWeatherInfo(String weatherId) {
       String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=bc0418b57b2d4918819d3974ac1285d9";//CN101020500
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                Weather weather= Utility.handleWeatherResponse(responseText);
                mWeatherView.showWeatherInfo(weather);
            }
        });
    }

    @Override
    public String getWeatherBgUrl() {
        final String requestBg="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBg, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bgUrl=response.body().string();
                mWeatherView.showWeatherBg(bgUrl);
            }
        });

        return null;
    }
}
