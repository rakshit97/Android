package com.example.rakshit.sunshine;

import android.content.ContentValues;
import android.util.Log;

import com.example.rakshit.sunshine.data.WeatherContract.LocationEntries;
import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class QueryUtil
{
    public static Vector<Vector<ContentValues>> fetchData(String url_string, String city)
    {
        URL url = createURL(url_string);
        String JSONResponse = null;
        try
        {
            JSONResponse = makeHTTPRequest(url);
        }
        catch (IOException e)
        {
            Log.e("QueryUtil", "Error in getting data from server");
        }

        return extractData(JSONResponse, city);
    }

    private static URL createURL(String url_string)
    {
        URL url = null;
        try
        {
            url = new URL(url_string);
        }
        catch(MalformedURLException e)
        {
            Log.e("QueryUtil", "Error forming URL");
        }
        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException
    {
        if(url==null)
            return null;

        String JSONResponse = null;
        HttpURLConnection connection = null;
        InputStream dataStream = null;

        try
        {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 200)
            {
                dataStream = connection.getInputStream();
                JSONResponse = readFromStream(dataStream);
            }
            else
                Log.e("QueryUtils", "Error! Response code: " + connection.getResponseCode());
        }
        catch (IOException e)
        {
            Log.e("QueryUtils", "Error getting response");
        }
        finally
        {
            if(connection!=null)
                connection.disconnect();
            if(dataStream!=null)
                dataStream.close();
            return JSONResponse;
        }
    }

    private static String readFromStream(InputStream dataStream) throws IOException
    {
        if(dataStream==null)
            return null;
        StringBuilder response = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(dataStream);
        BufferedReader reader = new BufferedReader(streamReader);
        String line = reader.readLine();
        while(line!=null && !line.isEmpty())
        {
            response.append(line);
            line = reader.readLine();
            if(line==null || line.isEmpty())
                break;
        }
        return response.toString();
    }

    private static Vector<Vector<ContentValues>> extractData(String JSONResponse, String c)
    {
        if(JSONResponse == null || JSONResponse.isEmpty())
            return null;

        Vector<Vector<ContentValues>> combined = new Vector<>(2);

        try
        {
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray list = root.getJSONArray("list");
            JSONObject cityObj = root.getJSONObject("city");
            long cityId = cityObj.getLong("id");
            String cityName = cityObj.getString("name");
            if(!cityName.equals(c))
                cityName = c;
            JSONObject cityCoords = cityObj.getJSONObject("coord");
            double longitude = cityCoords.getDouble("lon");
            double latitude = cityCoords.getDouble("lat");
            Vector<ContentValues> cvv = new Vector<>(list.length());
            ContentValues cv_city = new ContentValues();
            Vector<ContentValues> city = new Vector<>(1);

            for(int i=0;i<list.length();i++)
            {
                JSONObject item = list.getJSONObject(i);
                long time = item.getLong("dt");
                double pressure = item.getDouble("pressure");
                double humidity = item.getDouble("humidity");
                double windSpeed = item.getDouble("speed");
                double windDir = item.getDouble("deg");
                JSONObject temp = item.getJSONObject("temp");
                double temp_min = temp.getDouble("min");
                double temp_max = temp.getDouble("max");
                String shortDesc = item.getJSONArray("weather").getJSONObject(0).getString("main");
                String longDesc = item.getJSONArray("weather").getJSONObject(0).getString("description");
                int weatherId = item.getJSONArray("weather").getJSONObject(0).getInt("id");
                String arr[] = longDesc.split(" ");
                StringBuilder builder = new StringBuilder();
                for(int j=0;j<arr.length;j++)
                {
                    builder.append(Character.toUpperCase(arr[j].charAt(0))).append(arr[j].substring(1)).append(" ");
                }
                longDesc = builder.toString().trim();
                ContentValues cv = new ContentValues();
                cv.put(WeatherEntries._ID, i+1);
                cv.put(WeatherEntries.COLUMN_LOC_KEY, cityId);
                cv.put(WeatherEntries.COLUMN_DATE, time);
                cv.put(WeatherEntries.COLUMN_MAX_TEMP, temp_max);
                cv.put(WeatherEntries.COLUMN_MIN_TEMP, temp_min);
                cv.put(WeatherEntries.COLUMN_WEATHER_ID, weatherId);
                cv.put(WeatherEntries.COLUMN_SHORT_DESC, shortDesc);
                cv.put(WeatherEntries.COLUMN_LONG_DESC, longDesc);
                cv.put(WeatherEntries.COLUMN_HUMIDITY, humidity);
                cv.put(WeatherEntries.COLUMN_PRESSURE, pressure);
                cv.put(WeatherEntries.COLUMN_WIND_SPEED, windSpeed);
                cv.put(WeatherEntries.COLUMN_WIND_DIRECTION, windDir);
                cvv.add(cv);
            }
            cv_city.put(LocationEntries._ID, cityId);
            cv_city.put(LocationEntries.COLUMN_CITY, cityName.toLowerCase());
            cv_city.put(LocationEntries.COLUMN_LATITUDE, latitude);
            cv_city.put(LocationEntries.COLUMN_LONGITUDE, longitude);
            combined.add(cvv);
            city.add(cv_city);
            combined.add(city);
        }
        catch (JSONException e)
        {
            Log.e("QueryUtils", "Error extracting data");
        }

        return combined;
    }
}
