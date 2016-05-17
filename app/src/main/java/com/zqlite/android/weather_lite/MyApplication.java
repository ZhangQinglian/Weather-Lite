package com.zqlite.android.weather_lite;

import android.app.Application;
import android.content.SharedPreferences;

import com.zqlite.android.weather_lite.constant.WeatherConstant;
import com.zqlite.android.weather_lite.database.WeatherDatabaseHelper;
import com.zqlite.android.weather_lite.entity.HeCitiesIniter;
import com.zqlite.android.weather_lite.service.WeatherService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.functions.Action1;

/**
 * @author qinglian.zhang
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;
    private WeatherDatabaseHelper weatherDatabaseHelper;
    private final String pref = "weather";
    private final String initCity = "init_city";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        weatherDatabaseHelper = new WeatherDatabaseHelper(this,"weather",null,1);
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public WeatherService getWeatherService(){
        return WeatherService.sInstance;
    }

    public WeatherDatabaseHelper getWeatherDatabaseHelper(){
        return weatherDatabaseHelper;
    }

    public ExecutorService getSingleThreadExecutor(){
        return Executors.newSingleThreadExecutor();
    }


    public boolean isInitCities(){
        SharedPreferences sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        return sharedPreferences.getBoolean(initCity, false);
    }

    public void setInitCities(boolean b){
        SharedPreferences sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(initCity,b).apply();
    }
}
