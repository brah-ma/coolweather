package com.example.brahma.coolweather.presenter;

import com.example.brahma.coolweather.weather.Weather;

/**
 * Created by 10750 on 2017/8/8.
 */

public interface IWeatherContact {

    interface IWeatherView {
        void showWeatherInfo(Weather weather);

        void showWeatherBg(String bgUrl);
    }

    interface IWeatherPresenter {
        void getWeatherInfo(String weatherId);

        String getWeatherBgUrl();
    }

}
