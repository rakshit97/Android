package com.example.rakshit.sunshine.data;

import android.provider.BaseColumns;
import android.text.format.Time;

public class WeatherContract
{
    public static long normalizeData(long startDate)
    {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class WeatherEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_WIND_DIRECTION = "wind_dir";
    }

    public static final class LocationEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "location";
    }
}
