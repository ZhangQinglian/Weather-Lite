package com.zqlite.android.weather_lite;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zqlite.android.weather_lite.constant.WeatherConstant;
import com.zqlite.android.weather_lite.dao.HeWeatherPickerImpl;
import com.zqlite.android.weather_lite.database.WeatherDatabaseHelper;
import com.zqlite.android.weather_lite.entity.CityData;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherBuilder;
import com.zqlite.android.weather_lite.entity.WeatherData;
import com.zqlite.android.weather_lite.service.WeatherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends RichActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SEARCH_CITY_CODE = 12;

    public static final String SEARCH_CITY_KEY = "search_city_key";

    private WeatherService weatherService;

    private RecyclerView weatherGrid;
    private WeatherAdaper weatherAdaper;
    private List<MyCity> gMyCities;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, fab, "aaaaa");
                    startActivityForResult(intent, SEARCH_CITY_CODE, options.toBundle());
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        weatherService = MyApplication.getInstance().getWeatherService();
        if (isNetWorkAccess()) {
            if (!MyApplication.getInstance().isInitCities()) {
                //初始化城市列表
                weatherService.initCities(WeatherConstant.CITY_SEARCH_TYPE_ALL_CHINA, new HeWeatherPickerImpl.InitCitiesCallback() {
                    @Override
                    public void initSuccess() {
                        MyApplication.getInstance().setInitCities(true);
                    }

                    @Override
                    public void initFailure() {
                        MyApplication.getInstance().setInitCities(false);
                    }
                });
            }
        } else {
            toast("无网络，2秒后自动退出");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }

        initView();
        initWeather();
    }

    //初始化布局
    private void initView() {
        weatherGrid = (RecyclerView) findViewById(R.id.weather_grid);
        weatherGrid.setHasFixedSize(true);
        weatherAdaper = new WeatherAdaper();
        weatherGrid.setAdapter(weatherAdaper);
        GridLayoutManager gm = new GridLayoutManager(this, 2);

        weatherGrid.setLayoutManager(gm);
        weatherGrid.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchHelper.attachToRecyclerView(weatherGrid);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_content);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetWorkAccess()) {
                    toast("无网络，请尝试联网并下拉刷新");
                    swipeRefreshLayout.setRefreshing(false);
                    return;
                }
                initWeather();
            }
        });

    }

    //初始化天气
    private void initWeather() {
        if (!isNetWorkAccess()) {
            return;
        }
        weatherAdaper.removeALl();
        weatherService.getAllMyCities(new Action1<List<MyCity>>() {
            @Override
            public void call(List<MyCity> cities) {
                gMyCities = cities;
                List<String> cityIds = new ArrayList<String>();
                for (MyCity city : cities) {
                    cityIds.add(city.cityId);
                }
                weatherService.pickWeathers(cityIds, new Action1<List<WeatherData>>() {
                    @Override
                    public void call(List<WeatherData> weatherDatas) {
                        for (WeatherData data : weatherDatas) {
                            weatherAdaper.addWeatherData(data);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initWeather();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_CITY_CODE) {
            if (resultCode == RESULT_OK) {
                final String cityId = data.getExtras().getString(SEARCH_CITY_KEY);
                if (gMyCities != null ) {
                    for(MyCity myCity : gMyCities){
                        if(myCity.cityId.equals(cityId)){
                            toast("该城市已存在");
                            return;
                        }
                    }
                }
                Log.d("weather", "  cityId = " + cityId);
                weatherService.pick(cityId, new Action1<WeatherBuilder>() {
                    @Override
                    public void call(WeatherBuilder weatherBuilder) {
                        weatherService.addMyCity(cityId);
                        final WeatherData weatherData = weatherBuilder.build();
                        Log.d("weather", "  weatherData = " + weatherData.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                weatherAdaper.addWeatherData(weatherData);
                            }
                        });
                    }
                });
            }
        }
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
}
