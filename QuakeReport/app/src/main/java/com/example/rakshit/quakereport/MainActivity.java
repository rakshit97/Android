package com.example.rakshit.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<QuakeData>>
{
    QuakeAdapter adapter;
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    TextView empty_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empty_tv = (TextView)findViewById(R.id.empty_view);

        ConnectivityManager conn_manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net_info = conn_manager.getActiveNetworkInfo();
        if(net_info!=null && net_info.isConnected())
        {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            findViewById(R.id.progress).setVisibility(View.GONE);
            empty_tv.setText(getString(R.string.no_connection));
        }

        ListView data_list = (ListView)findViewById(R.id.list);
        data_list.setEmptyView(empty_tv);
        adapter = new QuakeAdapter(this, new ArrayList<QuakeData>(10));

        data_list.setAdapter(adapter);

        data_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String url = adapter.getItem(i).getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id==R.id.settings)
        {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<QuakeData>> onCreateLoader(int i, Bundle bundle)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMag = sharedPrefs.getString(getString(R.string.min_magnitude_key), getString(R.string.min_magnitude_default));
        String orderBy = sharedPrefs.getString(getString(R.string.order_by_key), getString(R.string.order_by_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMag);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new DataLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<QuakeData>> loader, ArrayList<QuakeData> quakeDatas)
    {
        findViewById(R.id.progress).setVisibility(View.GONE);
        adapter.clear();
        if(quakeDatas!=null && !quakeDatas.isEmpty())
            adapter.addAll(quakeDatas);
        else
            empty_tv.setText(getString(R.string.no_data));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<QuakeData>> loader)
    {
        adapter.clear();
    }
}
