package com.example.rakshit.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

import java.util.Calendar;

public class ForecastAdapter extends CursorAdapter
{
    private static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

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
        TextView tv_day = (TextView) view.findViewById(R.id.tv_day);
        TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        TextView tv_max_temp = (TextView) view.findViewById(R.id.tv_max_temp);
        TextView tv_min_temp = (TextView) view.findViewById(R.id.tv_min_temp);

        String day = getDay(cursor.getLong(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_DATE)));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_SHORT_DESC));
        double x = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_MAX_TEMP));
        double n = cursor.getDouble(cursor.getColumnIndexOrThrow(WeatherEntries.COLUMN_MIN_TEMP));

        String max = String.valueOf(Math.round(x));
        String min = String.valueOf(Math.round(n));

        tv_day.setText(day);
        tv_desc.setText(desc);
        tv_max_temp.setText(max);
        tv_min_temp.setText(min);
    }

    private String getDay(long time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time*1000L);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int today = getToday();

        if (day==getToday())
            return "Today";
        else if (day==((getToday()+1)%8)+1)
            return "Tomorrow";
        return days[day-1];
        //return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(time * 1000L));
    }

    private int getToday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
