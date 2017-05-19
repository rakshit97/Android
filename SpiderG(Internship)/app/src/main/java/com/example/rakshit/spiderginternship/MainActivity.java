package com.example.rakshit.spiderginternship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    private static final String by = "By BeInspired";

    public static final List<YouTubeVideoDataHolder> VIDEO_LIST;
    static {
        List<YouTubeVideoDataHolder> list = new ArrayList<>();
        list.add(new YouTubeVideoDataHolder("xp2qjshr-r4", "Retrain your Mind", by));
        list.add(new YouTubeVideoDataHolder("0U78LfCUU9o", "Keep going after your Dreams", by));
        list.add(new YouTubeVideoDataHolder("jwjGw3BCryI", "Winner's Mindset", by));
        list.add(new YouTubeVideoDataHolder("h52uKo6mris", "Your time is NOW", by));
        list.add(new YouTubeVideoDataHolder("1KGU6iB5MVE", "No Excuses!", by));
        list.add(new YouTubeVideoDataHolder("bYMUb4uQZoo", "Do what is Hard", by));
        list.add(new YouTubeVideoDataHolder("HeGPn5zxegY", "The Winning Mentality", by));
        list.add(new YouTubeVideoDataHolder("CPQ1budJRIQ", "Prove them Wrong", by));
        list.add(new YouTubeVideoDataHolder("-iPLKdjtC6U", "How successful people think", by));
        list.add(new YouTubeVideoDataHolder("1cRL9kpERkU", "Forget the Past", by));
        VIDEO_LIST = Collections.unmodifiableList(list);
    }

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private DisplayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean auto = preferences.getBoolean(getString(R.string.autoplay_key), false);
        final Boolean play;
        String player = preferences.getString(getString(R.string.player_key), getString(R.string.player_default_value));
        if (player.equals(getString(R.string.player_fullscreen_value)))
            play = false;
        else
            play = true;

        ListView listView = (ListView)findViewById(R.id.list);
        adapter = new DisplayAdapter(this, VIDEO_LIST);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, BuildConfig.API_KEY, VIDEO_LIST.get(i).getVideoId(), 0, auto, play);
                if (canResolveIntent(intent))
                {
                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                }
                else
                {
                    YouTubeInitializationResult.SERVICE_MISSING.getErrorDialog(MainActivity.this, REQ_RESOLVE_SERVICE_MISSING).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_START_STANDALONE_PLAYER && resultCode!=RESULT_OK)
        {
            YouTubeInitializationResult reason = YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (reason.isUserRecoverableError())
            {
                reason.getErrorDialog(this, 0).show();
            }
            else
                Toast.makeText(this, "Error initializing player: " + reason.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id==R.id.settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean canResolveIntent(Intent intent)
    {
        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        adapter.releaseLoaders();
    }
}
