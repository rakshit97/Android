package com.example.rakshit.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import static com.example.rakshit.quakereport.QueryUtil.fetchData;

public class MainActivity extends AppCompatActivity
{
    QuakeAdapter adapter;
    private static final String USGS_REQUEST_URL =
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EarthQuakeAsyncTask task = new EarthQuakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

        ListView data_list = (ListView)findViewById(R.id.list);
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

    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, ArrayList<QuakeData>>
    {
        @Override
        protected ArrayList<QuakeData> doInBackground(String... urls)
        {
            if(urls.length<1 || urls[0]==null)
                return null;

            return fetchData(urls[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<QuakeData> data)
        {
            adapter.clear();
            if(data!=null && !data.isEmpty())
                adapter.addAll(data);
        }
    }
}
