package com.zqlite.android.weather_lite.entity;


import com.zqlite.android.weather_lite.R;

import java.io.Serializable;

import static com.zqlite.android.weather_lite.entity.HeWeatherBuilder.DailyForecast;

import static com.zqlite.android.weather_lite.entity.HeWeatherBuilder.HourlyForecast;

import static  com.zqlite.android.weather_lite.entity.HeWeatherBuilder.Suggestion;

/**
 * @author qinglian.zhang
 */
public class WeatherData implements Serializable{

    public static enum WeatherCondition{
        Sunny(100,"http://files.heweather.com/cond_icon/100.png"),
        Cloudy(101,"http://files.heweather.com/cond_icon/101.png"),
        Few_Clouds(102,"http://files.heweather.com/cond_icon/102.png"),
        Partly_Cloudy(103,"http://files.heweather.com/cond_icon/103.png"),
        Overcast(104,"http://files.heweather.com/cond_icon/104.png"),
        Windy(200,"http://files.heweather.com/cond_icon/200.png"),
        Calm(201,"http://files.heweather.com/cond_icon/201.png"),
        Light_Breeze(202,"http://files.heweather.com/cond_icon/202.png"),
        Moderate(203,"http://files.heweather.com/cond_icon/203.png"),
        Fresh_Breeze(204,"http://files.heweather.com/cond_icon/204.png"),
        Strong_Breeze(205,"http://files.heweather.com/cond_icon/205.png"),
        High_Wind(206,"http://files.heweather.com/cond_icon/206.png"),
        Gale(207,"http://files.heweather.com/cond_icon/207.png"),
        Strong_Gale(208,"http://files.heweather.com/cond_icon/208.png"),
        Storm(209,"http://files.heweather.com/cond_icon/209.png"),
        Violent_Storm(210,"http://files.heweather.com/cond_icon/210.png"),
        Hurricane(211,"http://files.heweather.com/cond_icon/211.png"),
        Tornado(212,"http://files.heweather.com/cond_icon/212.png"),
        Tropical_Storm(213,"http://files.heweather.com/cond_icon/213.png"),
        Shower_Rain(300,"http://files.heweather.com/cond_icon/300.png"),
        Heavy_Shower_Rain(301,"http://files.heweather.com/cond_icon/301.png"),
        Thundershower(302,"http://files.heweather.com/cond_icon/302.png"),
        Heavy_Thunderstorm(303,"http://files.heweather.com/cond_icon/303.png"),
        Hail(304,"http://files.heweather.com/cond_icon/304.png"),
        Light_Rain(305,"http://files.heweather.com/cond_icon/305.png"),
        Moderate_Rain(306,"http://files.heweather.com/cond_icon/306.png"),
        Heavy_Rain(307,"http://files.heweather.com/cond_icon/307.png"),
        Extreme_Rain(308,"http://files.heweather.com/cond_icon/308.png"),
        Drizzle_Rain(309,"http://files.heweather.com/cond_icon/309.png"),
        Storm1(310,"http://files.heweather.com/cond_icon/310.png"),
        Heavy_Storm(311,"http://files.heweather.com/cond_icon/311.png"),
        Severe_Storm(312,"http://files.heweather.com/cond_icon/312.png"),
        Freezing_Rain(313,"http://files.heweather.com/cond_icon/313.png"),
        Light_Snow(400,"http://files.heweather.com/cond_icon/400.png"),
        Moderate_Snow(401,"http://files.heweather.com/cond_icon/401.png"),
        Heavy_Snow(402,"http://files.heweather.com/cond_icon/402.png"),
        Snowstorm(403,"http://files.heweather.com/cond_icon/403.png"),
        Sleet(404,"http://files.heweather.com/cond_icon/404.png"),
        Rain_And_Snow(405,"http://files.heweather.com/cond_icon/405.png"),
        Shower_Snow(406,"http://files.heweather.com/cond_icon/406.png"),
        Snow_Flurry(407,"http://files.heweather.com/cond_icon/407.png"),
        Mist(500,"http://files.heweather.com/cond_icon/500.png"),
        Foggy(501,"http://files.heweather.com/cond_icon/501.png"),
        Haze(502,"http://files.heweather.com/cond_icon/502.png"),
        Sand(503,"http://files.heweather.com/cond_icon/503.png"),
        Dust(504,"http://files.heweather.com/cond_icon/504.png"),
        Volcanic_Ash(506,"http://files.heweather.com/cond_icon/506.png"),
        Duststorm(507,"http://files.heweather.com/cond_icon/507.png"),
        Sandstorm(508,"http://files.heweather.com/cond_icon/508.png"),
        Hot(900,"http://files.heweather.com/cond_icon/900.png"),
        Cold(901,"http://files.heweather.com/cond_icon/901.png"),
        Unknown(999,"http://files.heweather.com/cond_icon/999.png");


