package com.example.rakshit.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        ArrayList<WordsList> nums = new ArrayList<WordsList>(10);

        nums.add(new WordsList("Lutti", "One", R.drawable.number_one));
        nums.add(new WordsList("Otiiko", "Two", R.drawable.number_two));
        nums.add(new WordsList("Tolookosu", "Three", R.drawable.number_three));
        nums.add(new WordsList("Oyyisa", "Four", R.drawable.number_four));
        nums.add(new WordsList("Massokka", "Five", R.drawable.number_five));
        nums.add(new WordsList("Temmokka", "Six", R.drawable.number_six));
        nums.add(new WordsList("Kenekaku", "Seven", R.drawable.number_seven));
        nums.add(new WordsList("Kawinta", "Eight", R.drawable.number_eight));
        nums.add(new WordsList("Wo'e", "Nine", R.drawable.number_nine));
        nums.add(new WordsList("Na'aacha", "Ten", R.drawable.number_ten));

        WordsListAdapter numsAdapter = new WordsListAdapter(this, nums, R.color.category_numbers);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(numsAdapter);
    }
}
