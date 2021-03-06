package com.example.rakshit.spiderginternship;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayAdapter extends ArrayAdapter<YouTubeVideoDataHolder>
{
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailToLoaders;
    private final ThumbnailListener thumbnailListener;
    private List<YouTubeVideoDataHolder> list;

    public DisplayAdapter(Activity context, ArrayList<YouTubeVideoDataHolder> data)
    {
        super(context, 0, data);
        this.list = data;
        thumbnailToLoaders = new HashMap<>();
        thumbnailListener = new ThumbnailListener();
    }

    public void releaseLoaders()
    {
        for(YouTubeThumbnailLoader loader:thumbnailToLoaders.values())
            loader.release();
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Nullable
    @Override
    public YouTubeVideoDataHolder getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View listItemView = convertView;
        YouTubeVideoDataHolder curr = getItem(position);

        if (listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_videos, parent, false);
            if (curr.getTaggggg()==1)
            {
                Log.e("adapter", "thumbnail1");
                YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) listItemView.findViewById(R.id.thumbnail);
                thumbnailView.setVisibility(View.VISIBLE);
                thumbnailView.setTag(curr.getVideoId());
                thumbnailView.initialize(BuildConfig.API_KEY, thumbnailListener);
            }
        }
        else
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_videos, parent, false);
            if(curr.getTaggggg()==1)
            {
                Log.e("adapter", "thumbnail2");
                YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) listItemView.findViewById(R.id.thumbnail);
                thumbnailView.setVisibility(View.VISIBLE);
                YouTubeThumbnailLoader loader = thumbnailToLoaders.get(thumbnailView);
                if (loader == null)
                    thumbnailView.setTag(curr.getVideoId());
                else
                {
                    thumbnailView.setImageResource(R.drawable.ic_schedule_black_24dp);
                    loader.setVideo(curr.getVideoId());
                }
            }
        }

        TextView name = (TextView) listItemView.findViewById(R.id.tv_name);
        TextView desc = (TextView) listItemView.findViewById(R.id.tv_desc);

        if(curr.getTaggggg()==2)
        {
            name.setText(curr.getVideoName());
            desc.setText(curr.getVideoDesc());
        }
        else
        {
            name.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        }

        return listItemView;
    }

    private final class ThumbnailListener implements YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader.OnThumbnailLoadedListener
    {
        @Override
        public void onInitializationSuccess(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader loader)
        {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailToLoaders.put(thumbnailView, loader);
            thumbnailView.setImageResource(R.drawable.ic_schedule_black_24dp);
            loader.setVideo((String) thumbnailView.getTag());
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult result)
        {
            thumbnailView.setImageResource(R.drawable.ic_error_black_24dp);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s)
        {

        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason)
        {
            thumbnailView.setImageResource(R.drawable.ic_error_black_24dp);
        }
    }
}
