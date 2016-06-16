package com.zqlite.android.weather_lite.ui.citylist;

import com.zqlite.android.mvpc.IContract;
import com.zqlite.android.mvpc.IPresenter;
import com.zqlite.android.mvpc.IView;
import com.zqlite.android.weather_lite.entity.CityData;

import java.util.List;

/**
 * Created by scott on 6/16/16.
 */
public class AddCityContract implements IContract {

    interface Presenter extends IPresenter{
        void search(String key);
    }

    interface View extends IView<AddCityContract.Presenter>{
        void result(List<CityData> datas);
    }
}
