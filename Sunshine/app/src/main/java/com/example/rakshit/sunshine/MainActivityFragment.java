package com.example.rakshit.sunshine;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ForecastData>>
{
    ForecastAdapter adapter;
    private static final String WEATHER_REQUEST_URL =
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=Manipal&units=metric&appid="+BuildConfig.API_KEY;
    private static final int WEATHER_LOADER_ID = 1;

    public MainActivityFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ConnectivityManager conn_manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net_info = conn_manager.getActiveNetworkInfo();
        if(net_info!=null && net_info.isConnected())
        {
            LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.initLoader(WEATHER_LOADER_ID, null, this);
        }

        adapter = new ForecastAdapter(getActivity(), new ArrayList<ForecastData>());
        ListView forecastList = (ListView)rootView.findViewById(R.id.lv_forecast);
        forecastList.setAdapter(adapter);

        forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(getContext(), DetailActivity.class).putExtra("data", adapter.getItem(i));
                startActivity(intent);
            }
        });

        return rootView;
    }

    //Loader methods
    @Override
    public Loader<ArrayList<ForecastData>> onCreateLoader(int i, Bundle bundle)
    {
        return new DataLoader(getActivity(), WEATHER_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ForecastData>> loader, ArrayList<ForecastData> forecastDatas)
    {
        adapter.clear();
        if(forecastDatas!=null && !forecastDatas.isEmpty())
            adapter.addAll(forecastDatas);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ForecastData>> loader)
    {
        adapter.clear();
    }
}
