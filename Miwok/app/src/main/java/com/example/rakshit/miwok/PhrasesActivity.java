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

        ArrayList<WordsList> phrases = new ArrayList<WordsList>(10);

        phrases.add(new WordsList("Minto wuksus", "Where are you going?"));
        phrases.add(new WordsList("Tinnә oyaase'nә", "What is your name?"));
        phrases.add(new WordsList("Oyaaset ...", "My name is ..."));
        phrases.add(new WordsList("Michәksәs?", "How are you feeling?"));
        phrases.add(new WordsList("Kuchi achit", "I’m feeling good."));
        phrases.add(new WordsList("Tәәnәs'aa?", "Are you coming?"));
        phrases.add(new WordsList("Hәә’әәnәm", "Yes, I’m coming."));
        phrases.add(new WordsList("әәnәm", "I’m coming."));
        phrases.add(new WordsList("Yoowutis", "Let’s go."));
        phrases.add(new WordsList("әnni'nem", "Come here."));

        WordsListAdapter phrasesAdapter = new WordsListAdapter(this, phrases, R.color.category_phrases);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(phrasesAdapter);
    }
}
