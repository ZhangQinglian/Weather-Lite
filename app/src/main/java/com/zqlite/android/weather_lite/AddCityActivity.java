package com.zqlite.android.weather_lite;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.zqlite.android.weather_lite.entity.CityData;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class AddCityActivity extends RichActivity {

    private ListView resultCities ;
    private SearchResultAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        SearchView searchBox = (SearchView) findViewById(R.id.search_box);
        assert searchBox != null;
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                if(newText != null && newText.trim().length() > 1){
                    MyApplication.getInstance().getWeatherService().searchCities(newText.trim(), new Action1<List<CityData>>() {
                        @Override
                        public void call(final List<CityData> cityDatas) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setResult(cityDatas);
                                }
                            });
                        }
                    });
                }else{
                    adapter.removeAll();
                }
                return false;
            }
        });

        resultCities = (ListView) findViewById(R.id.search_result);
        adapter = new SearchResultAdapter();
        resultCities.setAdapter(adapter);
        resultCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityData cityData = (CityData) parent.getItemAtPosition(position);
                //Log.d("weather", cityData.toString());
                Intent intent = new Intent();
                intent.putExtra(MainActivity.SEARCH_CITY_KEY, cityData.city_id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private class SearchResultAdapter extends BaseAdapter{


        private List<CityData> result = new ArrayList<>();

        public void setResult(List<CityData> cityDatas){
            result.clear();
            result.addAll(cityDatas);
            notifyDataSetChanged();
        }

        public void removeAll(){
            result.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return result.size();
        }

        @Override
        public Object getItem(int position) {
            return result.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CityHolder holder ;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.search_result_item,null);
                holder = new CityHolder();
                holder.searchResultItem = (TextView) convertView.findViewById(R.id.search_result_item);
                convertView.setTag(holder);
            }
            holder = (CityHolder) convertView.getTag();

            holder.searchResultItem.setText(result.get(position).cityName + "(" + result.get(position).prov +")" );
            return convertView;
        }

        private class CityHolder {
            public TextView searchResultItem ;

        }
    }

}
