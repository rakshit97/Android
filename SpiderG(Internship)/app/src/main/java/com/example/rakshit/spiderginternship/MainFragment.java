package com.example.rakshit.spiderginternship;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment
{
    private static final String by = "By BeInspired";
    YouTubeVideoDataHolder holder1 = new YouTubeVideoDataHolder("xp2qjshr-r4", "Retrain your Mind", 2, by);
    YouTubeVideoDataHolder holder2 = new YouTubeVideoDataHolder("xp2qjshr-r4", "Retrain your Mind", 1, by);

    public ArrayList<YouTubeVideoDataHolder> VIDEO_LIST = new ArrayList<>();
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
        VIDEO_LIST.add(new YouTubeVideoDataHolder("xp2qjshr-r4", "Retrain your Mind", 2, by));
        VIDEO_LIST.add(new YouTubeVideoDataHolder("0U78LfCUU9o", "Keep going after your Dreams", 1, by));
        setHasOptionsMenu(true);

        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean auto = preferences.getBoolean(getString(R.string.autoplay_key), false);

        final ListView listView = (ListView)rootView.findViewById(R.id.list);
        adapter = new DisplayAdapter(getActivity(), VIDEO_LIST);
        listView.setAdapter(adapter);

        FloatingActionButton bholder1 = (FloatingActionButton)rootView.findViewById(R.id.trigger_holder1);
        FloatingActionButton bholder2 = (FloatingActionButton)rootView.findViewById(R.id.trigger_holder2);

        bholder1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter.add(holder1);
                listView.smoothScrollToPosition(adapter.getCount());
            }
        });

        bholder2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter.add(holder2);
                listView.smoothScrollToPosition(adapter.getCount());
            }
        });

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
