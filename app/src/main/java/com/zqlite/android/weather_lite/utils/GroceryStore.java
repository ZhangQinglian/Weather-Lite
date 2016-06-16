package com.zqlite.android.weather_lite.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by scott on 6/16/16.
 */
public class GroceryStore {


    public static boolean isNetWorkAccess(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  cm.getActiveNetworkInfo();
        if(info != null){
            if(info.isAvailable()){
                return true;
            }
        }
        return false ;
    }

    public static void toast(int StringId,Context context){
        toast(context.getString(StringId),context);
    }

    public static void toast(String msg, Context context){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
