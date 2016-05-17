package com.zqlite.android.weather_lite.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zqlite.android.weather_lite.MyApplication;
import com.zqlite.android.weather_lite.database.WeatherDatabaseHelper;
import com.zqlite.android.weather_lite.entity.CityData;
import com.zqlite.android.weather_lite.entity.HeCitiesIniter;
import com.zqlite.android.weather_lite.entity.HeWeatherBuilder;
import com.zqlite.android.weather_lite.entity.MyCity;
import com.zqlite.android.weather_lite.entity.WeatherBuilder;
import com.zqlite.android.weather_lite.entity.WeatherData;
import com.zqlite.android.weather_lite.rest.HeWeatherREST;
import com.zqlite.android.weather_lite.utils.Trans2PinYin;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author qinglian.zhang
 */
public class HeWeatherPickerImpl extends WeatherPicker {


    public static final String[] CITY_ID_PROJECTION = {WeatherDatabaseHelper.CityColumns.CITY_ID};

    public static final String[] CITY_ALL_PROJECTION = WeatherDatabaseHelper.CityColumns.allColumns;

    public static final String CITY_ID_SELECTION = WeatherDatabaseHelper.CityColumns.CITY_ID + "=?";

    public interface InitCitiesCallback {
        void initSuccess();

        void initFailure();
    }

    private static HeWeatherPickerImpl sInstance = new HeWeatherPickerImpl();

    private HeWeatherREST heWeatherREST;

    public static HeWeatherPickerImpl getInstance() {
        return sInstance;
    }

