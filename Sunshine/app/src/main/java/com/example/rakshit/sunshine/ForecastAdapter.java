package com.example.rakshit.sunshine;

import android.content.Context;
import android.database.Cursor;
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

        double max = Math.round(x);
        double min = Math.round(n);

        holder.tv_day.setText(day);
        holder.tv_desc.setText(desc);
        holder.tv_max_temp.setText(context.getString(R.string.format_temp, max));
        holder.tv_min_temp.setText(context.getString(R.string.format_temp, min));
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

    private String getDay(long time)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time*1000L);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int today = getToday();

        if (day==getToday())
        {
            String ret =  new SimpleDateFormat("MMM D").format(new Date(time * 1000L));
            return "Today, "+ret;
        }
        else if (day==((getToday()+1)%8)+1)
            return "Tomorrow";
        return days[day-1];
    }

    private int getToday()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
