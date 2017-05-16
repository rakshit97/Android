package com.example.rakshit.sunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ForecastAdapter extends CursorAdapter
{
    private static final int TYPE_TODAY = 0;
    private static final int TYPE_OTHERS = 1;
    private static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public ForecastAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        int type = getItemViewType(cursor.getPosition());
        int layout = type==TYPE_TODAY?R.layout.list_forecast_today:R.layout.list_forecast;
        View view =  LayoutInflater.from(context).inflate(layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder holder = (ViewHolder) view.getTag();
        String day = getDay(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_DATE)));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_SHORT_DESC));
        double x = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_MAX_TEMP));
        double n = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_MIN_TEMP));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unit = preferences.getString(context.getString(R.string.units_key), context.getString(R.string.units_default_value));
        if (unit.equals(context.getString(R.string.units_fahrenheit_value)))
        {
            x = x * 9/5D + 32;
            n = n * 9/5D + 32;
        }
        double max = Math.round(x);
        double min = Math.round(n);

        int icon;
        if(getItemViewType(cursor.getPosition())==TYPE_TODAY)
            icon = getWeatherArt(cursor.getInt(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_WEATHER_ID)));
        else
            icon = getWeatherIcon(cursor.getInt(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_WEATHER_ID)));

        holder.tv_day.setText(day);
        holder.tv_desc.setText(desc);
        holder.tv_max_temp.setText(context.getString(R.string.format_temp, max));
        holder.tv_min_temp.setText(context.getString(R.string.format_temp, min));
        holder.icon.setImageResource(icon);
    }

    private static class ViewHolder
    {
        TextView tv_day;
        TextView tv_desc;
        TextView tv_max_temp;
        TextView tv_min_temp;
        ImageView icon;
        public ViewHolder(View view)
        {
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_max_temp = (TextView) view.findViewById(R.id.tv_max_temp);
            tv_min_temp = (TextView) view.findViewById(R.id.tv_min_temp);
            icon = (ImageView) view.findViewById(R.id.icon);
        }
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position==0?TYPE_TODAY:TYPE_OTHERS;
    }

    public String getDay(long time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time*1000L);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day==getToday())
        {
            String ret =  new SimpleDateFormat("MMM dd").format(new Date(time * 1000L));
            return "Today, "+ret;
        }
        else if (day==(getToday()+1)%8)
            return "Tomorrow";
        if (getToday()==7 && day==1)
            return "Tomorrow";
        return days[day-1];
    }

    private int getToday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getWeatherIcon(int weatherId)
    {
        if(weatherId>=200 && weatherId<=232 || weatherId==781 || weatherId==771 || (weatherId>=900 && weatherId<=902) || (weatherId>=957 && weatherId<=962))
            return R.drawable.ic_storm;
        else if(weatherId>=300 && weatherId<=321)
            return R.drawable.ic_light_rain;
        else if(weatherId>=500 && weatherId<=531 && weatherId!=511)
            return R.drawable.ic_rain;
        else if(weatherId>=600 && weatherId<=622 || weatherId==511 || weatherId==903 || weatherId==906)
            return R.drawable.ic_snow;
        else if(weatherId>=701 && weatherId<=781)
            return R.drawable.ic_fog;
        else if(weatherId==800 || weatherId==904)
            return R.drawable.ic_clear;
        else if (weatherId==801 || weatherId==802)
            return R.drawable.ic_light_clouds;
        else if(weatherId==803 || weatherId==804)
            return R.drawable.ic_cloudy;
        else
            return -1;
    }

    public static int getWeatherArt(int weatherId)
    {
        if(weatherId>=200 && weatherId<=232 || weatherId==781 || weatherId==771 || (weatherId>=900 && weatherId<=902) || (weatherId>=957 && weatherId<=962))
            return R.drawable.art_storm;
        else if(weatherId>=300 && weatherId<=321)
            return R.drawable.art_light_rain;
        else if(weatherId>=500 && weatherId<=531 && weatherId!=511)
            return R.drawable.art_rain;
        else if(weatherId>=600 && weatherId<=622 || weatherId==511 || weatherId==903 || weatherId==906)
            return R.drawable.art_snow;
        else if(weatherId>=701 && weatherId<=781)
            return R.drawable.art_fog;
        else if(weatherId==800 || weatherId==904)
            return R.drawable.art_clear;
        else if (weatherId==801 || weatherId==802)
            return R.drawable.art_light_clouds;
        else if(weatherId==803 || weatherId==804)
            return R.drawable.art_clouds;
        else
            return -1;
    }
}
