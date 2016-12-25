package com.example.rakshit.pets;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String selected = (String) adapterView.getItemAtPosition(i);
                if(!TextUtils.isEmpty(selected))
                {
                    if(selected.equals(getString(R.string.gender_male)))
                        gender = 1;
                    if(selected.equals(getString(R.string.gender_female)))
                        gender = 2;
                    else
                        gender = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                gender = 0;
            }
        });
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
                return true;
            case R.id.option_del:
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
