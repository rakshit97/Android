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

        ArrayList<QuakeData> data = new ArrayList<QuakeData>(7);

        data.add(new QuakeData(7.2, "San Francisco", "Feb 2, 2016"));
        data.add(new QuakeData(6.1, "London", "July 20, 2015"));
        data.add(new QuakeData(3.9, "Tokyo", "Nov 10, 2014"));
        data.add(new QuakeData(5.4, "Mexico City", "May 3, 2014"));
        data.add(new QuakeData(2.8, "Moscow", "May 31, 2013"));
        data.add(new QuakeData(4.9, "Rio de Janeiro", "Aug 19, 2012"));
        data.add(new QuakeData(1.6, "Paris", "Oct 30, 2011"));

        ListView data_list = (ListView)findViewById(R.id.list);
        QuakeAdapter adapter = new QuakeAdapter(this, data);

        data_list.setAdapter(adapter);
    }
}
