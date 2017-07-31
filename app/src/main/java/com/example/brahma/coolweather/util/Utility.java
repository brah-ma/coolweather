package com.example.brahma.coolweather.util;

import android.text.TextUtils;

import com.example.brahma.coolweather.db.City;
import com.example.brahma.coolweather.db.County;
import com.example.brahma.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 10750 on 2017/8/1.
 */

public class Utility {

    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceJson=allProvinces.getJSONObject(i);
                    Province province=new Province();
                    province.setName(provinceJson.getString("name"));
                    province.setCode(provinceJson.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityJson=allCities.getJSONObject(i);
                    City city=new City();
                    city.setName(cityJson.getString("name"));
                    city.setCode(cityJson.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response,int cityId){
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
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

}
