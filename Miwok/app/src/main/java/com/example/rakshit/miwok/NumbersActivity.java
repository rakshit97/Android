package com.example.rakshit.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        ArrayList <String> eng = new ArrayList<String>(10);
        eng.add("one");
        eng.add("two");
        eng.add("three");
        eng.add("four");
        eng.add("five");
        eng.add("six");
        eng.add("seven");
        eng.add("eight");
        eng.add("nine");
        eng.add("ten");

        //LinearLayout rootView = (LinearLayout)findViewById(R.id.activity_numbers);
        //TextView[] nums = new TextView[10];
        ArrayAdapter<String> numsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eng);

        ListView list = (ListView)findViewById(R.id.nums_list);
        list.setAdapter(numsAdapter);
    }
}
