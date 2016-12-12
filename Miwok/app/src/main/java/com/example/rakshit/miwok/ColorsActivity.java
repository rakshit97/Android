package com.example.rakshit.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity
{
    MediaPlayer myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        final ArrayList<WordsList> colors = new ArrayList<WordsList>(8);

        colors.add(new WordsList("Weṭeṭṭi", "Red", R.drawable.color_red, R.raw.color_red));
        colors.add(new WordsList("Chokokki", "Green", R.drawable.color_green, R.raw.color_green));
        colors.add(new WordsList("Takaakki", "Brown", R.drawable.color_brown, R.raw.color_brown));
        colors.add(new WordsList("Topoppi", "Gray", R.drawable.color_gray, R.raw.color_gray));
        colors.add(new WordsList("Kululli", "Black", R.drawable.color_black, R.raw.color_black));
        colors.add(new WordsList("Kelelli", "White", R.drawable.color_white, R.raw.color_white));
        colors.add(new WordsList("Topiisә", "Dusty Yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colors.add(new WordsList("Chiwiiṭә", "Mustard Yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordsListAdapter colorsAdapter = new WordsListAdapter(this, colors, R.color.category_colors);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(colorsAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                myPlayer = myPlayer.create(ColorsActivity.this, colors.get(i).getaudio_src());
                myPlayer.start();
            }
        });
    }
}
