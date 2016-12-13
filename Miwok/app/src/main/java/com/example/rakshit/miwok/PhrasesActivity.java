package com.example.rakshit.miwok;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity
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

        final ArrayList<WordsList> phrases = new ArrayList<WordsList>(10);

        phrases.add(new WordsList("Minto wuksus", "Where are you going?", R.raw.phrase_where_are_you_going));
        phrases.add(new WordsList("Tinnә oyaase'nә", "What is your name?", R.raw.phrase_what_is_your_name));
        phrases.add(new WordsList("Oyaaset ...", "My name is ...", R.raw.phrase_my_name_is));
        phrases.add(new WordsList("Michәksәs?", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        phrases.add(new WordsList("Kuchi achit", "I’m feeling good.", R.raw.phrase_im_feeling_good));
        phrases.add(new WordsList("Tәәnәs'aa?", "Are you coming?", R.raw.phrase_are_you_coming));
        phrases.add(new WordsList("Hәә’әәnәm", "Yes, I’m coming.", R.raw.phrase_yes_im_coming));
        phrases.add(new WordsList("әәnәm", "I’m coming.", R.raw.phrase_im_coming));
        phrases.add(new WordsList("Yoowutis", "Let’s go.", R.raw.phrase_lets_go));
        phrases.add(new WordsList("әnni'nem", "Come here.", R.raw.phrase_come_here));

        WordsListAdapter phrasesAdapter = new WordsListAdapter(this, phrases, R.color.category_phrases);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(phrasesAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                releasePlayer();
                myPlayer = myPlayer.create(PhrasesActivity.this, phrases.get(i).getaudio_src());
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
