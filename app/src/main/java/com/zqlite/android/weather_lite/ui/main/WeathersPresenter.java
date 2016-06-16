package com.zqlite.android.weather_lite.ui.main;

import com.zqlite.android.mvpc.UseCase;
import com.zqlite.android.mvpc.UseCaseHandler;
import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.entity.WeatherData;
import com.zqlite.android.weather_lite.service.WeatherService;
import com.zqlite.android.weather_lite.ui.main.domain.InitCities;
import com.zqlite.android.weather_lite.ui.main.domain.InitWeather;
import com.zqlite.android.weather_lite.ui.main.domain.PickWeatherData;
import com.zqlite.android.weather_lite.utils.GroceryStore;


/**
 * Created by scott on 6/16/16.
 */
public class WeathersPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private WeatherService weatherService;

    private UseCaseHandler useCaseHandler;

    private InitWeather initWeatherUseCase;

    private InitCities initCitiesUseCase;

    private PickWeatherData pickWeatherDataUseCase;

    public WeathersPresenter(MainContract.View view,UseCaseHandler handler){
        this.view = view;
        weatherService = MyApplication.getInstance().getWeatherService();
        view.setPresenter(this);
        useCaseHandler = handler;
        initWeatherUseCase = new InitWeather();
        initCitiesUseCase = new InitCities();
        pickWeatherDataUseCase = new PickWeatherData();
    }
    @Override
    public void start() {
        initWeather();
    }

    @Override
    public void initWeather() {
        if (!GroceryStore.isNetWorkAccess(MyApplication.getInstance())) {
            return;
        }
        view.removeAll();

        useCaseHandler.execute(initWeatherUseCase, new InitWeather.RequestValues(), new UseCase.UseCaseCallback<InitWeather.ResponseValue>() {
            @Override
            public void onSuccess(InitWeather.ResponseValue response) {
                view.setMyCities(response.cities);
                for (WeatherData data : response.weatherDatas) {
                    view.addWeather(data);
                }
                view.stopFresh();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void addWeather(WeatherData data) {
        view.addWeather(data);
    }

    @Override
    public void initCities() {
        useCaseHandler.execute(initCitiesUseCase,new InitCities.RequestValues(),new UseCase.UseCaseCallback<InitCities.ResponseValue>(){

            @Override
            public void onSuccess(InitCities.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void pickWeather(String cityId) {
        useCaseHandler.execute(pickWeatherDataUseCase, new PickWeatherData.RequestValues(cityId), new UseCase.UseCaseCallback<PickWeatherData.ResponseValue>() {
            @Override
            public void onSuccess(PickWeatherData.ResponseValue response) {
                addWeather(response.weatherData);
            }

            @Override
            public void onError() {

            }
        });
    }
}
