package com.example.rakshit.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    MediaPlayer song;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        song = MediaPlayer.create(this, R.raw.fightsong);
    }

    public void play_song(View view)
    {
        Toast.makeText(this, "Playing Fight Song by Rachel Platten",Toast.LENGTH_SHORT).show();
        song.start();
    }

    public void pause_song(View view)
    {
        Toast.makeText(this, "Paused",Toast.LENGTH_SHORT).show();
        song.pause();
    }
}
