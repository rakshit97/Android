package com.example.rakshit.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        ArrayList<WordsList> nums = new ArrayList<WordsList>(8);

        nums.add(new WordsList("Weṭeṭṭi", "Red"));
        nums.add(new WordsList("Chokokki", "Green"));
        nums.add(new WordsList("Takaakki", "Brown"));
        nums.add(new WordsList("Topoppi", "Gray"));
        nums.add(new WordsList("Kululli", "Black"));
        nums.add(new WordsList("Kelelli", "White"));
        nums.add(new WordsList("Topiisә", "Dusty Yellow"));
        nums.add(new WordsList("Chiwiiṭә", "Mustard Yellow"));

        WordsListAdapter colorsAdapter = new WordsListAdapter(this, nums);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(colorsAdapter);
    }
}
