package com.example.rakshit.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

import static com.example.rakshit.quakereport.QueryUtil.fetchData;

public class DataLoader extends AsyncTaskLoader<ArrayList<QuakeData>>
{
    String url_string;

    public DataLoader(Context context, String url_string)
    {
        super(context);
        this.url_string = url_string;
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public ArrayList<QuakeData> loadInBackground()
    {
        if(url_string==null)
            return null;
        return fetchData(url_string);
    }
}
