package com.example.rakshit.pets.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class PetsProvider extends ContentProvider
{
    private PetsDBHelper dbHelper;
    private static final int WHOLE_TABLE = 1000;
    private static final int SPECIFIC_COLUMN = 1001;

    private static final UriMatcher uri_matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        uri_matcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PETS_PATH, WHOLE_TABLE);
        uri_matcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PETS_PATH + "/#", SPECIFIC_COLUMN);
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new PetsDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1)
    {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }
}
