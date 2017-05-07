package com.example.rakshit.internshalainternship.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rakshit.internshalainternship.data.UsersContract.UserEntries;
import com.example.rakshit.internshalainternship.data.UsersContract.NotesEntries;

import static com.example.rakshit.internshalainternship.data.UsersContract.COL_ID;
import static com.example.rakshit.internshalainternship.data.UsersContract.COL_USERNAME;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_TABLE_CMD_USERS = "CREATE TABLE " + UserEntries.TABLE_NAME
            + "( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + ", " + COL_USERNAME + " TEXT NOT NULL"
            + ", " + UserEntries.COL_PASSWORD + " TEXT NOT NULL);";
    private static final String DELETE_TABLE_USERS = "DROP TABLE IF EXISTS " + UserEntries.TABLE_NAME;

    private static final String CREATE_TABLE_CMD_NOTES = "CREATE TABLE " + NotesEntries.TABLE_NAME
            + "( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
            + ", " + COL_USERNAME + " TEXT NOT NULL"
            + ", " + NotesEntries.COL_TOPIC + " TEXT"
            + ", " + NotesEntries.COL_TEXT + " TEXT);";
    public static final String DELETE_TABLE_NOTES = "DROP TABLE IF EXISTS " + NotesEntries.TABLE_NAME;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_CMD_USERS);
        db.execSQL(CREATE_TABLE_CMD_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL(DELETE_TABLE_USERS);
        db.execSQL(DELETE_TABLE_NOTES);
        onCreate(db);
    }
}
