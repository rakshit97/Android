package com.example.rakshit.quakereport;

import android.text.TextUtils;
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
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtil
{

    private static ArrayList<QuakeData> extractData(String JSONResponse)
    {
        if(TextUtils.isEmpty(JSONResponse))
            return null;

        ArrayList<QuakeData> data = new ArrayList<QuakeData>(10);
        try
        {
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray features = root.getJSONArray("features");

            for (int i = 0; i < features.length(); i++)
            {
                JSONObject quake_data = features.getJSONObject(i);
                JSONObject props = quake_data.getJSONObject("properties");
                double mag = props.getDouble("mag");
                String place = props.getString("place");
                long time = props.getLong("time");
                String url = props.getString("url");

                data.add(new QuakeData(mag, place, time, url));
            }
        }
        catch(JSONException e)
        {
            Log.e("QueryUtil", "JSON exception");
        }

        return data;
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
            Log.e("QueryUtils", "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException
    {
        String JSONResponse = "";
        if(url == null)
            return JSONResponse;

        HttpURLConnection connection = null;
        InputStream stream = null;

        try
        {
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 200)
            {
                stream = connection.getInputStream();
                JSONResponse = readFromStream(stream);
            }
            else
            {
                Log.e("QueryUtils", "Error response code: " + connection.getResponseCode() + connection.getResponseMessage());
            }
        }
        catch (IOException e)
        {
            Log.e("QueryUtils", "Error getting JSON response");
        }
        finally
        {
            if(connection!=null)
                connection.disconnect();
            if(stream!=null)
                stream.close();
            return JSONResponse;
        }
    }

    private static String readFromStream(InputStream stream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if(stream!=null)
        {
            InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line!=null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<QuakeData> fetchData(String url_string)
    {
        URL url = createURL(url_string);
        String JSONResponse = null;
        try
        {
            JSONResponse = makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.e("QueryUtils", "Error in making HTTP request");
        }

        return extractData(JSONResponse);


    }
}
