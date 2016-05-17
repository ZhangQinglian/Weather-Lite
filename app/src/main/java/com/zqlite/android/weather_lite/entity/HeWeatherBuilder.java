package com.zqlite.android.weather_lite.entity;

import android.util.Log;

import com.zqlite.android.weather_lite.constant.WeatherConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author qinglian.zhang
 *         <p/>
 *         用于解析得到的json数据，并将其转化为WeatherData对象
 */
public class HeWeatherBuilder extends WeatherBuilder {

    public static final String HE_WEATHER_JSON_KEY = "HeWeather data service 3.0";

    public static final String HE_WEATHER_JSON_BASIC = "basic";

    public static final String HE_WEATHER_JSON_NOW = "now";

    public static final String HE_WEATHER_JSON_AQI = "aqi";

    public static final String HE_WEATHER_JSON_DAILY_FORECAST = "daily_forecast";

    public static final String HE_WEATHER_JSON_HOURLY_FORECAST = "hourly_forecast";

    public static final String HE_WEATHER_JSON_SUGGESTION = "suggestion";

    private JSONObject jsonObject;
    private Basic basic;
    private Now now;
    private Aqi aqi;
    private DailyForecast[] dailyForecasts;
    private HourlyForecast[] hourlyForecasts;
    private Suggestion suggestion;

    public HeWeatherBuilder(String json) throws JSONException {
        jsonObject = new JSONObject(json);
        //Log.d("weather",jsonObject.toString());
        init();
    }

