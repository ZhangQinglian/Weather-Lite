package com.zqlite.android.weather_lite;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author qinglian.zhang
 */
public class RichActivity extends AppCompatActivity {




    public boolean isNetWorkAccess(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  cm.getActiveNetworkInfo();
        if(info != null){
            if(info.isAvailable()){
                return true;
            }
        }
        return false ;
    }

    public void toast(int StringId){
        toast(getString(StringId));
    }

    public void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
