package com.example.rakshit.sunshine;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rakshit.sunshine.data.WeatherContract;
import com.example.rakshit.sunshine.sync.SunshineSyncAdapter;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    ForecastAdapter adapter;
    private static final int NETWORK_LOADER_ID = 1;
    private static final int CURSOR_LOADER_ID = 2;
    public int flag = 2;
    LoaderManager loaderManager;
    View rootView;
    private String url = "";
    private boolean set = false;

    Utility utility;

    public MainActivityFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        utility = new Utility(getContext());
        loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(CURSOR_LOADER_ID, null, this);

        adapter = new ForecastAdapter(getActivity(), null);
        ListView forecastList = (ListView)rootView.findViewById(R.id.lv_forecast);
        forecastList.setAdapter(adapter);

        forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id)
            {
                Intent intent = new Intent(getContext(), DetailActivity.class).setData(ContentUris.withAppendedId(WeatherContract.WeatherEntries.CONTENT_URI, id));
                TextView tv_day = (TextView) view.findViewById(R.id.tv_day);
                String day = tv_day.getText().toString().trim();
                if(day.contains("Today"))
                {
                    day = "TODAY";
                }
                intent.putExtra("day", day);
                startActivity(intent);
            }
        });

        return rootView;
    }

    //Loader methods
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        if(i==1)
        {
            return new DataLoader(getActivity(), utility.getURL());
        }

        else
        {
            return new CursorLoader(getContext(), WeatherContract.WeatherEntries.buildUriWithLocation(utility.getLocation()), null, null, null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor forecastDatas)
    {
        Log.d("main", "load finished");
        rootView.findViewById(R.id.progress).setVisibility(View.GONE);
        rootView.findViewById(R.id.lv_forecast).setVisibility(View.VISIBLE);
        if(flag==2)
        {
            if(forecastDatas.getCount()==0)
            {
                ConnectivityManager conn_manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo net_info = conn_manager.getActiveNetworkInfo();
                if(net_info!=null && net_info.isConnected())
                {
                    flag = 1;
                    loaderManager.initLoader(NETWORK_LOADER_ID, null, this);
                }
            }
            else
                adapter.swapCursor(forecastDatas);
        }
        else
        {
            if(forecastDatas != null && forecastDatas.getCount() > 0)
            {
                adapter.swapCursor(forecastDatas);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_settings)
        {
            Intent i = new Intent(getContext(), SettingsActivity.class);
            startActivity(i);
        }
        else if(id == R.id.menu_refresh)
        {
            rootView.findViewById(R.id.lv_forecast).setVisibility(View.GONE);
            rootView.findViewById(R.id.progress).setVisibility(View.VISIBLE);

            ConnectivityManager conn_manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net_info = conn_manager.getActiveNetworkInfo();
            if(net_info!=null && net_info.isConnected())
            {
                flag=1;
//                loaderManager.restartLoader(NETWORK_LOADER_ID, null, this);
                SunshineSyncAdapter.syncImmediately(getContext());
            }
        }

        else if(id == R.id.menu_map)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String location = preferences.getString(getString(R.string.location_key), getString(R.string.location_default_value));
            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", location).build();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(geoLocation);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null)
                startActivity(intent);
            else
                Toast.makeText(getContext(), "No Maps application installed on your device", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
