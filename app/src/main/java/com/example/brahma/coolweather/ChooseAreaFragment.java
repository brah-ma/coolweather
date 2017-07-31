package com.example.brahma.coolweather;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class ChooseAreaFragment extends Fragment {

    public static final int Level_Province=0;

    public static final int Level_City=1;

    public static final int Level_County=2;

    private ProgressDialog progressDialog;

    @BindView(R.id.tv_titile)
    private TextView tvTitle;
    @BindView(R.id.btn_back)
    private Button btnBack;
    @BindView(R.id.list_view)
    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList=new ArrayList<>();

    private List<Province> provinceList;

    private List<City> cityList;

    private List<County> countyList;

    private Province selectedProvince;

    private City selectedCity;

    private County selectedCounty;

    private int currentLevel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_choose_area, container, false);
        ButterKnife.bind(this,view);
        adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==Level_Province){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if(currentLevel==Level_City){
                    selectedCity=cityList.get(position);
                    queryCounties();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel==Level_County){
                    queryCities();
                }else if(currentLevel==Level_City){
                    queryProvinces();
                }
            }
        });

        queryProvinces();
        return view;
    }

    private void queryProvinces(){
        tvTitle.setText("中国");
        btnBack.setVisibility(View.GONE);
        provinceList= DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getName());
            }
            adapter.notifyDataSetChanged();
            currentLevel=Level_Province;
        }else {
            String url="http://guolin.tech/api/china";
            queryFromServer(url,"province");
        }
    }

    private void queryCities(){
        tvTitle.setText(selectedProvince.getName());
        btnBack.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getName());
            }
            adapter.notifyDataSetChanged();
            currentLevel=Level_City;
        }else{
            int provinceCode=selectedProvince.getCode();
            String url="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(url,"city");

        }

    }

    private void queryCounties(){
        tvTitle.setText(selectedCity.getName());
        btnBack.setVisibility(View.VISIBLE);
        countyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getName());
            }
            adapter.notifyDataSetChanged();
            currentLevel=Level_County;
        }else{
            int provinceCode=selectedProvince.getCode();
            int cityCode=selectedCity.getCode();
            String url="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(url,"county");
        }
    }

    private void queryFromServer(String url, final String type){
        showPregressDialog();
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if("province".equals(type)){
                    result= Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result=Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showPregressDialog(){
        if (progressDialog==null) {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}