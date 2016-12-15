package com.example.rakshit.quakereport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> data = new ArrayList<String>(7);

        data.add("San Francisco");
        data.add("London");
        data.add("Tokyo");
        data.add("Mexico City");
        data.add("Moscow");
        data.add("Rio de Janeiro");
        data.add("Paris");

        ListView data_list = (ListView)findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        data_list.setAdapter(adapter);
    }
}
