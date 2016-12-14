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
public class ColorsFragment extends Fragment
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

    public ColorsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.sub_activity, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<WordsList> colors = new ArrayList<WordsList>(8);

        colors.add(new WordsList("Weṭeṭṭi", "Red", R.drawable.color_red, R.raw.color_red));
        colors.add(new WordsList("Chokokki", "Green", R.drawable.color_green, R.raw.color_green));
        colors.add(new WordsList("Takaakki", "Brown", R.drawable.color_brown, R.raw.color_brown));
        colors.add(new WordsList("Topoppi", "Gray", R.drawable.color_gray, R.raw.color_gray));
        colors.add(new WordsList("Kululli", "Black", R.drawable.color_black, R.raw.color_black));
        colors.add(new WordsList("Kelelli", "White", R.drawable.color_white, R.raw.color_white));
        colors.add(new WordsList("Topiisә", "Dusty Yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colors.add(new WordsList("Chiwiiṭә", "Mustard Yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordsListAdapter colorsAdapter = new WordsListAdapter(getActivity(), colors, R.color.category_colors);

        ListView list = (ListView)rootView.findViewById(R.id.list);
        list.setAdapter(colorsAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                releasePlayer();

                int status = audioManager.requestAudioFocus(afListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(status==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    myPlayer = myPlayer.create(getActivity(), colors.get(i).getaudio_src());
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
