package com.example.rakshit.indiezinternship;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
{
    int flag = 0;
    ListView lv;
    DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.listview);

        ((EditText)findViewById(R.id.edit_search)).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.e("main", ""+s.toString());
                Cursor c = getContentResolver().query(DBContract.BASE_URI, null, null, new String[]{s.toString()+"%"}, null);
                if (c!=null && c.getCount()>0)
                {
                    findViewById(R.id.no_res).setVisibility(View.GONE);
                    if (flag==0)
                    {
                        adapter = new DataAdapter(MainActivity.this, c);
                        lv.setAdapter(adapter);
                        flag = 1;
                    }
                    else
                    {
                        adapter.swapCursor(c);
                    }
                }
                else
                    findViewById(R.id.no_res).setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s)
            { }
        });
    }
}
