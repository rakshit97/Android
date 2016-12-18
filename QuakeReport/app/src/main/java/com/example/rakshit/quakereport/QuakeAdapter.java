package com.example.rakshit.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


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
        Log.i("QueryUtils", "Date = " + date);
        String offset, primary_location;
        String place = curr.getPlace();


        mag_formatter = new DecimalFormat("0.0");
        String magToDisplay = mag_formatter.format(curr.getMag());

        formatter = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        String dateToDisplay = formatter.format(date);
        Log.i("QueryUtils", "Date = " + dateToDisplay);
        formatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
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

        GradientDrawable mag_circle = (GradientDrawable)tv.getBackground();
        mag_circle.setColor(getMagColor(curr.getMag()));

        tv = (TextView)listItemView.findViewById(R.id.offset_container);
        tv.setText(offset);

        tv = (TextView)listItemView.findViewById(R.id.primary_location_container);
        tv.setText(primary_location);
        tv.setSelected(true);

        tv = (TextView)listItemView.findViewById(R.id.date_container);
        tv.setText(dateToDisplay);

        tv = (TextView)listItemView.findViewById(R.id.time_container);
        tv.setText(timeToDisplay);

        return listItemView;
    }

    public int getMagColor(double mag)
    {
        if(mag>=0 && mag<2)
            return ContextCompat.getColor(getContext(), R.color.magnitude1);
        else if(mag>=2 && mag<3)
            return ContextCompat.getColor(getContext(), R.color.magnitude2);
        else if(mag>=3 && mag<4)
            return ContextCompat.getColor(getContext(), R.color.magnitude3);
        else if(mag>=4 && mag<5)
            return ContextCompat.getColor(getContext(), R.color.magnitude4);
        else if(mag>=5 && mag<6)
            return ContextCompat.getColor(getContext(), R.color.magnitude5);
        else if(mag>=6 && mag<7)
            return ContextCompat.getColor(getContext(), R.color.magnitude6);
        else if(mag>=7 && mag<8)
            return ContextCompat.getColor(getContext(), R.color.magnitude7);
        else if(mag>=8 && mag<9)
            return ContextCompat.getColor(getContext(), R.color.magnitude8);
        else if(mag>=9 && mag<10)
            return ContextCompat.getColor(getContext(), R.color.magnitude9);
        else if(mag>=10)
            return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
        else
            return -1;
    }
}
