package com.example.brahma.coolweather.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 10750 on 2017/8/8.
 */

public class Now {
    @SerializedName("tmp")
    private String temperature;
    @SerializedName("cond")
    private More more;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public More getMore() {
        return more;
    }

    public void setMore(More more) {
        this.more = more;
    }

    public class More {
        @SerializedName("txt")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
