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

        ArrayList<WordsList> nums = new ArrayList<WordsList>(10);

        nums.add(new WordsList("Lutti", "One"));
        nums.add(new WordsList("Otiiko", "Two"));
        nums.add(new WordsList("Tolookosu", "Three"));
        nums.add(new WordsList("Oyyisa", "Four"));
        nums.add(new WordsList("Massokka", "Five"));
        nums.add(new WordsList("Temmokka", "Six"));
        nums.add(new WordsList("Kenekaku", "Seven"));
        nums.add(new WordsList("Kawinta", "Eight"));
        nums.add(new WordsList("Wo'e", "Nine"));
        nums.add(new WordsList("Na'aacha", "Ten"));

        WordsListAdapter colorsAdapter = new WordsListAdapter(this, nums);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(colorsAdapter);
    }
}
