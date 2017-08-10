package com.example.brahma.coolweather;


import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brahma.coolweather.db.City;
import com.example.brahma.coolweather.db.County;
import com.example.brahma.coolweather.db.Province;
import com.example.brahma.coolweather.presenter.AreaPresenter;
import com.example.brahma.coolweather.presenter.IAreaContact;
import com.example.brahma.coolweather.util.HttpUtil;
import com.example.brahma.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends android.app.Fragment implements IAreaContact.IAreaView {

    public static final int Level_Province = 1;

    public static final int Level_City = 2;

    public static final int Level_County = 3;

    private int currentLevel = 0;


    @BindView(R.id.tv_titile)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.list_view)
    ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList;

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private Province selectedProvince;

    private City selectedCity;


    private IAreaContact.IAreaPresenter mAreaPresenter;

    interface ClickCountyListener {
        void onClickCounty(County county);
    }

    private ClickCountyListener clickCountyListener;

    public void setOnClickCountyListener(ClickCountyListener clickCountyListener) {
        this.clickCountyListener = clickCountyListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        mAreaPresenter = new AreaPresenter(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == Level_Province) {
                    selectedProvince = provinceList.get(position);
                    mAreaPresenter.getCities(selectedProvince);
                } else if (currentLevel == Level_City) {
                    selectedCity = cityList.get(position);
                    mAreaPresenter.getCounties(selectedCity);
                } else if (currentLevel == Level_County) {
                   if(clickCountyListener!=null){
                       clickCountyListener.onClickCounty(countyList.get(position));
                   }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == Level_County) {
                    showCities(selectedProvince.getCityList());
                } else if (currentLevel == Level_City) {
                    showProvinces(provinceList);
                }
            }
        });

        mAreaPresenter.getProvinces();
    }

    @Override
    public void showProvinces(List<Province> provinceList) {
        currentLevel = Level_Province;
        this.provinceList = provinceList;
        dataList.clear();
        for (Province province : provinceList) {
            dataList.add(province.getName());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText("中国");
                btnBack.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void showCities(List<City> cityList) {
        selectedProvince.setCityList(cityList);
        currentLevel = Level_City;
        this.cityList = cityList;

        dataList.clear();
        for (City city : cityList) {
            dataList.add(city.getName());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(selectedProvince.getName());
                btnBack.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showCounties(List<County> countyList) {

        selectedCity.setCountyList(countyList);
        this.countyList=countyList;
        currentLevel = Level_County;
        this.countyList = countyList;
        dataList.clear();
        for (County county : countyList) {
            dataList.add(county.getName());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(selectedCity.getName());
                btnBack.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showFailure(String message) {

    }
}
