package com.example.rakshit.quakereport;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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

        DecimalFormat mag_formatter;

        SimpleDateFormat formatter;
        Date date = new Date(curr.getTime());

        String offset, primary_location;
        String place = curr.getPlace();


        mag_formatter = new DecimalFormat("0.0");
        String magToDisplay = mag_formatter.format(curr.getMag());

        formatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = formatter.format(date);
        formatter = new SimpleDateFormat(("h:mm a"));
        String timeToDisplay = formatter.format(date);


        if(place.contains(" of "))
        {
            int p = place.indexOf(" of ");
            offset = place.substring(0, p+3);
            primary_location = place.substring(p+4);

            //              OR
            //String[] parts = place.split(" of ");
            //offset = parts[0] + " of";
            //primary_location = parts[1];
        }
        else
        {
            offset = "Near the";
            primary_location = place;
        }

        tv = (TextView)listItemView.findViewById(R.id.mag_container);
        tv.setText(magToDisplay);

        tv = (TextView)listItemView.findViewById(R.id.offset_container);
        tv.setText(offset);

        tv = (TextView)listItemView.findViewById(R.id.primary_location_container);
        tv.setText(primary_location);

        tv = (TextView)listItemView.findViewById(R.id.date_container);
        tv.setText(dateToDisplay);

        tv = (TextView)listItemView.findViewById(R.id.time_container);
        tv.setText(timeToDisplay);

        return listItemView;
    }
}
