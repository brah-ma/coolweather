package com.example.brahma.coolweather.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10750 on 2017/8/8.
 */

public class Basic {

    @SerializedName("city")
    private String cityName;
    @SerializedName("id")
    private String weatherId;
    private Update update;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }
}
