package com.example.rakshit.pets;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rakshit.pets.data.PetsContract.tableCols;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    DisplayAdapter displayAdapter;
    static final String[] PROJECTION = new String[] {tableCols.COL_ID, tableCols.COL_NAME, tableCols.COL_BREED};
    static final String SELECTION = "((" + tableCols.COL_NAME + " NOTNULL) AND (" + tableCols.COL_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this);

        ListView listView = (ListView) findViewById(R.id.pets_lv);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        displayAdapter = new DisplayAdapter(this, null);
        listView.setAdapter(displayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id)
            {
                Uri uri = ContentUris.withAppendedId(tableCols.CONTENT_URI, id);
                Intent edit = new Intent(MainActivity.this, AddActivity.class);
                edit.setData(uri);
                startActivity(edit);
            }
        });

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

    private void insertDummyData()
    {
        ContentValues values = new ContentValues();

        values.put(tableCols.COL_NAME, "toto");
        values.put(tableCols.COL_BREED, "terrier");
        values.put(tableCols.COL_GENDER, tableCols.GENDER_MALE);
        values.put(tableCols.COL_WEIGHT, 10);

        Uri uri = getContentResolver().insert(tableCols.CONTENT_URI, values);
        if(uri==null)
            Log.e("MainActivity", "Cannot insert data");
    }

    private void deleteAll()
    {
        DialogInterface.OnClickListener deleteButtonListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int dltd = getContentResolver().delete(tableCols.CONTENT_URI, null, null);
                if(dltd!=0)
                {
                    Toast.makeText(MainActivity.this, "Deleted all records", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Error deleting records", Toast.LENGTH_SHORT).show();
            }
        };

        showDeleteDialog(deleteButtonListener);
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
            case R.id.options_del_all: deleteAll();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //dialog
    private void showDeleteDialog(DialogInterface.OnClickListener deleteButtonListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deletemessage);
        builder.setPositiveButton(R.string.yes, deleteButtonListener);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(dialogInterface!=null)
                    dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //LoaderCallback Methods

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this, tableCols.CONTENT_URI, PROJECTION, SELECTION, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        displayAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        displayAdapter.swapCursor(null);
    }
}