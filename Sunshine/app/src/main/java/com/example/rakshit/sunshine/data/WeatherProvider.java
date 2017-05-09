package com.example.rakshit.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.rakshit.sunshine.data.WeatherContract.LocationEntries;
import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

public class WeatherProvider extends ContentProvider
{
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private WeatherDbHelper dbHelper;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int LOCATION = 200;

    private static final SQLiteQueryBuilder builder;

    static
    {
        builder = new SQLiteQueryBuilder();

        //inner join
        builder.setTables(WeatherEntries.TABLE_NAME + " INNER JOIN "
            + LocationEntries.TABLE_NAME + " ON "
            + WeatherEntries.TABLE_NAME + "." + WeatherEntries.COLUMN_LOC_KEY
            + "=" + LocationEntries.TABLE_NAME + "."
            + LocationEntries._ID);
    }

    //location.location = ?
    public static final String locationSelection = LocationEntries.TABLE_NAME + "."
            + LocationEntries.COLUMN_LOCATION + "=?";

    //location.location = ? AND date >= ?
    public static final String locationWithStartDateSelection = LocationEntries.TABLE_NAME + "."
             + LocationEntries.COLUMN_LOCATION + "=? AND " + WeatherEntries.COLUMN_DATE + ">=?";

    //location.location = ? AND date = ?
    public static final String locationWithDateSelection = LocationEntries.TABLE_NAME + "."
            + LocationEntries.COLUMN_LOCATION + "=? AND " + WeatherEntries.COLUMN_DATE + "=?";

    private Cursor getWeatherByLocation(Uri uri,String[] projection, String sortOrder)
    {
        String location = WeatherEntries.getLocationFromUri(uri);
        long startDate = WeatherEntries.getStartDateFromUri(uri);
        String[] selectionArgs;
        String selection;

        if(startDate==0)
        {
            selection = locationSelection;
            selectionArgs = new String[]{location};
        }
        else
        {
            selection = locationWithStartDateSelection;
            selectionArgs = new String[]{location, Long.toString(startDate)};
        }

        return builder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    private Cursor getWeatherByLocationAndDate(Uri uri, String[] projection, String sortOrder)
    {
        String location = WeatherEntries.getLocationFromUri(uri);
        long date = WeatherEntries.getDateFromUri(uri);
        String selection = locationWithDateSelection;
        String[] selectionArgs = new String[]{location, Long.toString(date)};
        return builder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    static UriMatcher buildUriMatcher()
    {
        return null;
    }


    @Override
    public boolean onCreate()
    {
        dbHelper = new WeatherDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        final int match = uriMatcher.match(uri);
        switch (match)
        {
            case WEATHER:
                return WeatherEntries.MIME_LIST;
            case WEATHER_WITH_LOCATION:
                return  WeatherEntries.MIME_LIST;
            case WEATHER_WITH_LOCATION_AND_DATE:
                return WeatherEntries.MIME_ITEM;
            case LOCATION:
                return LocationEntries.MIME_LIST;
            default:
                throw new IllegalArgumentException("Invalid uri");
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1)
    {
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match)
        {
            case WEATHER_WITH_LOCATION_AND_DATE:
                cursor = getWeatherByLocationAndDate(uri, strings, s1);
                break;
            case WEATHER_WITH_LOCATION:
                cursor = getWeatherByLocation(uri, strings, s1);
                break;
            case WEATHER:
                cursor = null;
                break;
            case LOCATION:
                cursor = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri retUri;

        switch (match)
        {
            case WEATHER:
                normalizeDate(contentValues);
                long id = database.insert(WeatherEntries.TABLE_NAME, null, contentValues);
                if (id>0)
                    retUri = WeatherEntries.buildUriWithId(id);
                else
                    throw new SQLException("Cannot insert data");
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    private void normalizeDate(ContentValues values)
    {
        if (values.containsKey(WeatherEntries.COLUMN_DATE))
        {
            long date = values.getAsLong(WeatherEntries.COLUMN_DATE);
            values.put(WeatherEntries.COLUMN_DATE, WeatherContract.normalizeDate(date));
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {
                        normalizeDate(value);
                        long id = db.insert(WeatherEntries.TABLE_NAME, null, value);
                        if (id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
