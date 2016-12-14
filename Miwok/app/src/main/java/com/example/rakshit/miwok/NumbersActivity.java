package com.example.rakshit.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NumbersActivity extends AppCompatActivity
{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new NumbersFragment()).commit();

    }
}
