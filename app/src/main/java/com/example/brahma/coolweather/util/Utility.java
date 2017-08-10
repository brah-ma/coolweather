package com.example.brahma.coolweather.util;

import android.text.TextUtils;

import com.example.brahma.coolweather.db.City;
import com.example.brahma.coolweather.db.County;
import com.example.brahma.coolweather.db.Province;
import com.example.brahma.coolweather.weather.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10750 on 2017/8/1.
 */

public class Utility {

    public static List<Province> handleProvinceResponse(String response){
        List<Province> provinceList=new ArrayList<>();
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceJson=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setName(provinceJson.getString("name"));
                    province.setProvinceId(provinceJson.getInt("id"));
                    province.save();
                    provinceList.add(province);
                }
                return provinceList;
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static List<City> handleCityResponse(String response,int provinceId){
        List<City> cityList=new ArrayList<>();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityJson=allCities.getJSONObject(i);
                    City city=new City();
                    city.setName(cityJson.getString("name"));
                    city.setCityId(cityJson.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                    cityList.add(city);
                }
                return cityList;
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static List<County> handleCountyResponse(String response,int cityId){
        List<County> countyList=new ArrayList<>();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyJson=allCounties.getJSONObject(i);
                    County county=new County();
                    county.setName(countyJson.getString("name"));
                    county.setWeatherId(countyJson.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                    countyList.add(county);
                }
                return countyList;
            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static Weather handleWeatherResponse(String weatherTxt){
        try{
            JSONObject jsonObject=new JSONObject(weatherTxt);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (JSONException e){
            return null;
        }

    }

}
