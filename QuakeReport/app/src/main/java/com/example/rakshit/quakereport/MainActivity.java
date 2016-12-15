package com.example.rakshit.quakereport;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<QuakeData> data = QueryUtil.extractData();

        ListView data_list = (ListView)findViewById(R.id.list);
        QuakeAdapter adapter = new QuakeAdapter(this, data);

        data_list.setAdapter(adapter);
    }
}
