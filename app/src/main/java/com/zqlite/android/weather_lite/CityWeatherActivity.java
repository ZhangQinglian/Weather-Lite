package com.zqlite.android.weather_lite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zqlite.android.weather_lite.constant.WeatherConstant;
import com.zqlite.android.weather_lite.entity.HeWeatherBuilder;
import com.zqlite.android.weather_lite.entity.WeatherData;

import java.util.Calendar;
import java.util.Date;

public class CityWeatherActivity extends AppCompatActivity {

    private TextView weatherCond;

    private TextView weatherTmp;

    private View weatherBg;

    private ImageView weatherIcon;

    private LinearLayout weatherDaily;

    private View infoContainder;

    private RecyclerView hourForecast;

    private TextView weatherToday;

    public static final String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        infoContainder = findViewById(R.id.info_container);
        weatherBg = findViewById(R.id.weather_bg);

        Intent intent = getIntent();
        WeatherData weatherData = (WeatherData) intent.getExtras().getSerializable("weather");
        Log.d("weather", weatherData.toString());
        weatherBg.setBackground(getDrawable(WeatherData.getColorByCode(Integer.parseInt(weatherData.cond[0]))));
        infoContainder.setBackground(getDrawable(WeatherData.getColorByCode(Integer.parseInt(weatherData.cond[0]))));
        weatherCond = (TextView) findViewById(R.id.weather_cond);
        weatherTmp = (TextView) findViewById(R.id.weather_tmp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        weatherCond.setText(weatherData.cond[1]);
        weatherTmp.setText(weatherData.fl);
        weatherIcon.setImageDrawable(getDrawable(WeatherData.getWeatherIconDrawableId(Integer.parseInt(weatherData.cond[0]))));
        getSupportActionBar().setTitle(weatherData.city);

        weatherDaily = (LinearLayout) findViewById(R.id.daily_forecast_content);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Log.d("weather", " week : "  + week);
        int i = 0;
        for (HeWeatherBuilder.DailyForecast dailyForecast : weatherData.dailyForecasts) {
            Hodler hodler;
            View convertView = LayoutInflater.from(CityWeatherActivity.this).inflate(R.layout.daily_forecast_item, null);
            hodler = new Hodler();
            hodler.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            hodler.date = (TextView) convertView.findViewById(R.id.item_date);
            hodler.tmp = (TextView) convertView.findViewById(R.id.item_tmp);
            convertView.setTag(hodler);
            hodler = (Hodler) convertView.getTag();
            String tmp = dailyForecast.tmp[0] + "  " + dailyForecast.tmp[1];
            hodler.tmp.setText(tmp);
            hodler.date.setText(WEEKS[(7+week + i)%7]);
            hodler.icon.setImageDrawable(getDrawable(WeatherData.getWeatherIcon80DrawableId(Integer.parseInt(dailyForecast.cond[0]))));
            weatherDaily.addView(convertView);
            i++;
        }

        hourForecast = (RecyclerView) findViewById(R.id.hour_forecast);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        hourForecast.setHasFixedSize(true);
        hourForecast.setLayoutManager(linearLayoutManager);
        hourForecast.setAdapter(new HourForecastAdapter(weatherData.hourlyForecasts));

        weatherToday = (TextView) findViewById(R.id.weather_today);
        StringBuilder sb = new StringBuilder();
        sb.append("今天：")
                .append("风力")
                .append(weatherData.wind)
                .append("\n现在")
                .append(weatherData.cond[1])
                .append("。最高气温")
                .append(weatherData.dailyForecasts[0].tmp[0])
                .append(WeatherConstant.CELSIUS_DEGRESS)
                .append("\n").append("今晚")
                .append(weatherData.dailyForecasts[0].cond[3])
                .append(",最低气温")
                .append(weatherData.dailyForecasts[0].tmp[1])
                .append(WeatherConstant.CELSIUS_DEGRESS);
        weatherToday.setText(sb.toString());

        LinearLayout.LayoutParams LLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        LLP.setMargins(80,10,80,10);
        LinearLayout comf = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        comf.setLayoutParams(LLP);
        ((TextView)(comf.findViewById(R.id.suggestion_name))).setText("舒适度指数：");
        ((TextView)(comf.findViewById(R.id.brf))).setText(weatherData.suggestion.comf[0]);
        ((TextView)(comf.findViewById(R.id.txt))).setText(weatherData.suggestion.comf[1]);
        weatherDaily.addView(comf);

        LinearLayout cw = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        cw.setLayoutParams(LLP);
        ((TextView)(cw.findViewById(R.id.suggestion_name))).setText("洗车指数：");
        ((TextView)(cw.findViewById(R.id.brf))).setText(weatherData.suggestion.cw[0]);
        ((TextView)(cw.findViewById(R.id.txt))).setText(weatherData.suggestion.cw[1]);
        weatherDaily.addView(cw);

        LinearLayout drsg = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        drsg.setLayoutParams(LLP);
        ((TextView)(drsg.findViewById(R.id.suggestion_name))).setText("穿衣指数：");
        ((TextView)(drsg.findViewById(R.id.brf))).setText(weatherData.suggestion.drsg[0]);
        ((TextView)(drsg.findViewById(R.id.txt))).setText(weatherData.suggestion.drsg[1]);
        weatherDaily.addView(drsg);

        LinearLayout flu = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        flu.setLayoutParams(LLP);
        ((TextView)(flu.findViewById(R.id.suggestion_name))).setText("感冒指数：");
        ((TextView)(flu.findViewById(R.id.brf))).setText(weatherData.suggestion.flu[0]);
        ((TextView)(flu.findViewById(R.id.txt))).setText(weatherData.suggestion.flu[1]);
        weatherDaily.addView(flu);

        LinearLayout sport = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        sport.setLayoutParams(LLP);
        ((TextView)(sport.findViewById(R.id.suggestion_name))).setText("运动指数：");
        ((TextView)(sport.findViewById(R.id.brf))).setText(weatherData.suggestion.sport[0]);
        ((TextView)(sport.findViewById(R.id.txt))).setText(weatherData.suggestion.sport[1]);
        weatherDaily.addView(sport);

        LinearLayout trav = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        trav.setLayoutParams(LLP);
        ((TextView)(trav.findViewById(R.id.suggestion_name))).setText("旅游指数：");
        ((TextView)(trav.findViewById(R.id.brf))).setText(weatherData.suggestion.trav[0]);
        ((TextView)(trav.findViewById(R.id.txt))).setText(weatherData.suggestion.trav[1]);
        weatherDaily.addView(trav);

        LinearLayout uv = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.suggestion_item,null);
        uv.setLayoutParams(LLP);
        ((TextView)(uv.findViewById(R.id.suggestion_name))).setText("紫外线指数：");
        ((TextView)(uv.findViewById(R.id.brf))).setText(weatherData.suggestion.uv[0]);
        ((TextView)(uv.findViewById(R.id.txt))).setText(weatherData.suggestion.uv[1]);
        weatherDaily.addView(uv);

    }

    private class HourForecastAdapter extends RecyclerView.Adapter<MyHolder> {


        HeWeatherBuilder.HourlyForecast[] hourlyForecasts = new HeWeatherBuilder.HourlyForecast[0];

        public HourForecastAdapter(HeWeatherBuilder.HourlyForecast[] datas) {
            if (datas != null && datas.length > 0) {
                hourlyForecasts = datas;
            }
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyHolder holder = new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            HeWeatherBuilder.HourlyForecast hourlyForecast = hourlyForecasts[position];
            holder.tmp.setText(hourlyForecast.tmp + WeatherConstant.CELSIUS_DEGRESS);
            holder.date.setText(hourlyForecast.date.split(" ")[1]);
            holder.pop.setText(hourlyForecast.pop + " %");
        }

        @Override
        public int getItemCount() {
            return hourlyForecasts.length;
        }


    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView tmp;
        TextView pop;
        public MyHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.item_date);
            tmp = (TextView) itemView.findViewById(R.id.item_tmp);
            pop = (TextView) itemView.findViewById(R.id.item_pop);
        }
    }

    private class Hodler {

        TextView date;

        TextView tmp;

        ImageView icon;

    }
}
