package com.example.brahma.coolweather.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10750 on 2017/8/8.
 */

public class AQICity {

    private String aqi;
    private String pm25;

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
}