        private int code ;
        private String iconUri;

        WeatherCondition(int code,String iconUri){
            this.code = code;
            this.iconUri = iconUri;
        }
    }
    /**
     * 城市
     */
    public String city;

    /**
     * 城市 id
     */
    public String cityId;
    /**
     * 国家
     */
    public String cnty;

    /**
     * 天气更新时间
     */
    public String date;

    /**
     * 天气情况
     */
    public String cond[];

    /**
     * 湿度
     */
    public String hum;
    /**
     * 体感温度
     */
    public String fl;

    /**
     * 风力风向
     */
    public String wind;

    /**
     * 空气质量指数
     */
    public String aqi;

    /**
     * 大气压
     */
    public String pres;

    /**
     * 未来七天天气
     */
    public DailyForecast[] dailyForecasts;

    /**
     * 当天每三小时天气
     */
    public HourlyForecast[] hourlyForecasts;

    /**
     * 天气指数
     */
    public Suggestion suggestion;

    @Override
    public String toString() {
        return city + " " + date + " " + cond[1] + " " + " 体感温度 : " + fl + " 摄氏度" + "cond id = " + cond[0];
    }

    public static String getWeatherIconUriByCode(int code){
        for(WeatherCondition condition : WeatherCondition.values()){
            if(condition.code == code){
                return condition.iconUri;
            }
        }
        return WeatherCondition.Unknown.iconUri;
    }

    public static int getWeatherIconDrawableId(int code){
        //TODO 区分白天和晚上的天气图标
        switch (code){
            case 100:
                return R.drawable.clear_day;
            case 101:
            case 102:
            case 103:
                return R.drawable.mostly_cloudy;
            case 104:
                return R.drawable.cloudy_weather;
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
                return R.drawable.windy_weather;
            case 300:
            case 301:
                return R.drawable.rainy_day;
            case 302:
            case 303:
                return R.drawable.showcase;
            case 305:
            case 306:
            case 307:
            case 308:
                return R.drawable.rainy_weather;
            case 401:
            case 402:
            case 403:
                return R.drawable.snow_weather;
            case 407:
            case 500:
            case 501:
            case 502:
                return R.drawable.haze_weather;
            default:
                return R.drawable.unknown;
        }
    }
    public static int getWeatherIcon80DrawableId(int code){
        //TODO 区分白天和晚上的天气图标
        switch (code){
            case 100:
                return R.drawable.clear_day_80;
            case 101:
            case 102:
            case 103:
                return R.drawable.mostly_cloudy_80;
            case 104:
                return R.drawable.cloudy_weather_80;
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
                return R.drawable.windy_weather_80;
            case 300:
            case 301:
                return R.drawable.rainy_day_80;
            case 302:
            case 303:
                return R.drawable.showcase_80;
            case 305:
            case 306:
            case 307:
            case 308:
                return R.drawable.rainy_weather_80;
            case 401:
            case 402:
            case 403:
                return R.drawable.snow_weather_80;
            case 407:
            case 500:
            case 501:
            case 502:
                return R.drawable.haze_weather_80;
            default:
                return R.drawable.unknown_80;
        }
    }

    public static int getColorByCode(int code){
        switch (code){
            case 100:
                return R.color.blue;
            case 101:
            case 102:
            case 103:
                return R.color.cyan;
            case 300:
                return R.color.gray;
            default:
                return R.color.gray;
        }
    }
}
