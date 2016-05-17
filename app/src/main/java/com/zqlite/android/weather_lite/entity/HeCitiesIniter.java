package com.zqlite.android.weather_lite.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang
 */
public class HeCitiesIniter {

    public static final String CITY_INFO = "city_info";
    List<CityData> cityDatas;
    public HeCitiesIniter(String json) throws JSONException {
        //TODO 并不一定每次程序启动都去初始化，若数据库已存在尝试更新数据库
        cityDatas = new ArrayList<>(1000);
        JSONArray citiesJsonArray = new JSONObject(json).getJSONArray(CITY_INFO);

        for(int i = 0;i<citiesJsonArray.length();i++){
            JSONObject cityJsonObject = citiesJsonArray.getJSONObject(i);
            CityData cityData = new CityData();
            cityData.cityName = cityJsonObject.getString("city");
            cityData.cntyName = cityJsonObject.getString("cnty");
            cityData.city_id = cityJsonObject.getString("id");
            cityData.lat = cityJsonObject.getString("lat");
            cityData.lon = cityJsonObject.getString("lon");
            cityData.prov = cityJsonObject.getString("prov");
            cityDatas.add(cityData);
        }
    }

    public List<CityData> getCityDatas(){
        return cityDatas;
    }
}
