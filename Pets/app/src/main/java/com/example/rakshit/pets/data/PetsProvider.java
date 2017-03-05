package com.example.rakshit.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.rakshit.pets.data.PetsContract.tableCols;

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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        int match = uri_matcher.match(uri);
        switch (match)
        {
            case WHOLE_TABLE:
                cursor = db.query(tableCols.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SPECIFIC_COLUMN:
                selection = tableCols.COL_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(tableCols.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query invalid Uri " +uri);
        }

        return cursor;
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
        final int match = uri_matcher.match(uri);
        switch (match)
        {
            case WHOLE_TABLE:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion not supported for" + uri);
        }
    }
    private Uri insertPet(Uri uri, ContentValues contentValues)
    {
        long id;
        String name = contentValues.getAsString(tableCols.COL_NAME);
        if(name == null || name.isEmpty())
        {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            id = -1;
            return ContentUris.withAppendedId(uri, id);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        id = db.insert(tableCols.TABLE_NAME, null, contentValues);
        if(id==-1)
            return null;
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        final int match = uri_matcher.match(uri);
        switch(match)
        {
            case WHOLE_TABLE:
                return deletePet(uri, s, strings);
            case SPECIFIC_COLUMN:
                s = tableCols.COL_ID + "?=";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return deletePet(uri, s, strings);
            default:
                throw new IllegalArgumentException("Update not supported for "+uri);
        }
    }
    private int deletePet(Uri uri, String s, String[] strings)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(tableCols.TABLE_NAME, s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        final int match = uri_matcher.match(uri);
        switch(match)
        {
            case WHOLE_TABLE:
                return updatePet(uri, contentValues, s, strings);
            case SPECIFIC_COLUMN:
                s = tableCols.COL_ID + "?=";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update not supported for "+uri);
        }
    }

    private int updatePet(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        if(contentValues.size()==0)
            return 0;
        if(contentValues.containsKey(tableCols.COL_NAME))
        {
            String name = contentValues.getAsString(tableCols.COL_NAME);
            if (name == null || name.isEmpty()) {
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.update(tableCols.TABLE_NAME, contentValues, s, strings);
    }
}
