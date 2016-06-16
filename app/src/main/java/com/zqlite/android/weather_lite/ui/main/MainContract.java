package com.zqlite.android.weather_lite.ui.main;

import com.zqlite.android.mvpc.IContract;
import com.zqlite.android.mvpc.IPresenter;
import com.zqlite.android.mvpc.IView;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherData;

import java.util.List;

/**
 * Created by scott on 6/16/16.
 */
public class MainContract implements IContract {

    interface Presenter extends IPresenter{

        void initWeather();

        void addWeather(WeatherData data);

        void initCities();

        void pickWeather(String cityId);
    }

    interface View extends IView<Presenter>{

        void removeAll();

        void addWeather(WeatherData data);

        void stopFresh();

        void setMyCities(List<MyCity> cities);
    }

}
