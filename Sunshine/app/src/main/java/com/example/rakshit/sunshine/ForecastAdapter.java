package com.example.rakshit.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

public class ForecastAdapter extends CursorAdapter
{
    public ForecastAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_forecast, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView forecast = (TextView) view.findViewById(R.id.tv_forecast);
        String max = String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_MAX_TEMP)));
        String min = String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_MIN_TEMP)));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_LONG_DESC));
        forecast.setText(max+"/"+min+" - "+desc);
    }
}
