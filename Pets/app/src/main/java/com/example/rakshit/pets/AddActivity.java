package com.example.rakshit.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.rakshit.pets.data.PetsDBHelper;

public class AddActivity extends AppCompatActivity
{

    private Spinner genderSpinner;
    private int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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
                    if(selected.equals(getString(R.string.gender_female)))
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
        PetsDBHelper dbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String name = findViewById(R.id.name).toString().trim();
        String breed = findViewById(R.id.breed).toString().trim();
        String weight = findViewById(R.id.weight).toString().trim();

        ContentValues values = new ContentValues();
        values.put(tableCols.COL_NAME, name);
        values.put(tableCols.COL_BREED, breed);
        values.put(tableCols.COLUMN_GENDER, gender);
        values.put(tableCols.COLUMN_WEIGHT, weight);

        long rowId = db.insert(tableCols.TABLE_NAME, null, values);
        if(rowId!=-1)
            Toast.makeText(this, "Added pet with row id "+rowId, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error adding pet" , Toast.LENGTH_SHORT).show();

        goBack();
    }

    private void clearData()
    {
        EditText name = (EditText)findViewById(R.id.name);
        name.setText("");
        EditText breed = (EditText)findViewById(R.id.breed);
        breed.setText("");
        EditText weight = (EditText)findViewById(R.id.weight);
        weight.setText("");
        genderSpinner.setSelection(0);
    }

    private void goBack()
    {
        startActivity(new Intent(AddActivity.this, MainActivity.class));
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
}
