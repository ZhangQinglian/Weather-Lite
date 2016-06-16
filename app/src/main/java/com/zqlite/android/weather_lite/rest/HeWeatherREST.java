package com.zqlite.android.weather_lite.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author qinglian.zhang
 */
public interface HeWeatherREST {

    public static final String HE_WEATHER_API = "https://api.heweather.com/" ;

    //在http://www.heweather.com处申请apikey并替换xxx
    public static final String API_KEY = "601297358cee47b6950b984c10fca845";

    @GET("x3/weather")
    Observable<ResponseBody> getWeatherAsync(@Query("cityid") String city,@Query("key") String key);

    @GET("x3/weather")
    Call<ResponseBody> getWeatherSync(@Query("cityid") String city,@Query("key") String key);

    @GET("x3/citylist")
    Observable<ResponseBody> getCitiesAsync(@Query("search") String search,@Query("key") String key);
}
