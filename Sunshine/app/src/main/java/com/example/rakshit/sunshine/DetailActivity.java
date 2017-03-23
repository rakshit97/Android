package com.example.rakshit.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity
{
    private ShareActionProvider shareActionProvider;
    private static String dataString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container_detail, new PlaceHolderFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if(shareActionProvider != null)
            shareActionProvider.setShareIntent(createShareIntent());
        return true;
    }

    private Intent createShareIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, dataString+"#Sunshine App");
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_settings)
        {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    //Inner fragment class
    public static class PlaceHolderFragment extends Fragment
    {
        public PlaceHolderFragment()
        {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView tv_details = (TextView)rootView.findViewById(R.id.tv_details);

            ForecastData data = getActivity().getIntent().getParcelableExtra("data");
            dataString = String.valueOf(data.getTemp_max()) + "/" + String.valueOf(data.getTemp_min())+ " - " + data.getCondition();
            tv_details.setText(dataString);
            return rootView;
        }
    }

    @Override
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
    }
}
