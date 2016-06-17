package com.zqlite.android.weather_lite.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zqlite.android.weather_lite.ui.city.CityWeatherActivity;
import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.R;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherData;
import com.zqlite.android.weather_lite.utils.GroceryStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scott on 6/16/16.
 */
public class WeathersFragment extends Fragment implements MainContract.View{

    private MainContract.Presenter presenter;

    WeatherAdaper weatherAdaper ;

    private List<MyCity> gMyCities;

    SwipeRefreshLayout swipeRefreshLayout;

    Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gMyCities = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.content_main,null,false);
        RecyclerView weatherGrid = (RecyclerView) view.findViewById(R.id.weather_grid);
        weatherGrid.setHasFixedSize(true);
        weatherAdaper = new WeatherAdaper();
        weatherGrid.setAdapter(weatherAdaper);
        GridLayoutManager gm = new GridLayoutManager(getContext(), 2);

        weatherGrid.setLayoutManager(gm);
        weatherGrid.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchHelper.attachToRecyclerView(weatherGrid);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_content);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!GroceryStore.isNetWorkAccess(getContext())) {
                    GroceryStore.toast("无网络，请尝试联网并下拉刷新",getContext());
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                presenter.start();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                if (!GroceryStore.isNetWorkAccess(getContext())) {
                    GroceryStore.toast("无网络，请尝试联网并下拉刷新",getContext());
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                presenter.start();
            }
        },1000);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void removeAll() {
        weatherAdaper.removeALl();
    }

    @Override
    public void addWeather(WeatherData data) {
        weatherAdaper.addWeatherData(data);
    }

    @Override
    public void stopFresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void setMyCities(List<MyCity> cities) {
        gMyCities = cities;
    }

    public static class WeatherAdaper extends RecyclerView.Adapter<WeatherAdaper.MyViewHolder> {


        private List<WeatherData> weatherDatas = new ArrayList<>();

        public WeatherAdaper() {

        }

        public void removeALl() {

            weatherDatas.clear();
            notifyDataSetChanged();

        }

        public void addWeatherDataWithPosition(WeatherData weatherData, int position) {
            //TODO 排序
            if (weatherDatas.size() < position) {
                addWeatherData(weatherData);
            } else {

            }
            weatherDatas.add(position, weatherData);
            notifyItemInserted(position);
        }

        public void addWeatherData(WeatherData weatherData) {
            for (WeatherData data : weatherDatas) {
                if (data.city.equals(weatherData.city)) {
                    return;
                }
            }
            weatherDatas.add(weatherData);
            notifyItemInserted(weatherDatas.size());
        }

        public void swape(int origin,int target){
            Log.d("weather", "src : " + origin + "   tar : " + target) ;
            if (origin < target) {
                for (int i = origin; i < target; i++) {
                    Collections.swap(weatherDatas, i, i + 1);
                }
            }
            if (origin > target) {
                for (int i = origin; i > target; i--) {
                    int l1 = i ;
                    int l2 = i - 1;
                    Log.d("weather", " swap " + l1 + " and " + l2);
                    Collections.swap(weatherDatas, i, i - 1);
                }
            }
            notifyItemMoved(origin, target);
            List<MyCity> myCities = new ArrayList<>();
            for(int i = 0;i<weatherDatas.size();i++){
                Log.d("weather","city name : " + weatherDatas.get(i).city +" city id: " + weatherDatas.get(i).cityId + " index = " + i);
                myCities.add(new MyCity(weatherDatas.get(i).cityId,String.valueOf(i)));
            }
            MyApplication.getInstance().getWeatherService().reStoreMyCitiesIndex(myCities);
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_city_weather_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final WeatherData data = weatherDatas.get(position);
            holder.weaatherCity.setText(data.city);
            holder.weathertmp.setText(data.fl);
            holder.weatherCond.setText(data.cond[1]);
            holder.weatherWind.setText(data.wind);
            holder.weatherHum.setText(data.hum);
            holder.weatherPres.setText(data.pres);
            Uri uri = Uri.parse(WeatherData.getWeatherIconUriByCode(Integer.parseInt(data.cond[0])));
            holder.weatherIcon.setImageDrawable(holder
                    .weatherIcon.getContext()
                    .getDrawable(WeatherData.getWeatherIconDrawableId(Integer.parseInt(data.cond[0]))));

            setWeatherViewColor(holder, data);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context,CityWeatherActivity.class);
                    intent.putExtra("weather",data);
                    context.startActivity(intent);
                }
            });
        }

        private void setWeatherViewColor(MyViewHolder holder, WeatherData data) {
            holder.weatherBG.setBackgroundColor(holder.weatherBG.getContext().getResources().getColor(WeatherData.getColorByCode(Integer.parseInt(data.cond[0]))));
            int textColor = holder.weatherBG.getContext().getResources().getColor(R.color.white);
            holder.weaatherCity.setTextColor(textColor);
            holder.weathertmp.setTextColor(textColor);
            holder.weatherCond.setTextColor(textColor);

        }

        @Override
        public int getItemCount() {
            return weatherDatas.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {


            public View weatherBG;

            public ImageView weatherIcon;

            public TextView weatherCond;

            public TextView weathertmp;

            public TextView weaatherCity;

            public TextView weatherWind;

            public TextView weatherHum;

            public TextView weatherPres;

            public MyViewHolder(View itemView) {
                super(itemView);
                weatherBG = itemView.findViewById(R.id.weather_bg);
                weatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
                weatherCond = (TextView) itemView.findViewById(R.id.weather_cond);
                weathertmp = (TextView) itemView.findViewById(R.id.weather_tmp);
                weaatherCity = (TextView) itemView.findViewById(R.id.weather_city);
                weatherWind = (TextView) itemView.findViewById(R.id.weather_wind);
                weatherHum = (TextView) itemView.findViewById(R.id.weather_hum);
                weatherPres = (TextView) itemView.findViewById(R.id.weather_pres);
            }
        }
    }

    ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, 0) {

        private View view ;
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int src = viewHolder.getAdapterPosition();
            int tar = target.getAdapterPosition();
            weatherAdaper.swape(src,tar);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                Log.d("scott","drag");
                ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleX", 1.0f, 1.2f);
                ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1.0f, 1.2f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
                animatorSet.start();
                view = viewHolder.itemView;
            }
            if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f);
                ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
                animatorSet.start();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.SEARCH_CITY_CODE) {
            if (resultCode == MainActivity.RESULT_OK) {
                final String cityId = data.getExtras().getString(MainActivity.SEARCH_CITY_KEY);
                if (gMyCities != null ) {
                    for(MyCity myCity : gMyCities){
                        if(myCity.cityId.equals(cityId)){
                            GroceryStore.toast("该城市已存在",getContext());
                            return;
                        }
                    }
                }
                Log.d("weather", "  cityId = " + cityId);
                presenter.pickWeather(cityId);
            }
        }
    }
}
