package com.zqlite.android.weather_lite.ui.citylist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zqlite.android.mvpc.UseCaseHandler;
import com.zqlite.android.weather_lite.R;
import com.zqlite.android.weather_lite.RichActivity;

public class AddCityActivity extends RichActivity {


    private AddCityPresenter presenter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        AddCityFragment fragment = new AddCityFragment();
        UseCaseHandler useCaseHandler = UseCaseHandler.getInstance();
        presenter = new AddCityPresenter(fragment,useCaseHandler);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
