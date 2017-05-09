package com.example.rakshit.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rakshit.sunshine.data.WeatherContract.LocationEntries;
import com.example.rakshit.sunshine.data.WeatherContract.WeatherEntries;

public class WeatherDbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "weather.db";

    public WeatherDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        final String CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntries.TABLE_NAME
                + " (" + WeatherEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherEntries.COLUMN_LOC_KEY + " INTEGER NOT NULL, "
                + WeatherEntries.COLUMN_DATE + " INTEGER NOT NULL, "
                + WeatherEntries.COLUMN_MAX_TEMP + " REAL NOT NULL, "
                + WeatherEntries.COLUMN_MIN_TEMP + " REAL NOT NULL, "
                + WeatherEntries.COLUMN_WEATHER_ID + " INTEGER NOT NULL, "
                + WeatherEntries.COLUMN_SHORT_DESC + " TEXT NOT NULL, "
                + WeatherEntries.COLUMN_HUMIDITY + " REAL NOT NULL, "
                + WeatherEntries.COLUMN_PRESSURE + " REAL NOT NULL, "
                + WeatherEntries.COLUMN_WIND_SPEED + " REAL NOT NULL, "
                + WeatherEntries.COLUMN_WIND_DIRECTION + " REAL NOT NULL, "
                + "FOREIGN KEY (" + WeatherEntries.COLUMN_LOC_KEY + ") REFERENCES "
                + LocationEntries.TABLE_NAME + " (" + LocationEntries._ID + "), "
                + "UNIQUE (" + WeatherEntries.COLUMN_DATE + ", "
                + WeatherEntries.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        final String CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntries.TABLE_NAME
                +" (" + LocationEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LocationEntries.COLUMN_LOCATION + " TEXT UNIQUE NOT NULL, "
                + LocationEntries.COLUMN_CITY + " TEXT NOT NULL, "
                + LocationEntries.COLUMN_LATITUDE + " REAL NOT NULL, "
                + LocationEntries.COLUMN_LONGITUDE + " REAL NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_WEATHER_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntries.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntries.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
