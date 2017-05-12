package com.example.rakshit.sunshine;

import android.content.AsyncTaskLoader;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.rakshit.sunshine.data.WeatherContract.LocationEntries;
import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

import java.util.Vector;

import static com.example.rakshit.sunshine.QueryUtil.fetchData;

public class DataLoader extends AsyncTaskLoader<Cursor>
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
    public Cursor loadInBackground()
    {
        if(url_string == null)
            return null;
        Uri uri1 = Uri.parse(url_string);
        String city = uri1.getQueryParameter("q");
        Vector<Vector<ContentValues>> retVector =  fetchData(url_string, city);
        checkLocation(retVector.get(1));
        getContext().getContentResolver().delete(WeatherEntries.CONTENT_URI, null, null);
        ContentValues[] values = new ContentValues[retVector.get(0).size()];
        retVector.get(0).toArray(values);
        int count = getContext().getContentResolver().bulkInsert(WeatherEntries.CONTENT_URI, values);
        if (count<7)
            Log.e("insert", "can't insert all weather values");

        Uri uri = WeatherEntries.buildUriWithLocation(city);
        return getContext().getContentResolver().query(uri, null, null, null, WeatherEntries.COLUMN_DATE + " ASC ");
    }

    private void checkLocation(Vector<ContentValues> v)
    {
        ContentValues cv = v.get(0);
        long id = cv.getAsLong(LocationEntries._ID);
        Cursor cursor = getContext().getContentResolver().query(LocationEntries.CONTENT_URI, null, LocationEntries._ID+"=?", new String[]{String.valueOf(id)}, null);
        if (cursor.getCount()==0)
        {
            ContentUris.parseId(getContext().getContentResolver().insert(LocationEntries.CONTENT_URI, cv));
        }
        cursor.close();
    }
}
