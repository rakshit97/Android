package com.example.rakshit.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        ArrayList<WordsList> nums = new ArrayList<WordsList>(10);

        nums.add(new WordsList("Minto wuksus", "Where are you going?"));
        nums.add(new WordsList("Tinnә oyaase'nә", "What is your name?"));
        nums.add(new WordsList("Oyaaset ...", "My name is ..."));
        nums.add(new WordsList("Michәksәs?", "How are you feeling?"));
        nums.add(new WordsList("Kuchi achit", "I’m feeling good."));
        nums.add(new WordsList("Tәәnәs'aa?", "Are you coming?"));
        nums.add(new WordsList("Hәә’әәnәm", "Yes, I’m coming."));
        nums.add(new WordsList("әәnәm", "I’m coming."));
        nums.add(new WordsList("Yoowutis", "Let’s go."));
        nums.add(new WordsList("әnni'nem", "Come here."));

        WordsListAdapter phrasesAdapter = new WordsListAdapter(this, nums);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(phrasesAdapter);
    }
}
