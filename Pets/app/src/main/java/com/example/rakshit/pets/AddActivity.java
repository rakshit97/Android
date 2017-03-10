package com.example.rakshit.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rakshit.pets.data.PetsContract.tableCols;

public class AddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    private Spinner genderSpinner;
    private int gender;
    private EditText name;
    private EditText breed;
    private EditText weight;
    Uri uri;

    static final String[] PROJECTION = new String[] {tableCols.COL_ID, tableCols.COL_NAME, tableCols.COL_BREED, tableCols.COL_GENDER, tableCols.COL_WEIGHT};
    static final String SELECTION = "((" + tableCols.COL_NAME + " NOTNULL) AND (" + tableCols.COL_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent edit = getIntent();
        uri = edit.getData();

        if(uri==null)
            setTitle(getString(R.string.add));
        else
        {
            setTitle(getString(R.string.edit));
            getLoaderManager().initLoader(1, null, this);
        }

        name = (EditText)findViewById(R.id.name);
        breed = (EditText)findViewById(R.id.breed);
        weight = (EditText)findViewById(R.id.weight);

        genderSpinner = (Spinner)findViewById(R.id.spinner);
        setupSpinner();
    }

    private void setupSpinner()
    {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_dropdown_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(genderSpinnerAdapter);
        genderSpinner.setSelection(0);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String selected = (String) adapterView.getItemAtPosition(i);
                if(!TextUtils.isEmpty(selected))
                {
                    if(selected.equals(getString(R.string.gender_male)))
                        gender = tableCols.GENDER_MALE;
                    else if(selected.equals(getString(R.string.gender_female)))
                        gender = tableCols.GENDER_FEMALE;
                    else
                        gender = tableCols.GENDER_UNKNOWN;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                gender = tableCols.GENDER_UNKNOWN;
            }
        });
    }

    private void insertPet()
    {
        String val_name = name.getText().toString().trim();
        String val_breed = breed.getText().toString().trim();
        int val_weight;
        try
        {
            val_weight = Integer.valueOf(weight.getText().toString().trim());
        }
        catch (NumberFormatException e)
        {
            val_weight = 0;
        }

        ContentValues values = new ContentValues();
        values.put(tableCols.COL_NAME, val_name);
        values.put(tableCols.COL_BREED, val_breed);
        values.put(tableCols.COL_GENDER, gender);
        values.put(tableCols.COL_WEIGHT, val_weight);

        if(uri==null)
        {
            long rowId = ContentUris.parseId(getContentResolver().insert(tableCols.CONTENT_URI, values));
            if(rowId!=-1)
                Toast.makeText(this, "Added pet with row id "+rowId, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Error adding pet" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            long updtd = getContentResolver().update(uri, values, null, null);
            if(updtd!=0)
                Toast.makeText(this, "Updated pet with row id "+ContentUris.parseId(uri), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Error updating pet" , Toast.LENGTH_SHORT).show();
        }
    }

    private void clearData()
    {
        name.setText("");
        breed.setText("");
        weight.setText("");
        genderSpinner.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.addactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.options_save:
                insertPet();
                finish();
                return true;
            case R.id.option_del:
                clearData();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Loader Callbacks

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this, uri, PROJECTION, SELECTION, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        data.moveToFirst();
        name.setText(data.getString(data.getColumnIndexOrThrow(tableCols.COL_NAME)));
        breed.setText(data.getString(data.getColumnIndexOrThrow(tableCols.COL_BREED)));
        weight.setText(String.valueOf(data.getInt(data.getColumnIndexOrThrow(tableCols.COL_WEIGHT))));
        genderSpinner.setSelection(data.getInt(data.getColumnIndexOrThrow(tableCols.COL_GENDER)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        clearData();
    }
}
