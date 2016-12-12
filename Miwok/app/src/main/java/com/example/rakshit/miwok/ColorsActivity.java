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

        ArrayList<WordsList> colors = new ArrayList<WordsList>(8);

        colors.add(new WordsList("Weṭeṭṭi", "Red", R.drawable.color_red));
        colors.add(new WordsList("Chokokki", "Green", R.drawable.color_green));
        colors.add(new WordsList("Takaakki", "Brown", R.drawable.color_brown));
        colors.add(new WordsList("Topoppi", "Gray", R.drawable.color_gray));
        colors.add(new WordsList("Kululli", "Black", R.drawable.color_black));
        colors.add(new WordsList("Kelelli", "White", R.drawable.color_white));
        colors.add(new WordsList("Topiisә", "Dusty Yellow", R.drawable.color_dusty_yellow));
        colors.add(new WordsList("Chiwiiṭә", "Mustard Yellow", R.drawable.color_mustard_yellow));

        WordsListAdapter colorsAdapter = new WordsListAdapter(this, colors, R.color.category_colors);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(colorsAdapter);
    }
}
