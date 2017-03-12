package com.example.rakshit.sunshine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, new MainActivityFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.menu_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    public static class MainActivityFragment extends Fragment
    {
        public MainActivityFragment()
        {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ArrayList<String> fake_data = new ArrayList<String>(10);
            fake_data.add("Today-Sunny-36/23");
            fake_data.add("Today-Sunny-36/23");
            fake_data.add("Today-Rainy-36/23");
            fake_data.add("Today-Sunny-36/23");
            fake_data.add("Today-Stormy-36/23");
            fake_data.add("Today-Sunny-36/23");
            fake_data.add("Today-Snow-36/23");
            fake_data.add("Today-Sunny-36/23");
            fake_data.add("Today-Cloudy-36/23");
            fake_data.add("Today-Sunny-36/23");

            ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_forecast, R.id.tv_forecast, fake_data);
            ListView forecastList = (ListView)rootView.findViewById(R.id.lv_forecast);
            forecastList.setAdapter(forecastAdapter);

            return rootView;
        }
    }
}
