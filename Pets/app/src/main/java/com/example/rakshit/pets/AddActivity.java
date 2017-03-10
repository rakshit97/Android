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
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

    private int prev_gender=0, prev_weight =0;
    private String prev_name = "", prev_breed = "";
    private boolean hasChanged;

    static final String[] PROJECTION = new String[] {tableCols.COL_ID, tableCols.COL_NAME, tableCols.COL_BREED, tableCols.COL_GENDER, tableCols.COL_WEIGHT};
    static final String SELECTION = "((" + tableCols.COL_NAME + " NOTNULL) AND (" + tableCols.COL_NAME + " != '' ))";

    private View.OnTouchListener touchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            checkForChange();
            return false;
        }
    };

    private void checkForChange()
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

        if(!val_name.equals(prev_name) || !val_breed.equals(prev_breed) || gender!=prev_gender || val_weight!=prev_weight)
            hasChanged = true;
    }

    @Nullable
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent edit = getIntent();
        uri = edit.getData();
        hasChanged = false;

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

        name.setOnTouchListener(touchListener);
        breed.setOnTouchListener(touchListener);
        weight.setOnTouchListener(touchListener);

        genderSpinner = (Spinner)findViewById(R.id.spinner);
        genderSpinner.setOnTouchListener(touchListener);
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
        if(!val_name.equals(prev_name) || !val_breed.equals(prev_breed) || gender!=prev_gender || val_weight!=prev_weight)
            hasChanged = true;
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
        if(uri!=null && hasChanged)
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
                if(uri!=null)
                {
                    DialogInterface.OnClickListener deleteButtonListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            long dltd = getContentResolver().delete(uri, null, null);
                            if(dltd!=0)
                            {
                                Toast.makeText(AddActivity.this, "Deleted record(s)", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(AddActivity.this, "Error deleting", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    };

                    showDeleteDialog(deleteButtonListener);
                    return true;
                }
                clearData();
                return true;
            case android.R.id.home:
                checkForChange();
                if(!hasChanged)
                {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        NavUtils.navigateUpFromSameTask(AddActivity.this);
                    }
                };
                showDiscardDialog(discardButtonListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        checkForChange();
        if(!hasChanged)
        {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        };
        showDiscardDialog(discardButtonListener);
    }

    //create dialog box
    private void showDiscardDialog(DialogInterface.OnClickListener discardButtonListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discardmessage);
        builder.setPositiveButton(R.string.positive, discardButtonListener);
        builder.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener()
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
        try {
            prev_name = data.getString(data.getColumnIndexOrThrow(tableCols.COL_NAME));
            name.setText(prev_name);
            prev_breed = data.getString(data.getColumnIndexOrThrow(tableCols.COL_BREED));
            breed.setText(prev_breed);
            prev_weight = data.getInt(data.getColumnIndexOrThrow(tableCols.COL_WEIGHT));
            weight.setText(String.valueOf(prev_weight));
            prev_gender = data.getInt(data.getColumnIndexOrThrow(tableCols.COL_GENDER));
            genderSpinner.setSelection(prev_gender);
        }
        catch(CursorIndexOutOfBoundsException e)
        {
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        clearData();
    }
}
