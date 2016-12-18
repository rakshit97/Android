package com.example.rakshit.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<QuakeData>>
{
    QuakeAdapter adapter;
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    TextView empty_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        ListView data_list = (ListView)findViewById(R.id.list);
        empty_tv = (TextView)findViewById(R.id.empty_view);
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
    public Loader<ArrayList<QuakeData>> onCreateLoader(int i, Bundle bundle)
    {
        return new DataLoader(this, USGS_REQUEST_URL);
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
