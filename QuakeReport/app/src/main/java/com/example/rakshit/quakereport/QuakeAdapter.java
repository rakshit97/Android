package com.example.rakshit.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class QuakeAdapter extends ArrayAdapter<QuakeData>
{
    public QuakeAdapter(Activity context, ArrayList<QuakeData> data)
    {
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if(listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }

        QuakeData curr = getItem(position);
        TextView tv;

        tv = (TextView)listItemView.findViewById(R.id.mag_container);
        tv.setText(curr.getMag());

        tv = (TextView)listItemView.findViewById(R.id.place_container);
        tv.setText(curr.getPlace());

        tv = (TextView)listItemView.findViewById(R.id.date_container);
        tv.setText(curr.getDate());

        return listItemView;
    }
}
