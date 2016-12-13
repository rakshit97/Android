package com.example.rakshit.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity
{
    MediaPlayer myPlayer;

    private MediaPlayer.OnCompletionListener complistener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            releasePlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        final ArrayList<WordsList> relations = new ArrayList<WordsList>(10);

        relations.add(new WordsList("әpә", "Father", R.drawable.family_father, R.raw.family_father));
        relations.add(new WordsList("әṭa", "Mother", R.drawable.family_mother, R.raw.family_mother));
        relations.add(new WordsList("Angsi", "Son", R.drawable.family_son, R.raw.family_son));
        relations.add(new WordsList("Tune", "Daughter", R.drawable.family_daughter, R.raw.family_daughter));
        relations.add(new WordsList("Taachi", "Older Brother", R.drawable.family_older_brother, R.raw.family_older_brother));
        relations.add(new WordsList("Chalitti", "Younger Brother", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        relations.add(new WordsList("Teṭe", "Older Sister", R.drawable.family_older_sister, R.raw.family_older_sister));
        relations.add(new WordsList("Kolliti", "Younger Sister", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        relations.add(new WordsList("Paapa", "Grandfather", R.drawable.family_grandfather, R.raw.family_grandfather));
        relations.add(new WordsList("Ama", "Grandmother", R.drawable.family_grandmother, R.raw.family_grandmother));

        WordsListAdapter familyAdapter = new WordsListAdapter(this, relations, R.color.category_family);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(familyAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                releasePlayer();
                myPlayer = myPlayer.create(FamilyActivity.this, relations.get(i).getaudio_src());
                myPlayer.start();

                myPlayer.setOnCompletionListener(complistener);
            }
        });
    }

    private void releasePlayer()
    {
        if(myPlayer!=null)
        {
            myPlayer.release();
            myPlayer = null;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        releasePlayer();
    }
}
