package com.example.brahma.coolweather.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10750 on 2017/8/8.
 */

public class Update {
    @SerializedName("loc")
    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
