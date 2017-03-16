package com.example.rakshit.sunshine;

import android.util.Log;

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
import java.util.ArrayList;

public class QueryUtil
{
    public static ArrayList<ForecastData> fetchData(String url_string)
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

        return extractData(JSONResponse);
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

    private static ArrayList<ForecastData> extractData(String JSONResponse)
    {
        if(JSONResponse == null || JSONResponse.isEmpty())
            return null;

        ArrayList<ForecastData> forecastDatas = new ArrayList<>(7);
        try
        {
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray list = root.getJSONArray("list");

            for(int i=0;i<list.length();i++)
            {
                JSONObject item = list.getJSONObject(i);
                long time = item.getLong("dt");
                JSONObject temp = item.getJSONObject("temp");
                double temp_min = temp.getDouble("min");
                double temp_max = temp.getDouble("max");
                String condition = item.getJSONArray("weather").getJSONObject(0).getString("description");
                String arr[] = condition.split(" ");
                StringBuilder builder = new StringBuilder();
                for(int j=0;j<arr.length;j++)
                {
                    builder.append(Character.toUpperCase(arr[j].charAt(0))).append(arr[j].substring(1)).append(" ");
                }
                condition = builder.toString().trim();

                forecastDatas.add(new ForecastData(time, temp_max, temp_min, condition));
            }
        }
        catch (JSONException e)
        {
            Log.e("QueryUtils", "Error extracting data");
        }
        return forecastDatas;
    }
}
