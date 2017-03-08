package com.example.rakshit.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.rakshit.pets.data.PetsContract.tableCols;


public class DisplayAdapter extends CursorAdapter
{
    public DisplayAdapter(Context context, Cursor cursor)
    {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_pet, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView name_tv = (TextView) view.findViewById(R.id.name_tv);
        TextView summary_tv = (TextView) view.findViewById(R.id.summary_tv);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(tableCols.COL_NAME));
        String breed = cursor.getString(cursor.getColumnIndexOrThrow(tableCols.COL_BREED));
        name_tv.setText(name);
        summary_tv.setText(breed);
    }
}
