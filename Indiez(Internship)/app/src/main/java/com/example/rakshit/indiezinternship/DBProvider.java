package com.example.rakshit.indiezinternship;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.rakshit.indiezinternship.DBContract.tableCols.COL_ID;
import static com.example.rakshit.indiezinternship.DBContract.tableCols.COL_WORD;
import static com.example.rakshit.indiezinternship.DBContract.tableCols.TABLE;

public class DBProvider extends ContentProvider
{
    private DBHelper dbHelper;

    @Override
    public boolean onCreate()
    {
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        return dbHelper.getReadableDatabase().query(TABLE, new String[]{COL_ID, COL_WORD}, COL_WORD + " LIKE ?", selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;
    }
}
