package com.example.rakshit.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.rakshit.pets.data.PetsContract.tableCols;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo()
    {
        String[] projection = {tableCols.COL_ID, tableCols.COL_NAME, tableCols.COL_BREED, tableCols.COL_GENDER, tableCols.COL_WEIGHT};

        Cursor cursor = getContentResolver().query(tableCols.CONTENT_URI, projection, null, null, null);
        try
        {
            TextView displayView = (TextView) findViewById(R.id.pet_tv);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount()+"\n");

            while(cursor.moveToNext())
            {
                displayView.append("\n" + cursor.getInt(0) + " - " + cursor.getString(1) + " - "
                        + cursor.getString(2) + " - ");
                if(cursor.getInt(3)==0)
                    displayView.append("Unknown Gender");
                else if(cursor.getInt(3)==1)
                    displayView.append("Male");
                else
                    displayView.append("Female");

                displayView.append(" - " + cursor.getInt(4));
            }

        }
        finally
        {
            cursor.close();
        }
    }

    private void insertDummyData()
    {
        ContentValues values = new ContentValues();

        values.put(tableCols.COL_NAME, "toto");
        values.put(tableCols.COL_BREED, "terrier");
        values.put(tableCols.COL_GENDER, tableCols.GENDER_MALE);
        values.put(tableCols.COL_WEIGHT, 10);

        Uri uri = getContentResolver().insert(tableCols.CONTENT_URI, values);
        if(uri!=null)
            displayDatabaseInfo();
        else
            Log.e("MainActivity", "Cannot insert data");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.options_add_dummy: insertDummyData();return true;
            case R.id.options_del_all: return true;
        }
        return super.onOptionsItemSelected(item);
    }
}