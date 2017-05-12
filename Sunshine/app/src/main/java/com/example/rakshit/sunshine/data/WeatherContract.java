package com.example.rakshit.sunshine.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class WeatherContract
{
    public static final String CONTENT_AUTHORITY = "com.example.rakshit.sunshine";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = WeatherEntries.TABLE_NAME;
    public static final String PATH_LOCATION = LocationEntries.TABLE_NAME;

    public static long normalizeDate(long startDate)
    {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class WeatherEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "weather";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();
        public static final String MIME_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String MIME_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_LONG_DESC = "long_desc";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_WIND_DIRECTION = "wind_dir";

        public static Uri buildUriWithId(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUriWithLocation(String location)
        {
            return CONTENT_URI.buildUpon().appendPath(location).build();
        }

        public static Uri buildUriWithLocationAndStartDate(String location, long startDate)
        {
            return CONTENT_URI.buildUpon().appendPath(location).appendQueryParameter(COLUMN_DATE, Long.toString(normalizeDate(startDate))).build();
        }

        public static Uri buildUriWithLocationAndDate(String location, long date)
        {
            return CONTENT_URI.buildUpon().appendPath(location).appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getLocationFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static long getDateFromUri(Uri uri)
        {
            return Long.valueOf(uri.getPathSegments().get(2));
        }

        public static long getStartDateFromUri(Uri uri)
        {
            if(uri.getQueryParameter(COLUMN_DATE)!=null && uri.getQueryParameter(COLUMN_DATE).length()>0)
                return Long.valueOf(uri.getQueryParameter(COLUMN_DATE));
            return 0;
        }
    }

    public static final class LocationEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "location";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        public static final String MIME_LIST = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        public static final String MIME_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_LATITUDE = "lat";
        public static final String COLUMN_LONGITUDE = "long";

        public static Uri buildLocationUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
