package com.example.rakshit.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
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
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private WeatherDbHelper dbHelper;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int WEATHER_WITH_ID = 103;
    static final int LOCATION = 200;

    static
    {
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER, WEATHER);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/#", WEATHER_WITH_ID);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);
        uriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_LOCATION, LOCATION);
    }

    private static final SQLiteQueryBuilder builder;

    static
    {
        builder = new SQLiteQueryBuilder();
        //inner join
        builder.setTables(WeatherEntries.TABLE_NAME + " INNER JOIN "
            + LocationEntries.TABLE_NAME + " ON "
            + WeatherEntries.TABLE_NAME + "." + WeatherEntries.COLUMN_LOC_KEY
            + "=" + LocationEntries.TABLE_NAME + "."
            + LocationEntries.COLUMN_ID);
    }

    //location.city = ?
    public static final String locationSelection = LocationEntries.TABLE_NAME + "."
            + LocationEntries.COLUMN_CITY + "=?";

    //location.city = ? AND date >= ?
    public static final String locationWithStartDateSelection = LocationEntries.TABLE_NAME + "."
             + LocationEntries.COLUMN_CITY + "=? AND " + WeatherEntries.COLUMN_DATE + ">=?";

    //location.city = ? AND date = ?
    public static final String locationWithDateSelection = LocationEntries.TABLE_NAME + "."
            + LocationEntries.COLUMN_CITY + "=? AND " + WeatherEntries.COLUMN_DATE + "=?";

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
            case WEATHER_WITH_ID:
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
                cursor = dbHelper.getReadableDatabase().query(WeatherEntries.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case WEATHER_WITH_ID:
                cursor = dbHelper.getReadableDatabase().query(WeatherEntries.TABLE_NAME, strings, WeatherEntries.COLUMN_ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))}, null, null, s1);
                break;
            case LOCATION:
                cursor = dbHelper.getReadableDatabase().query(LocationEntries.TABLE_NAME, strings, s, strings1, null, null, s1);;
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
            {
                long id = database.insert(WeatherEntries.TABLE_NAME, null, contentValues);
                if (id > 0)
                    retUri = WeatherEntries.buildUriWithId(id);
                else
                    throw new SQLException("Cannot insert data");
                break;
            }
            case LOCATION:
            {
                long id = database.insert(LocationEntries.TABLE_NAME, null, contentValues);
                if (id > 0)
                    retUri = LocationEntries.buildLocationUri(id);
                else
                    throw new SQLException("Cannot insert data");
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        database.close();
        return retUri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updtd;

        switch (match)
        {
            case WEATHER:
            {
                updtd = db.update(WeatherEntries.TABLE_NAME, contentValues, s, strings);
                if (updtd<=0)
                    throw new SQLException("Can't update");
                break;
            }
            case LOCATION:
            {
                updtd = db.update(LocationEntries.TABLE_NAME, contentValues, s, strings);
                if (updtd<=0)
                    throw new SQLException("Can't update");
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updtd;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int dltd;

        switch (match)
        {
            case WEATHER:
            {
                dltd = db.delete(WeatherEntries.TABLE_NAME, s, strings);
                break;
            }
            case LOCATION:
            {
                dltd = db.delete(LocationEntries.TABLE_NAME, s, strings);
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return dltd;
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
                try
                {
                    for (ContentValues value : values)
                    {
                        long id = db.insert(WeatherEntries.TABLE_NAME, null, value);
                        if (id != -1)
                            count++;
                    }
                    db.setTransactionSuccessful();
                } finally
                {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
