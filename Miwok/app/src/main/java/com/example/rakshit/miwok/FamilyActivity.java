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

        ArrayList<WordsList> relations = new ArrayList<WordsList>(10);

        relations.add(new WordsList("әpә", "Father", R.drawable.family_father));
        relations.add(new WordsList("әṭa", "Mother", R.drawable.family_mother));
        relations.add(new WordsList("Angsi", "Son", R.drawable.family_son));
        relations.add(new WordsList("Tune", "Daughter", R.drawable.family_daughter));
        relations.add(new WordsList("Taachi", "Older Brother", R.drawable.family_older_brother));
        relations.add(new WordsList("Chalitti", "Younger Brother", R.drawable.family_younger_brother));
        relations.add(new WordsList("Teṭe", "Older Sister", R.drawable.family_older_sister));
        relations.add(new WordsList("Kolliti", "Younger Sister", R.drawable.family_younger_sister));
        relations.add(new WordsList("Paapa", "Grandfather", R.drawable.family_grandfather));
        relations.add(new WordsList("ama", "Grandmother", R.drawable.family_grandmother));

        WordsListAdapter familyAdapter = new WordsListAdapter(this, relations, R.color.category_family);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(familyAdapter);
    }
}
