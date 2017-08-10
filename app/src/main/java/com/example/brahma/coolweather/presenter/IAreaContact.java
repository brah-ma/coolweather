package com.example.brahma.coolweather.presenter;

import com.example.brahma.coolweather.db.City;
import com.example.brahma.coolweather.db.County;
import com.example.brahma.coolweather.db.Province;

import java.util.List;

/**
 * Created by 10750 on 2017/8/3.
 */

public interface IAreaContact {

    interface IAreaView {
        void showProvinces(List<Province> provinceList);

        void showCities(List<City> cityList);

        void showCounties(List<County> countyList);

        void showFailure(String message);
    }

    interface IAreaPresenter {
        void getProvinces();

        void getCities(Province province);

        void getCounties(City city);
    }

}
