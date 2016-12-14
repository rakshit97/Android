package com.example.rakshit.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment
{
    private MediaPlayer myPlayer;

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

    public PhrasesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.sub_activity, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

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

        WordsListAdapter phrasesAdapter = new WordsListAdapter(getActivity(), phrases, R.color.category_phrases);

        ListView list = (ListView)rootView.findViewById(R.id.list);
        list.setAdapter(phrasesAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                releasePlayer();

                int status = audioManager.requestAudioFocus(afListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(status==AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    myPlayer = myPlayer.create(getActivity(), phrases.get(i).getaudio_src());
                    myPlayer.start();

                    myPlayer.setOnCompletionListener(complistener);
                }
            }
        });

        return rootView;
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
    public void onStop()
    {
        super.onStop();
        releasePlayer();
    }
}
