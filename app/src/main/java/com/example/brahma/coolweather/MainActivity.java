package com.example.brahma.coolweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.brahma.coolweather.db.County;
import com.example.brahma.coolweather.presenter.IWeatherContact;
import com.example.brahma.coolweather.presenter.WeatherPresenter;
import com.example.brahma.coolweather.weather.Forecast;
import com.example.brahma.coolweather.weather.Weather;
import com.google.gson.annotations.SerializedName;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity implements ChooseAreaFragment.ClickCountyListener,IWeatherContact.IWeatherView {

    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.ll_forecast)
    LinearLayout llForecast;
    @BindView(R.id.tv_aqi)
    TextView tvAqi;
    @BindView(R.id.tv_pm25)
    TextView tvPm25;
    @BindView(R.id.tv_comfort)
    TextView tvComfort;
    @BindView(R.id.tv_crashCar)
    TextView tvCrashCar;
    @BindView(R.id.tv_sport)
    TextView tvSport;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.ivWeatherBg)
    ImageView ivWeatherBg;

    private String weatherId;



    ChooseAreaFragment chooseAreaFragment;
    IWeatherContact.IWeatherPresenter mWeatherpresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mWeatherpresenter=new WeatherPresenter(this);
        chooseAreaFragment= (ChooseAreaFragment) getFragmentManager().findFragmentById(R.id.fragment_choose_area);
        chooseAreaFragment.setOnClickCountyListener(this);

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("weather",MODE_PRIVATE);
       weatherId=sharedPreferences.getString("weather_id",null);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(weatherId!=null){
                    mWeatherpresenter.getWeatherInfo(weatherId);
                }

            }
        });

//        mWeatherpresenter.getWeatherInfo("");

        if(weatherId!=null){
            mWeatherpresenter.getWeatherInfo(weatherId);
        }else{
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        mWeatherpresenter.getWeatherBgUrl();

    }

    @Override
    public void onClickCounty(County county) {
        SharedPreferences sharedPreferences=getSharedPreferences("weather",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("weather_id",county.getWeatherId());
        editor.commit();
        weatherId=county.getWeatherId();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mSwipeRefreshLayout.setRefreshing(true);
        mWeatherpresenter.getWeatherInfo(county.getWeatherId());

    }

    @Override
    public void showWeatherInfo( final Weather weather) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!weather.getStatus().equals("ok")){
                    Toast.makeText(MainActivity.this,weather.getStatus(),Toast.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                String cityName=weather.getBasic().getCityName();
                String updateTime=weather.getBasic().getUpdate().getUpdateTime().split(" ")[1];
                String temperature=weather.getNow().getTemperature()+"℃";
                String weatherInfo=weather.getNow().getMore().getInfo();
                tvTitle.setText(cityName);
                tvTime.setText(updateTime);
                tvTemperature.setText(temperature);
                tvWeather.setText(weatherInfo);
                llForecast.removeAllViews();
                if(llForecast.getChildCount()==weather.getForecastList().size()){
                    for(int i=0;i<weather.getForecastList().size();i++){
                        View view=llForecast.getChildAt(i);
                        ViewHolder holder=(ViewHolder)view.getTag();
                        Forecast forecast=weather.getForecastList().get(i);
                        holder.tvDate.setText(forecast.date);
                        holder.tvInfo.setText(forecast.more.getInfo());
                        holder.tvMaxTemp.setText(forecast.temperature.max);
                        holder.tvMinTemp.setText(forecast.temperature.min);
                    }
                }else{
                    llForecast.removeAllViews();
                    for(Forecast forecast:weather.getForecastList()){
                        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.forecast_item,llForecast,false);
                        ViewHolder holder=new ViewHolder(view);
                        view.setTag(holder);
                        holder.tvDate.setText(forecast.date);
                        holder.tvInfo.setText(forecast.more.getInfo());
                        holder.tvMaxTemp.setText(forecast.temperature.max);
                        holder.tvMinTemp.setText(forecast.temperature.min);
                        llForecast.addView(view);
                    }
                }
                if (weather.getAqi()!=null){
                    tvAqi.setText(weather.getAqi().getCity().getAqi());
                    tvPm25.setText(weather.getAqi().getCity().getPm25());
                }
                String comfort="舒适度："+weather.getSuggestion().getComfort().getInfo();
                String carWash="洗车指数："+weather.getSuggestion().getCarWash().getInfo();
                String sport="运动建议："+weather.getSuggestion().getSport().getInfo();
                tvComfort.setText(comfort);
                tvCrashCar.setText(carWash);
                tvSport.setText(sport);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void showWeatherBg(final  String bgUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(MainActivity.this).load(bgUrl).into(ivWeatherBg);
            }
        });

    }

    class ViewHolder{
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvInfo)
        TextView tvInfo;
        @BindView(R.id.tvMaxTemp)
        TextView tvMaxTemp;
        @BindView(R.id.tvMinTemp)
        TextView tvMinTemp;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
