package com.example.rakshit.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    Uri uri;
    private ShareActionProvider shareActionProvider;
    String shareable="";

    TextView tv_date;
    TextView tv_max;
    TextView tv_min;
    TextView tv_desc;
    ImageView tv_icon;
    TextView tv_humidity;
    TextView tv_wind;
    TextView tv_pressure;

    private String[] direction = {"N", "NE", "E", "SE", "S", "SW", "W", "NW" };

    public DetailFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        uri = getActivity().getIntent().getData();

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);

        tv_date = (TextView) rootView.findViewById(R.id.tv_day);
        tv_max = (TextView) rootView.findViewById(R.id.tv_max_temp);
        tv_min = (TextView) rootView.findViewById(R.id.tv_min_temp);
        tv_desc = (TextView) rootView.findViewById(R.id.tv_desc);
        tv_icon = (ImageView) rootView.findViewById(R.id.icon);
        tv_humidity = (TextView) rootView.findViewById(R.id.tv_humidity);
        tv_wind = (TextView) rootView.findViewById(R.id.tv_wind);
        tv_pressure = (TextView) rootView.findViewById(R.id.tv_pressure);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if(shareActionProvider != null)
            shareActionProvider.setShareIntent(createShareIntent(shareable));
    }

    private Intent createShareIntent(String msg)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg + " #Sunshine App");
        return shareIntent;
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(getContext(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        data.moveToFirst();

        String date = new SimpleDateFormat("MMM dd").format(new Date(data.getLong(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_DATE)) * 1000L));
        String max = getString(R.string.format_temp, (double)Math.round(data.getDouble(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_MAX_TEMP))));
        String min = getString(R.string.format_temp, (double)Math.round(data.getDouble(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_MIN_TEMP))));
        String desc = data.getString(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_SHORT_DESC));
        String humidity = "Humidity: " + String.valueOf(Math.round(data.getDouble(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_HUMIDITY)))) + "%";
        double windspeed = data.getDouble(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_WIND_SPEED));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unit = preferences.getString(getString(R.string.units_key), getString(R.string.units_default_value));
        if (unit.equals(getString(R.string.units_celsius_value)))
        {
            windspeed*=3.6;
            unit = " km/h  ";
        }
        else if (unit.equals(getString(R.string.units_fahrenheit_value))) {
            windspeed *= 2.23693629;
            unit = " mph  ";
        }
        String wind = "Wind: " + String.valueOf(windspeed) + unit + direction[Math.round(data.getInt(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_WIND_DIRECTION))/45)%8];
        String pressure = "Pressure: " + String.valueOf(data.getDouble(data.getColumnIndexOrThrow(WeatherEntries.COLUMN_PRESSURE))) + " hPa";

        tv_date.setText(date);
        tv_max.setText(max);
        tv_min.setText(min);
        tv_desc.setText(desc);
        tv_humidity.setText(humidity);
        tv_wind.setText(wind);
        tv_pressure.setText(pressure);
        shareable = date+"\n"+max+"/"+min+" - "+desc+"\n"+humidity+"\n"+wind+"\n"+pressure;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }
}