    private HeWeatherPickerImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HeWeatherREST.HE_WEATHER_API).addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .build();
        heWeatherREST = retrofit.create(HeWeatherREST.class);
    }

    @Override
    public void pickWeather(String cityName, final Action1<WeatherBuilder> action) {
        heWeatherREST.getWeatherAsync(cityName, HeWeatherREST.API_KEY).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                try {
                    String body = responseBody.string();
                    //Log.d("weather", "body : " + body);
                    WeatherBuilder builder = new HeWeatherBuilder(body);
                    action.call(builder);
                } catch (IOException | JSONException e) {
                    action.call(null);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void pickWeathers(final List<String> keys, final Action1<List<WeatherData>> action) {
        MyApplication.getInstance().getSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<WeatherData> list = new ArrayList<>();
                for (String key : keys) {
                    Call<ResponseBody> responseBodyCall = heWeatherREST.getWeatherSync(key, HeWeatherREST.API_KEY);
                    try {
                        String body = responseBodyCall.execute().body().string();
                        WeatherBuilder builder = new HeWeatherBuilder(body);
                        WeatherData data = builder.build();
                        data.cityId = key;
                        list.add(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                action.call(list);
            }
        });
    }

    /**
     * 初始化数据库中的城市表
     * @param search 城市类型
     *               国内城市：allchina、 热门城市：hotworld、 全部城市：allworld
     * @param callback 初始化后的回调
     */
    @Override
    public void initCitiesList(String search, final HeWeatherPickerImpl.InitCitiesCallback callback) {
        heWeatherREST.getCitiesAsync(search, HeWeatherREST.API_KEY).subscribe(new Action1<ResponseBody>() {
            @Override
            public void call(ResponseBody responseBody) {
                try {
                    String body = responseBody.string();
                    Log.d("weather", "body : " + body);
                    HeCitiesIniter initer = new HeCitiesIniter(body);
                    List<CityData> cityDatas = initer.getCityDatas();
                    Log.d("weather", "city size = " + cityDatas.size());
                    WeatherDatabaseHelper databaseHelper = MyApplication.getInstance().getWeatherDatabaseHelper();
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    for (CityData cityData : cityDatas) {
                        ContentValues values = new ContentValues();
                        values.put(WeatherDatabaseHelper.CityColumns.CITY_ID, cityData.city_id);
                        values.put(WeatherDatabaseHelper.CityColumns.CITY_NAME, cityData.cityName);
                        values.put(WeatherDatabaseHelper.CityColumns.CITY_NAME_PINYIN, Trans2PinYin.getInstance().convertAll(cityData.cityName));
                        values.put(WeatherDatabaseHelper.CityColumns.CNTY_NAME, cityData.cntyName);
                        values.put(WeatherDatabaseHelper.CityColumns.LAT, cityData.lat);
                        values.put(WeatherDatabaseHelper.CityColumns.LON, cityData.lon);
                        values.put(WeatherDatabaseHelper.CityColumns.PROV, cityData.prov);
                        Cursor c = db.query(WeatherDatabaseHelper.TABLE_CHINA_CITIES, CITY_ID_PROJECTION, CITY_ID_SELECTION, new String[]{cityData.city_id}, null, null, null);
                        if (c != null) {
                            try {
                                if (c.getCount() > 0) {
                                    // Log.d("weather","  update");
                                    db.update(WeatherDatabaseHelper.TABLE_CHINA_CITIES, values, CITY_ID_SELECTION, new String[]{cityData.city_id});
                                } else {
                                    // Log.d("weather","  insert");
                                    db.insert(WeatherDatabaseHelper.TABLE_CHINA_CITIES, "", values);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                c.close();
                            }
                        } else {
                            // Log.d("weather","  insert");
                            db.insert(WeatherDatabaseHelper.TABLE_CHINA_CITIES, "", values);
                        }
                    }
                    callback.initSuccess();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    callback.initFailure();
                }
            }
        });
    }

    @Override
    public void getAllCities(Action1<List<CityData>> action) {
        Observable.create(new Observable.OnSubscribe<List<CityData>>() {
            @Override
            public void call(final Subscriber<? super List<CityData>> subscriber) {
                MyApplication.getInstance().getSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<CityData> cities = null;
                        WeatherDatabaseHelper databaseHelper = MyApplication.getInstance().getWeatherDatabaseHelper();
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        Cursor c = db.query(WeatherDatabaseHelper.TABLE_CHINA_CITIES, CITY_ALL_PROJECTION, null, null, null, null, null);
                        if (c != null) {
                            try {
                                cities = new ArrayList<CityData>();
                                while (c.moveToNext()) {
                                    CityData city = new CityData();
                                    cursor2City(city, c);
                                    cities.add(city);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                c.close();
                            }
                        }
                        subscriber.onStart();
                        subscriber.onNext(cities);
                    }
                });

            }
        }).subscribe(action);
    }

    @Override
    public void searchCities(final String key, Action1<List<CityData>> action) {
        Observable.create(new Observable.OnSubscribe<List<CityData>>() {
            @Override
            public void call(final Subscriber<? super List<CityData>> subscriber) {
                MyApplication.getInstance().getSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<CityData> cities = null;
                        WeatherDatabaseHelper databaseHelper = MyApplication.getInstance().getWeatherDatabaseHelper();
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        String where = WeatherDatabaseHelper.CityColumns.CITY_NAME + "=? OR " + WeatherDatabaseHelper.CityColumns.CITY_NAME_PINYIN + "=? OR ";
                        StringBuilder sb = new StringBuilder();
                        for(int i = 0;i<key.length();i++){
                            sb.append(key.substring(i,i+1)).append("%");
                        }
                        where += WeatherDatabaseHelper.CityColumns.CITY_NAME_PINYIN + " LIKE '" + sb.toString() + "'";
                        //Log.d("weather","where : " + where);

                        Cursor c = db.query(WeatherDatabaseHelper.TABLE_CHINA_CITIES, CITY_ALL_PROJECTION, where, new String[]{key,key}, null, null, null);
                        if (c != null) {
                            try {
                                cities = new ArrayList<CityData>();
                                while(c.moveToNext()){
                                    CityData city = new CityData();
                                    cursor2City(city,c);
                                    cities.add(city);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                c.close();
                            }
                        }
                        subscriber.onStart();
                        subscriber.onNext(cities);
                    }
                });

            }
        }).subscribe(action);
    }

    @Override
    public void addMyCity(final String cityId) {
        MyApplication.getInstance().getSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<MyCity> myCities = getMyCities();
                for (MyCity mycity : myCities) {
                    if (mycity.cityId.equals(cityId)) {
                        return;
                    }
                }

                WeatherDatabaseHelper databaseHelper = MyApplication.getInstance().getWeatherDatabaseHelper();
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(WeatherDatabaseHelper.CityColumns.CITY_ID, cityId);
                values.put(WeatherDatabaseHelper.MyCityColumns.INDEX, String.valueOf(myCities.size() + 1));
                db.insert(WeatherDatabaseHelper.TABLE_CHINA_MY_CITY, "", values);
            }
        });
    }

    @Override
    public void getAllMyCities(final Action1<List<MyCity>> action) {
        Observable.create(new Observable.OnSubscribe<List<MyCity>>() {
            @Override
            public void call(final Subscriber<? super List<MyCity>> subscriber) {
                MyApplication.getInstance().getSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onStart();
                        ;
                        subscriber.onNext(getMyCities());
                    }
                });
            }
        }).subscribe(action);
    }

    @Override
    public synchronized void reStoreMyCitiesIndex(final List<MyCity> myCities) {
        MyApplication.getInstance().getSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                for (MyCity myCity : myCities) {
                    ContentValues values = new ContentValues();
                    values.put(WeatherDatabaseHelper.CityColumns.CITY_ID,myCity.cityId);
                    values.put(WeatherDatabaseHelper.MyCityColumns.INDEX,myCity.index);
                    Log.d("weather","  city id : " + myCity.cityId + "   index : " + myCity.index);
                    SQLiteDatabase db = MyApplication.getInstance().getWeatherDatabaseHelper().getWritableDatabase();
                    db.update(WeatherDatabaseHelper.TABLE_CHINA_MY_CITY, values, WeatherDatabaseHelper.CityColumns.CITY_ID+"=?",new String[]{myCity.cityId});
                }
            }
        });
    }

    private List<MyCity> getMyCities(){
        List<MyCity> myCities = new ArrayList<>() ;
        WeatherDatabaseHelper databaseHelper = MyApplication.getInstance().getWeatherDatabaseHelper();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = db.query(WeatherDatabaseHelper.TABLE_CHINA_MY_CITY, new String[]{WeatherDatabaseHelper.CityColumns.CITY_ID,WeatherDatabaseHelper.MyCityColumns.INDEX}, null, null, null, null, null);
        if(c != null){
            try{
                while(c.moveToNext()){
                    myCities.add(new MyCity(c.getString(0), c.getString(1)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                c.close();
            }
        }else{
        }
        Collections.sort(myCities, new Comparator<MyCity>() {
            @Override
            public int compare(MyCity lhs, MyCity rhs) {
                return Integer.parseInt(lhs.index) - Integer.parseInt(rhs.index);
            }
        });
        return myCities;
    }
    private void cursor2City(CityData city,Cursor c){
        city.city_id = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.CITY_ID));
        city.cityName = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.CITY_NAME));
        city.cityNamePinyin = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.CITY_NAME_PINYIN));
        city.cntyName = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.CNTY_NAME));
        city.lat = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.LAT));
        city.lon = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.LON));
        city.prov = c.getString(c.getColumnIndex(WeatherDatabaseHelper.CityColumns.PROV));
    }
}
