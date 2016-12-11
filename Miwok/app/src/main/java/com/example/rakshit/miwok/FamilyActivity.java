package com.example.rakshit.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        ArrayList<WordsList> nums = new ArrayList<WordsList>(10);

        nums.add(new WordsList("әpә", "Father"));
        nums.add(new WordsList("әṭa", "Mother"));
        nums.add(new WordsList("Angsi", "Son"));
        nums.add(new WordsList("Tune", "Daughter"));
        nums.add(new WordsList("Taachi", "Older Brother"));
        nums.add(new WordsList("Chalitti", "Younger Brother"));
        nums.add(new WordsList("Teṭe", "Older Sister"));
        nums.add(new WordsList("Kolliti", "Younger Sister"));
        nums.add(new WordsList("ama", "Grandmother"));
        nums.add(new WordsList("Paapa", "Grandfather"));

        WordsListAdapter familyAdapter = new WordsListAdapter(this, nums);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(familyAdapter);
    }
}
