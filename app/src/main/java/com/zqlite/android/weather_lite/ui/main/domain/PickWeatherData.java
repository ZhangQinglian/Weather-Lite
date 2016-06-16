package com.zqlite.android.weather_lite.ui.main.domain;

import android.util.Log;

import com.zqlite.android.mvpc.UseCase;
import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.entity.WeatherBuilder;
import com.zqlite.android.weather_lite.entity.WeatherData;

import rx.functions.Action1;

/**
 * Created by scott on 6/16/16.
 */
public class PickWeatherData extends UseCase<PickWeatherData.RequestValues,PickWeatherData.ResponseValue> {

    @Override
    protected void executeUseCase(final RequestValues requestValues) {
        MyApplication.getInstance().getWeatherService().pick(requestValues.cityId, new Action1<WeatherBuilder>() {
            @Override
            public void call(WeatherBuilder weatherBuilder) {
                MyApplication.getInstance().getWeatherService().addMyCity(requestValues.cityId);
                final WeatherData weatherData = weatherBuilder.build();
                Log.d("weather", "  weatherData = " + weatherData.toString());
                getUseCaseCallback().onSuccess(new PickWeatherData.ResponseValue(weatherData));
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues{
        public final String cityId ;

        public RequestValues(String cityId){
            this.cityId = cityId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue{

        public final WeatherData weatherData;

        public ResponseValue(WeatherData data){
            weatherData = data;
        }
    }
}
