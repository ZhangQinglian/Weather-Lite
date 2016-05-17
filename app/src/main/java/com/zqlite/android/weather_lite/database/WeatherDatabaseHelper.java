package com.zqlite.android.weather_lite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author qingliang.zhang
 */
public class WeatherDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_CHINA_CITIES = "china_cities";

    public static final String TABLE_CHINA_MY_CITY = "china_my_city";

    public interface MyCityColumns {
        public static final String INDEX = "_index";
    }

    public interface CityColumns {
        public static final String _ID = "_id";

        public static final String CITY_ID = "city_id";

        public static final String CITY_NAME = "city_name";

        public static final String CITY_NAME_PINYIN = "city_name_pinyin";

        public static final String CNTY_NAME = "cnty_name";

        public static final String LAT = "lat";

        public static final String LON = "lon";

        public static final String PROV = "prov";

        public static final String[] allColumns = {_ID, CITY_ID, CITY_NAME, CITY_NAME_PINYIN, CNTY_NAME, LAT, LON, PROV};
    }

    public WeatherDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHINA_CITIES + " (" +
                CityColumns._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY," +
                CityColumns.CITY_ID + " TEXT," +
                CityColumns.CITY_NAME + " TEXT," +
                CityColumns.CITY_NAME_PINYIN + " TEXT," +
                CityColumns.CNTY_NAME + " TEXT," +
                CityColumns.LAT + " TEXT," +
                CityColumns.LON + " TEXT," +
                CityColumns.PROV + " TEXT" +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHINA_MY_CITY + " (" +
                CityColumns._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY," +
                CityColumns.CITY_ID + " TEXT," +
                MyCityColumns.INDEX + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
