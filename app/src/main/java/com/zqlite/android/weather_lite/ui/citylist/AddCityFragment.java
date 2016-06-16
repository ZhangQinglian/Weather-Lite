package com.zqlite.android.weather_lite.ui.citylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.zqlite.android.weather_lite.R;
import com.zqlite.android.weather_lite.entity.CityData;
import com.zqlite.android.weather_lite.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by scott on 6/16/16.
 */
public class AddCityFragment extends Fragment implements AddCityContract.View{

    private ListView resultCities ;
    private SearchResultAdapter adapter ;
    private AddCityContract.Presenter presenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_city_fragment,null,false);
        SearchView searchBox = (SearchView) view.findViewById(R.id.search_box);
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
                    presenter.search(newText);
                }else{
                    adapter.removeAll();
                }
                return false;
            }
        });

        resultCities = (ListView) view.findViewById(R.id.search_result);
        adapter = new SearchResultAdapter();
        resultCities.setAdapter(adapter);
        resultCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityData cityData = (CityData) parent.getItemAtPosition(position);
                //Log.d("weather", cityData.toString());
                Intent intent = new Intent();
                intent.putExtra(MainActivity.SEARCH_CITY_KEY, cityData.city_id);
                getActivity().setResult(MainActivity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void result(final List<CityData> datas) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setResult(datas);
            }
        });
    }

    @Override
    public void setPresenter(AddCityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private class SearchResultAdapter extends BaseAdapter {


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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item,null);
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
