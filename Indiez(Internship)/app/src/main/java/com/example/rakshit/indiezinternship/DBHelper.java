package com.example.rakshit.indiezinternship;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.rakshit.indiezinternship.DBContract.tableCols.COL_WORD;
import static com.example.rakshit.indiezinternship.DBContract.tableCols.TABLE;

public class DBHelper extends SQLiteOpenHelper
{
    private final Context context;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "WORDS";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_DB_CMD = "CREATE TABLE " + DBContract.tableCols.TABLE
        + "(" + DBContract.tableCols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
        + ", " + DBContract.tableCols.COL_WORD + " TEXT NOT NULL)";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        this.db = db;
        this.db.execSQL(CREATE_DB_CMD);
        db.beginTransaction();
        try
        {
            addWords(db);
            db.setTransactionSuccessful();
        } catch (IOException e)
        {
            Log.e("DBHelper", Log.getStackTraceString(e));
        } finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private void addWords(SQLiteDatabase db) throws IOException
    {
        final Resources resources = context.getResources();
        InputStream iStream = resources.openRawResource(R.raw.words);
        BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
        String line;
        while ((line = reader.readLine()) != null)
        {
            long id = insertWord(line.trim());
            if (id < 0)
            {
                Log.e("DBHelper", "unable to add word: " + line.trim());
            }
        }
        reader.close();
    }

    private long insertWord(String word)
    {
        ContentValues value = new ContentValues();
        value.put(COL_WORD, word);
        return db.insert(TABLE, null, value);
    }
}
