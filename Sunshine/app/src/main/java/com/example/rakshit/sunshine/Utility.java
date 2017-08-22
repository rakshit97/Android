package com.example.rakshit.sunshine;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

public class Utility
{
    private static final String WEATHER_REQUEST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private String URL;
    private String location;

    public Utility(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String location = preferences.getString(context.getString(R.string.location_key), context.getString(R.string.location_default_value)).toLowerCase();
        setLocation(location);

        Uri baseUri = Uri.parse(WEATHER_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", location);
        uriBuilder.appendQueryParameter("units", "metric");
        uriBuilder.appendQueryParameter("appid", BuildConfig.API_KEY);
        setURL(uriBuilder.toString());
    }

    private void setURL(String URL)
    {
        this.URL = URL;
    }

    public String getURL()
    {
        return URL;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }
}
