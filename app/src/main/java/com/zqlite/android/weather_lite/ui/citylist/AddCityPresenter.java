package com.zqlite.android.weather_lite.ui.citylist;

import com.zqlite.android.mvpc.UseCase;
import com.zqlite.android.mvpc.UseCaseHandler;
import com.zqlite.android.weather_lite.ui.citylist.domain.SearchCity;


/**
 * Created by scott on 6/16/16.
 */
public class AddCityPresenter implements AddCityContract.Presenter {

    private AddCityContract.View view ;

    private UseCaseHandler useCaseHandler;

    private SearchCity searchCityUseCase;
    public AddCityPresenter(AddCityContract.View view,UseCaseHandler useCaseHandler){
        this.view = view;
        this.useCaseHandler = useCaseHandler;
        view.setPresenter(this);
        searchCityUseCase = new SearchCity();

    }

    @Override
    public void start() {

    }

    @Override
    public void search(String key) {

        useCaseHandler.execute(searchCityUseCase, new SearchCity.RequestValues(key), new UseCase.UseCaseCallback<SearchCity.ReponseValue>() {
            @Override
            public void onSuccess(SearchCity.ReponseValue response) {
                view.result(response.cityDatas);
            }

            @Override
            public void onError() {

            }
        });

    }
}
