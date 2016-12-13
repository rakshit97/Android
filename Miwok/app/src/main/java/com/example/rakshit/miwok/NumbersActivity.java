package com.example.rakshit.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity
{
    MediaPlayer myPlayer;

    private AudioManager audioManager;

    private MediaPlayer.OnCompletionListener complistener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            releasePlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener afListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i)
        {
            if(i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                myPlayer.pause();
                myPlayer.seekTo(0);
            }
            else if(i==AudioManager.AUDIOFOCUS_GAIN)
                myPlayer.start();
            else if(i==AudioManager.AUDIOFOCUS_LOSS)
                releasePlayer();
        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<WordsList> nums = new ArrayList<WordsList>(10);

        nums.add(new WordsList("Lutti", "One", R.drawable.number_one, R.raw.number_one));
        nums.add(new WordsList("Otiiko", "Two", R.drawable.number_two, R.raw.number_two));
        nums.add(new WordsList("Tolookosu", "Three", R.drawable.number_three, R.raw.number_three));
        nums.add(new WordsList("Oyyisa", "Four", R.drawable.number_four, R.raw.number_four));
        nums.add(new WordsList("Massokka", "Five", R.drawable.number_five, R.raw.number_five));
        nums.add(new WordsList("Temmokka", "Six", R.drawable.number_six, R.raw.number_six));
        nums.add(new WordsList("Kenekaku", "Seven", R.drawable.number_seven, R.raw.number_seven));
        nums.add(new WordsList("Kawinta", "Eight", R.drawable.number_eight, R.raw.number_eight));
        nums.add(new WordsList("Wo'e", "Nine", R.drawable.number_nine, R.raw.number_nine));
        nums.add(new WordsList("Na'aacha", "Ten", R.drawable.number_ten, R.raw.number_ten));

        WordsListAdapter numsAdapter = new WordsListAdapter(this, nums, R.color.category_numbers);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(numsAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                releasePlayer();

                int status = audioManager.requestAudioFocus(afListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(status==AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    myPlayer = myPlayer.create(NumbersActivity.this, nums.get(i).getaudio_src());
                    myPlayer.start();

                    myPlayer.setOnCompletionListener(complistener);
                }
            }
        });
    }

    private void releasePlayer()
    {
        if(myPlayer!=null)
        {
            myPlayer.release();
            myPlayer = null;
            audioManager.abandonAudioFocus(afListener);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        releasePlayer();
    }
}
