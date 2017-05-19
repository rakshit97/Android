package com.example.rakshit.spiderginternship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment
{
    private static final String by = "By BeInspired";

    public static final List<YouTubeVideoDataHolder> VIDEO_LIST;
    static {
        List<YouTubeVideoDataHolder> list = new ArrayList<>();
        list.add(new YouTubeVideoDataHolder("xp2qjshr-r4", "Retrain your Mind", by, false));
        list.add(new YouTubeVideoDataHolder("0U78LfCUU9o", "Keep going after your Dreams", by, true));
        list.add(new YouTubeVideoDataHolder("jwjGw3BCryI", "Winner's Mindset", by, true));
        list.add(new YouTubeVideoDataHolder("h52uKo6mris", "Your time is NOW", by, false));
        list.add(new YouTubeVideoDataHolder("1KGU6iB5MVE", "No Excuses!", by, false));
        list.add(new YouTubeVideoDataHolder("bYMUb4uQZoo", "Do what is Hard", by, false));
        list.add(new YouTubeVideoDataHolder("HeGPn5zxegY", "The Winning Mentality", by, true));
        list.add(new YouTubeVideoDataHolder("CPQ1budJRIQ", "Prove them Wrong", by, false));
        list.add(new YouTubeVideoDataHolder("-iPLKdjtC6U", "How successful people think", by, true));
        list.add(new YouTubeVideoDataHolder("1cRL9kpERkU", "Forget the Past", by, true));
        VIDEO_LIST = Collections.unmodifiableList(list);
    }

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private DisplayAdapter adapter;
    View rootView;

    public MainFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean auto = preferences.getBoolean(getString(R.string.autoplay_key), false);

        ListView listView = (ListView)rootView.findViewById(R.id.list);
        adapter = new DisplayAdapter(getActivity(), VIDEO_LIST);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(), BuildConfig.API_KEY, VIDEO_LIST.get(i).getVideoId(), 0, auto, false);
                if (canResolveIntent(intent))
                {
                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                }
                else
                {
                    YouTubeInitializationResult.SERVICE_MISSING.getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id==R.id.settings)
        {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_START_STANDALONE_PLAYER && resultCode!=RESULT_OK)
        {
            YouTubeInitializationResult reason = YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (reason.isUserRecoverableError())
            {
                reason.getErrorDialog(getActivity(), 0).show();
            }
            else
                Toast.makeText(getContext(), "Error initializing player: " + reason.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean canResolveIntent(Intent intent)
    {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        adapter.releaseLoaders();
    }
}
