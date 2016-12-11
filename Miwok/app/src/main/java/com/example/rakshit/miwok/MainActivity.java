package com.example.rakshit.miwok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchNumbersActivity(View view)
    {
        Intent i = new Intent(this, NumbersActivity.class);
        startActivity(i);
    }

    public void launchFamilyActivity(View view)
    {
        Intent i = new Intent(this, FamilyActivity.class);
        startActivity(i);
    }

    public void launchColorsActivity(View view)
    {
        Intent i = new Intent(this, ColorsActivity.class);
        startActivity(i);
    }

    public void launchPhrasesActivity(View view)
    {
        Intent i = new Intent(this, PhrasesActivity.class);
        startActivity(i);
    }
}
