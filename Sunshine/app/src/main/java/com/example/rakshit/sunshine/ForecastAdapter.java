package com.example.rakshit.sunshine;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ForecastAdapter extends ArrayAdapter<ForecastData>
{
    public ForecastAdapter(Activity context, ArrayList<ForecastData> datas)
    {
        super(context, 0, datas);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_forecast, parent, false);
        }

        ForecastData curr = getItem(position);
        TextView tv_forecast = (TextView)convertView.findViewById(R.id.tv_forecast);
        tv_forecast.setText(String.valueOf(curr.getTemp_max()) + "/" + String.valueOf(curr.getTemp_min())+ " - " + curr.getCondition());
        return convertView;
    }
}
