package com.example.rakshit.indiezinternship;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataAdapter extends CursorAdapter
{
    public DataAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ((TextView)view.findViewById(android.R.id.text1)).setText(cursor.getString(cursor.getColumnIndexOrThrow(DBContract.tableCols.COL_WORD)));
    }

    @Override
    public int getCount()
    {
        return 10;
    }
}
