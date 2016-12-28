package com.example.rakshit.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rakshit.pets.data.PetsContract.tableCols;

public class PetsDBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "pets.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_DB_CMD = "CREATE TABLE " + tableCols.TABLE_NAME
            + "( " + tableCols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + ", " + tableCols.COL_NAME + " TEXT NOT NULL"
            + ", " + tableCols.COL_BREED + " TEXT"
            + ", " + tableCols.COLUMN_GENDER + " INTEGER NOT NULL"
            + ", " + tableCols.COLUMN_WEIGHT + " INTEGER);";
    private static final String DELETE_DB = "";

    public PetsDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DB_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {

    }
}
