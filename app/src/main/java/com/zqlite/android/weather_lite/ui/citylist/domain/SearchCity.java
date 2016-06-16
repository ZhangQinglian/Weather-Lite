package com.zqlite.android.weather_lite.ui.citylist.domain;

import com.zqlite.android.mvpc.UseCase;
import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.entity.CityData;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by scott on 6/16/16.
 */
public class SearchCity extends UseCase<SearchCity.RequestValues,SearchCity.ReponseValue> {


    @Override
    protected void executeUseCase(RequestValues requestValues) {

        MyApplication.getInstance().getWeatherService().searchCities(requestValues.key, new Action1<List<CityData>>() {
            @Override
            public void call(final List<CityData> cityDatas) {
                getUseCaseCallback().onSuccess(new SearchCity.ReponseValue(cityDatas));
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValues{
        public final String key;
        public RequestValues(String key){
            this.key = key;
        }
    }

    public static final class ReponseValue implements UseCase.ResponseValue{
        public final List<CityData> cityDatas ;

        public ReponseValue(List<CityData> datas){
            this.cityDatas = datas;
        }
    }
}
