package com.zqlite.android.weather_lite.ui.main.domain;

import com.zqlite.android.mvpc.UseCase;
import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.constant.WeatherConstant;
import com.zqlite.android.weather_lite.dao.HeWeatherPickerImpl;

/**
 * Created by scott on 6/16/16.
 */
public class InitCities extends UseCase<InitCities.RequestValues,InitCities.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        if (!MyApplication.getInstance().isInitCities()) {
            //初始化城市列表
            MyApplication.getInstance().getWeatherService().initCities(WeatherConstant.CITY_SEARCH_TYPE_ALL_CHINA, new HeWeatherPickerImpl.InitCitiesCallback() {
                @Override
                public void initSuccess() {
                    MyApplication.getInstance().setInitCities(true);
                    getUseCaseCallback().onSuccess(new InitCities.ResponseValue());
                }

                @Override
                public void initFailure() {
                    MyApplication.getInstance().setInitCities(false);
                    getUseCaseCallback().onError();
                }
            });
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{

    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }
}
