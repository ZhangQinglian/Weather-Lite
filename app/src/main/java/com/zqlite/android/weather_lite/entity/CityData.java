package com.zqlite.android.weather_lite.entity;

/**
 * @author qinglian.zhang
 */
public class CityData {

    public String cityName;

    public String cityNamePinyin;

    public String cntyName;

    public String city_id;

    public String lat;

    public String lon;

    public String prov;

    @Override
    public String toString() {
        return cityName + " - " + cityNamePinyin + " - " + cntyName + " - " + city_id + " - " + lat + " - " + lon +" - " +  prov;
    }
}
