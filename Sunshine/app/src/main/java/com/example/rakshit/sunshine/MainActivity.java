package com.example.rakshit.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rakshit.sunshine.sync.SunshineSyncAdapter;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SunshineSyncAdapter.initializeSyncAdapter(this);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, new MainActivityFragment()).commit();
    }
}