    private void init() throws JSONException {
        JSONObject jsonObjectWeather = jsonObject.getJSONArray(HE_WEATHER_JSON_KEY)
                .getJSONObject(0);

        //init Basic
        basic = new Basic();
        JSONObject jsonObjectBasic = jsonObjectWeather.getJSONObject(HE_WEATHER_JSON_BASIC);

        basic.city = jsonObjectBasic.getString("city");
        basic.cnty = jsonObjectBasic.getString("cnty");
        basic.id = jsonObjectBasic.getString("id");
        basic.lat = jsonObjectBasic.getString("lat");
        basic.lon = jsonObjectBasic.getString("lon");
        basic.update[0] = jsonObjectBasic.getJSONObject("update").getString("loc");
        basic.update[1] = jsonObjectBasic.getJSONObject("update").getString("utc");


        //init Now
        JSONObject jsonObjectNow = jsonObjectWeather.getJSONObject(HE_WEATHER_JSON_NOW);
        now = new Now();
        now.cond[0] = jsonObjectNow.getJSONObject("cond").getString("code");
        now.cond[1] = jsonObjectNow.getJSONObject("cond").getString("txt");
        now.fl = jsonObjectNow.getString("fl");
        now.hum = jsonObjectNow.getString("hum");
        now.pcpn = jsonObjectNow.getString("pcpn");
        now.pres = jsonObjectNow.getString("pres");
        now.tmp = jsonObjectNow.getString("tmp");
        now.vis = jsonObjectNow.getString("vis");
        now.wind[0] = jsonObjectNow.getJSONObject("wind").getString("deg");
        now.wind[1] = jsonObjectNow.getJSONObject("wind").getString("dir");
        now.wind[2] = jsonObjectNow.getJSONObject("wind").getString("sc");
        now.wind[3] = jsonObjectNow.getJSONObject("wind").getString("spd");

        //init Aqi
        try {
            JSONObject jsonObjectAqi = jsonObjectWeather.getJSONObject(HE_WEATHER_JSON_AQI).getJSONObject("city");
            aqi = new Aqi();
            aqi.aqi = jsonObjectAqi.getString("aqi");
            aqi.co = jsonObjectAqi.getString("co");
            aqi.no2 = jsonObjectAqi.getString("no2");
            aqi.o3 = jsonObjectAqi.getString("o3");
            aqi.pm10 = jsonObjectAqi.getString("pm10");
            aqi.pm25 = jsonObjectAqi.getString("pm25");
            aqi.qlty = jsonObjectAqi.getString("qlty");
            aqi.so2 = jsonObjectAqi.getString("so2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //init daily forecast
        JSONArray jsonArrayDailyForecasts = jsonObjectWeather.getJSONArray(HE_WEATHER_JSON_DAILY_FORECAST);
        int len = jsonArrayDailyForecasts.length();
        if (len > 0) {
            dailyForecasts = new DailyForecast[len];
            for (int i = 0; i < len; i++) {
                JSONObject jsonObject = jsonArrayDailyForecasts.getJSONObject(i);
                DailyForecast df = new DailyForecast();
                df.date = jsonObject.getString("date");
                df.astro[0] = jsonObject.getJSONObject("astro").getString("sr");
                df.astro[1] = jsonObject.getJSONObject("astro").getString("ss");
                df.cond[0] = jsonObject.getJSONObject("cond").getString("code_d");
                df.cond[1] = jsonObject.getJSONObject("cond").getString("code_n");
                df.cond[2] = jsonObject.getJSONObject("cond").getString("txt_d");
                df.cond[3] = jsonObject.getJSONObject("cond").getString("txt_n");
                df.hum = jsonObject.getString("hum");
                df.pcpn = jsonObject.getString("pcpn");
                df.pop = jsonObject.getString("pop");
                df.pres = jsonObject.getString("pres");
                df.tmp[0] = jsonObject.getJSONObject("tmp").getString("max");
                df.tmp[1] = jsonObject.getJSONObject("tmp").getString("min");
                df.vis = jsonObject.getString("vis");
                df.wind[0] = jsonObject.getJSONObject("wind").getString("deg");
                df.wind[1] = jsonObject.getJSONObject("wind").getString("dir");
                df.wind[2] = jsonObject.getJSONObject("wind").getString("sc");
                df.wind[3] = jsonObject.getJSONObject("wind").getString("spd");
                dailyForecasts[i] = df;
            }
        }

        //init hourly forecast
        JSONArray jsonArrayHourlyForecasts = jsonObjectWeather.getJSONArray(HE_WEATHER_JSON_HOURLY_FORECAST);
        int len1 = jsonArrayHourlyForecasts.length();
        if (len1 > 0) {
            hourlyForecasts = new HourlyForecast[len1];
            for (int i = 0; i < len1; i++) {
                JSONObject jsonObjectHourly = jsonArrayHourlyForecasts.getJSONObject(i);
                HourlyForecast hourlyForecast = new HourlyForecast();
                hourlyForecast.date = jsonObjectHourly.getString("date");
                hourlyForecast.hum = jsonObjectHourly.getString("hum");
                hourlyForecast.pop = jsonObjectHourly.getString("pop");
                hourlyForecast.pres = jsonObjectHourly.getString("pres");
                hourlyForecast.tmp = jsonObjectHourly.getString("tmp");
                hourlyForecast.wind[0] = jsonObjectHourly.getJSONObject("wind").getString("deg");
                hourlyForecast.wind[1] = jsonObjectHourly.getJSONObject("wind").getString("dir");
                hourlyForecast.wind[2] = jsonObjectHourly.getJSONObject("wind").getString("sc");
                hourlyForecast.wind[3] = jsonObjectHourly.getJSONObject("wind").getString("spd");
                hourlyForecasts[i] = hourlyForecast;
            }
        }

        //init Suggestion
        JSONObject jsonObjectSuggestion = jsonObjectWeather.getJSONObject(HE_WEATHER_JSON_SUGGESTION);
        suggestion = new Suggestion();
        suggestion.comf[0] = jsonObjectSuggestion.getJSONObject("comf").getString("brf");
        suggestion.comf[1] = jsonObjectSuggestion.getJSONObject("comf").getString("txt");

        suggestion.cw[0] = jsonObjectSuggestion.getJSONObject("cw").getString("brf");
        suggestion.cw[1] = jsonObjectSuggestion.getJSONObject("cw").getString("txt");

        suggestion.drsg[0] = jsonObjectSuggestion.getJSONObject("drsg").getString("brf");
        suggestion.drsg[1] = jsonObjectSuggestion.getJSONObject("drsg").getString("txt");

        suggestion.flu[0] = jsonObjectSuggestion.getJSONObject("flu").getString("brf");
        suggestion.flu[1] = jsonObjectSuggestion.getJSONObject("flu").getString("txt");

        suggestion.sport[0] = jsonObjectSuggestion.getJSONObject("sport").getString("brf");
        suggestion.sport[1] = jsonObjectSuggestion.getJSONObject("sport").getString("txt");

        suggestion.trav[0] = jsonObjectSuggestion.getJSONObject("trav").getString("brf");
        suggestion.trav[1] = jsonObjectSuggestion.getJSONObject("trav").getString("txt");

        suggestion.uv[0] = jsonObjectSuggestion.getJSONObject("uv").getString("brf");
        suggestion.uv[1] = jsonObjectSuggestion.getJSONObject("uv").getString("txt");

    }

    @Override
    public WeatherData build() {
        WeatherData data = new WeatherData();
        data.city = basic.city;
        data.cityId = basic.id;
        data.cnty = basic.cnty;
        data.date = basic.update[0];
        data.cond = now.cond;
        data.hum = now.hum + WeatherConstant.HUM_UNIT;
        data.fl = now.fl + WeatherConstant.CELSIUS_DEGRESS;
        data.wind = now.wind[1] + " " + now.wind[3] + WeatherConstant.WIND_UNIT;
        data.pres = now.pres + WeatherConstant.PRES_UNIT;
        if (aqi != null) {
            data.aqi = aqi.aqi;
        }
        data.dailyForecasts = dailyForecasts;
        data.hourlyForecasts = hourlyForecasts;
        data.suggestion = suggestion;
        return data;
    }

    /**
     * 基本信息
     */
    public static class Basic {
        /**
         * 城市名称
         */
        public String city;
        /**
         * 国家
         */
        public String cnty;
        /**
         * 城市ID，参见http://www.heweather.com/documents/cn-city-list
         */
        public String id;
        /**
         * 城市维度
         */
        public String lat;
        /**
         * 城市经度
         */
        public String lon;
        /**
         * [0] loc :当地时间
         * [1] utc :UTC时间
         */
        public String[] update = new String[2];
    }

    /**
     * 当前天气状况
     */
    public static class Now {
        /**
         * 天气状况
         * [0] code 天气状况代码
         * [1] txt 天气状况描述
         */
        public String[] cond = new String[2];
        /**
         * 体感温度
         */
        public String fl;
        /**
         * 相对湿度（%）
         */
        public String hum;
        /**
         * 降水量（mm）
         */
        public String pcpn;
        /**
         * 气压
         */
        public String pres;
        /**
         * 温度
         */
        public String tmp;
        /**
         * 能见度（km）
         */
        public String vis;
        /**
         * 风力风向
         * [0] deg 风向（360度）
         * [1] dir 风向
         * [2] sc 风力
         * [3] spd 风速（kmph）
         */
        public String[] wind = new String[4];
    }

    /**
     * 空气质量，仅限国内部分城市，国际城市无此字段
     */
    public static class Aqi {
        /**
         * 空气质量指数
         */
        public String aqi;
        /**
         * 一氧化碳1小时平均值(ug/m³)
         */
        public String co;
        /**
         * 二氧化氮1小时平均值(ug/m³)
         */
        public String no2;
        /**
         * 臭氧1小时平均值(ug/m³)
         */
        public String o3;
        /**
         * PM10 1小时平均值(ug/m³)
         */
        public String pm10;
        /**
         * PM2.5 1小时平均值(ug/m³)
         */
        public String pm25;
        /**
         * 空气质量类别
         */
        public String qlty;
        /**
         * 二氧化硫1小时平均值(ug/m³)
         */
        public String so2;
    }

    /**
     * 天气预报
     */
    public static class DailyForecast implements Serializable {
        /**
         * 预报日期
         */
        public String date;
        /**
         * 天文数值
         * [0] sr 日出时间
         * [1] ss 日落时间
         */
        public String[] astro = new String[2];
        /**
         * 天气状况
         * [0] code_d 白天天气状况代码，参考http://www.heweather.com/documents/condition-code
         * [1] code_n 夜间天气状况代码
         * [2] txt_d 白天天气状况描述
         * [3] txt_n 夜间天气状况描述
         */
        public String[] cond = new String[4];
        /**
         * 相对湿度（%
         */
        public String hum;
        /**
         * 降水量（mm）
         */
        public String pcpn;
        /**
         * 降水概率
         */
        public String pop;
        /**
         * 气压
         */
        public String pres;
        /**
         * 温度
         * [0] max 最高温度
         * [1] min 最低温度
         */
        public String[] tmp = new String[2];
        /**
         * 能见度（km）
         */
        public String vis;
        /**
         * 风力风向
         * [0] 风向（360度）
         * [1] 风向
         * [2] 风力
         * [3] 风速（kmph）
         */
        public String[] wind = new String[4];
    }

    public static class HourlyForecast implements Serializable {
        /**
         * 时间
         */
        public String date;
        /**
         * 相对湿度
         */
        public String hum;
        /**
         * 降水概率
         */
        public String pop;
        /**
         * 气压
         */
        public String pres;
        /**
         * 温度
         */
        public String tmp;
        /**
         * 风力风向
         * [0] 风向（360度）
         * [1] 风向
         * [2] 风力
         * [3] 风速（kmph）
         */
        public String[] wind = new String[4];
    }

    public static class Suggestion implements Serializable {

        /**
         * 舒适度指数
         */
        public String[] comf = new String[2];
        /**
         * 洗车指数
         */
        public String[] cw = new String[2];
        /**
         * 穿衣指数
         */
        public String[] drsg = new String[2];
        /**
         * 感冒指数
         */
        public String[] flu = new String[2];
        /**
         * 运动指数
         */
        public String[] sport = new String[2];
        /**
         * 旅游指数
         */
        public String[] trav = new String[2];
        /**
         * 紫外线指数
         */
        public String[] uv = new String[2];

    }
}
