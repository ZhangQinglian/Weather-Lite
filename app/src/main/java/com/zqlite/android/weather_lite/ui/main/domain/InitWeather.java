package com.zqlite.android.weather_lite.ui.main.domain;

import com.zqlite.android.mvpc.UseCase;
import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherData;
import com.zqlite.android.weather_lite.service.WeatherService;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by scott on 6/16/16.
 */
public class InitWeather extends UseCase<InitWeather.RequestValues,InitWeather.ResponseValue> {

    private WeatherService weatherService;

    public InitWeather(){
        weatherService = MyApplication.getInstance().getWeatherService();
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        weatherService.getAllMyCities(new Action1<List<MyCity>>() {
            @Override
            public void call(final List<MyCity> cities) {
                List<String> cityIds = new ArrayList<String>();
                for (MyCity city : cities) {
                    cityIds.add(city.cityId);
                }
                weatherService.pickWeathers(cityIds, new Action1<List<WeatherData>>() {
                    @Override
                    public void call(List<WeatherData> weatherDatas) {
                        getUseCaseCallback().onSuccess(new ResponseValue(weatherDatas,cities));
                    }
                });
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues{

    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        public final List<WeatherData> weatherDatas;

        public final List<MyCity> cities;



        public ResponseValue(List<WeatherData> datas,List<MyCity> cities){
            weatherDatas = datas;
            this.cities = cities;
        }
    }
}
