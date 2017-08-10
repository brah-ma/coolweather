package com.example.brahma.coolweather.db;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 10750 on 2017/8/1.
 */

public class City extends DataSupport {

    private int id;

    private int cityId;

    private int provinceId;

    private String name;

    private List<County> countyList;

    public List<County> getCountyList() {
        return countyList;
    }

    public void setCountyList(List<County> countyList) {
        this.countyList = countyList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
