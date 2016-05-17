package com.zqlite.android.weather_lite.dao;

import com.zqlite.android.weather_lite.entity.CityData;
import com.zqlite.android.weather_lite.entity.HeCitiesIniter;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherBuilder;
import com.zqlite.android.weather_lite.entity.WeatherData;

import java.util.List;

import rx.functions.Action1;

/**
 * @author qinglian.zhang
 * 天气获取超类
 */
public abstract class WeatherPicker {

    /**
     * 查看某个城市的天气情况
     * @param key 城市关键字
     *            3x ：cityid
     * @param action
     */
    public abstract void pickWeather(String key,Action1<WeatherBuilder> action);

    public abstract void pickWeathers(List<String> keys,Action1<List<WeatherData>> action);

    public abstract void initCitiesList(String search,HeWeatherPickerImpl.InitCitiesCallback callback);

    public abstract void getAllCities(Action1<List<CityData>> action);

    public abstract void searchCities(String key,Action1<List<CityData>> action);

    public abstract void addMyCity(String cityId);

    public abstract void getAllMyCities(Action1<List<MyCity>> action);

    public abstract void reStoreMyCitiesIndex(List<MyCity> myCities);


}
