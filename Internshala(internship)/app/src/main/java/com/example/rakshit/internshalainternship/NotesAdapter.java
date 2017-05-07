package com.example.rakshit.internshalainternship;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rakshit.internshalainternship.data.UsersContract;
import com.example.rakshit.internshalainternship.data.UsersContract.NotesEntries;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
{
    private CursorAdapter cursorAdapter;
    private Context _context;

    public NotesAdapter(Context context, Cursor data)
    {
        _context = context;
        cursorAdapter = new CursorAdapter(_context, data, 0)
        {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent)
            {
                return LayoutInflater.from(context).inflate(R.layout.card_notes, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor)
            {
                TextView tv_head = (TextView)view.findViewById(R.id.card_heading);
                TextView tv_content = (TextView)view.findViewById(R.id.card_content);
                String head = cursor.getString(cursor.getColumnIndexOrThrow(NotesEntries.COL_TOPIC));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(NotesEntries.COL_TEXT));
                tv_head.setText(head);
                tv_content.setText(content);
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    @Override
    public int getItemCount()
    {
        return cursorAdapter.getCount();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(cursorAdapter.newView(_context, cursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        cursorAdapter.getCursor().moveToPosition(position);
        final int p = position;
        cursorAdapter.bindView(holder.itemView, _context, cursorAdapter.getCursor());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Cursor mcursor = cursorAdapter.getCursor();
                mcursor.moveToPosition(p);
                Bundle bundle = new Bundle();
                bundle.putString("URI", ContentUris.withAppendedId(NotesEntries.CONTENT_URI, mcursor.getLong(mcursor.getColumnIndexOrThrow(UsersContract.COL_ID))).toString());
                EditFragment ef = new EditFragment();
                ef.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) _context;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_main, ef).addToBackStack("notes").commit();
            }
        });
    }

    public void swapCursor(Cursor cursor)
    {
        cursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }
}
