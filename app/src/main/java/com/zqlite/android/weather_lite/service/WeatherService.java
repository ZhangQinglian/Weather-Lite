package com.zqlite.android.weather_lite.service;

import com.zqlite.android.weather_lite.dao.HeWeatherPickerImpl;
import com.zqlite.android.weather_lite.dao.WeatherPicker;
import com.zqlite.android.weather_lite.entity.CityData;
import com.zqlite.android.weather_lite.entity.HeCitiesIniter;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherBuilder;
import com.zqlite.android.weather_lite.entity.WeatherData;

import java.util.List;

import rx.functions.Action1;

/**
 * @author qinglian.zhang
 */
public enum  WeatherService {
    sInstance;

    private WeatherPicker weatherPicker;

    WeatherService(){
        weatherPicker = HeWeatherPickerImpl.getInstance();
    }

    public void pick(String city,Action1<WeatherBuilder> action){
        weatherPicker.pickWeather(city,action);
    }

    public void pickWeathers(List<String> keys,Action1<List<WeatherData>> action){
        weatherPicker.pickWeathers(keys, action);
    }
    public void initCities(String search,HeWeatherPickerImpl.InitCitiesCallback callback){
        weatherPicker.initCitiesList(search, callback);
    }

    public void getAllCities(Action1<List<CityData>> action){
        weatherPicker.getAllCities(action);
    }

    public void searchCities(final String key, Action1<List<CityData>> action){
        weatherPicker.searchCities(key, action);
    }


    public void addMyCity(String cityId){
        weatherPicker.addMyCity(cityId);
    };

    public void getAllMyCities(Action1<List<MyCity>> action){
        weatherPicker.getAllMyCities(action);
    };

    public void reStoreMyCitiesIndex(List<MyCity> myCities){
        weatherPicker.reStoreMyCitiesIndex(myCities);
    }
}
