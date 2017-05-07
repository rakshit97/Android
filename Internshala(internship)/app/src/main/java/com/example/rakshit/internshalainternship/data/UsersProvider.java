package com.example.rakshit.internshalainternship.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.rakshit.internshalainternship.data.UsersContract.NotesEntries;
import com.example.rakshit.internshalainternship.data.UsersContract.UserEntries;

import static com.example.rakshit.internshalainternship.data.UsersContract.COL_ID;
import static com.example.rakshit.internshalainternship.data.UsersContract.MIME_ITEM_NOTES;
import static com.example.rakshit.internshalainternship.data.UsersContract.MIME_ITEM_USERS;
import static com.example.rakshit.internshalainternship.data.UsersContract.MIME_LIST_NOTES;
import static com.example.rakshit.internshalainternship.data.UsersContract.MIME_LIST_USERS;


public class UsersProvider extends ContentProvider
{
    private DBHelper dbHelper;
    private static final int WHOLE_TABLE_USERS = 1000;
    private static final int SPECIFIC_COLUMN_USERS = 1001;
    private static final int USER_ALL_NOTES = 2000;
    private static final int SPECIFIC_COLUMN_NOTES = 2001;

    private static final UriMatcher uri_matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        uri_matcher.addURI(UsersContract.CONTENT_AUTHORITY, UsersContract.PATH_USERS, WHOLE_TABLE_USERS);
        uri_matcher.addURI(UsersContract.CONTENT_AUTHORITY, UsersContract.PATH_USERS + "/#", SPECIFIC_COLUMN_USERS);
        uri_matcher.addURI(UsersContract.CONTENT_AUTHORITY, UsersContract.PATH_NOTES, USER_ALL_NOTES);
        uri_matcher.addURI(UsersContract.CONTENT_AUTHORITY, UsersContract.PATH_NOTES + "/#", SPECIFIC_COLUMN_NOTES);
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new DBHelper(getContext());
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
            case WHOLE_TABLE_USERS:
                cursor = db.query(UserEntries.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case USER_ALL_NOTES:
                cursor = db.query(NotesEntries.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case SPECIFIC_COLUMN_NOTES:
                selection = COL_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(NotesEntries.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query invalid Uri " +uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        final int match = uri_matcher.match(uri);
        switch (match)
        {
            case WHOLE_TABLE_USERS:
                return MIME_LIST_USERS;
            case SPECIFIC_COLUMN_USERS:
                return MIME_ITEM_USERS;
            case USER_ALL_NOTES:
                return MIME_LIST_NOTES;
            case SPECIFIC_COLUMN_NOTES:
                return MIME_ITEM_NOTES;
            default:
                throw new IllegalArgumentException("Invalid URI");
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        final int match = uri_matcher.match(uri);
        switch (match)
        {
            case WHOLE_TABLE_USERS:
                return insertData(uri, contentValues, 0);

            case USER_ALL_NOTES:
                return insertData(uri, contentValues, 1);
            default:
                throw new IllegalArgumentException("Insertion not supported for" + uri);
        }
    }
    private Uri insertData(Uri uri, ContentValues contentValues, int i)
    {
        long id;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(i==0)
            id = db.insert(UserEntries.TABLE_NAME, null, contentValues);
        else
            id = db.insert(NotesEntries.TABLE_NAME, null, contentValues);
        if(id==-1)
            return null;
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        final int match = uri_matcher.match(uri);
        switch(match)
        {
            case SPECIFIC_COLUMN_USERS:
                s = COL_ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return deleteData(uri, s, strings, 0);

            case USER_ALL_NOTES:
                return deleteData(uri, s, strings, 1);

            case SPECIFIC_COLUMN_NOTES:
                s = COL_ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deleteData(uri, s, strings, 1);
            default:
                throw new IllegalArgumentException("Update not supported for "+uri);
        }
    }
    private int deleteData(Uri uri, String s, String[] strings, int i)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        if(i==0)
            return db.delete(UserEntries.TABLE_NAME, s, strings);
        else
            return db.delete(NotesEntries.TABLE_NAME, s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        final int match = uri_matcher.match(uri);
        switch(match)
        {
            case SPECIFIC_COLUMN_NOTES:
                s = COL_ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateData(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update not supported for "+uri);
        }
    }

    private int updateData(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        if(contentValues.size()==0)
            return 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        return db.update(NotesEntries.TABLE_NAME, contentValues, s, strings);
    }
}
