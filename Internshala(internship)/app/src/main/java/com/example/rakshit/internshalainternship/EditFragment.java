package com.example.rakshit.internshalainternship;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rakshit.internshalainternship.data.UsersContract;


public class EditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    Uri uri = null;
    SessionManager manager;
    final String[] PROJECTION = new String[]{UsersContract.NotesEntries.COL_TOPIC, UsersContract.NotesEntries.COL_TEXT};
    TextView head, content;
    String prev_head, prev_content;

    public EditFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View edit_view = inflater.inflate(R.layout.fragment_edit, container, false);
        head = (TextView)edit_view.findViewById(R.id.head);
        content = (TextView)edit_view.findViewById(R.id.content);
        setHasOptionsMenu(true);
        manager = new SessionManager(getContext());

        Bundle args = this.getArguments();
        String s = args.getString("URI", null);
        if(s!=null)
            uri = Uri.parse(s);

        if(uri!=null)
        {
            ((MainActivity)getActivity()).setupActionBarForEdit("Edit note");
            getLoaderManager().initLoader(1, null, this);
        }
        else
            ((MainActivity)getActivity()).setupActionBarForEdit("Add a note");

        FloatingActionButton actionButton = (FloatingActionButton)edit_view.findViewById(R.id.fab_save);
        actionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                insertNote();
            }
        });

        return edit_view;
    }

    private Boolean checkForChange()
    {
        String curr_head = head.getText().toString().trim();
        String curr_content = content.getText().toString().trim();
        return !(curr_head.equals(prev_head) && curr_content.equals(prev_content));
    }

    private void insertNote()
    {
        String val_head = head.getText().toString().trim();
        String val_content = content.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(UsersContract.COL_USERNAME, manager.getUsername());
        values.put(UsersContract.NotesEntries.COL_TOPIC, val_head);
        values.put(UsersContract.NotesEntries.COL_TEXT, val_content);
        if(uri==null && !(val_content.isEmpty() && val_head.isEmpty()))
        {

            long rowId = ContentUris.parseId(getContext().getContentResolver().insert(UsersContract.NotesEntries.CONTENT_URI, values));
            if (rowId!=-1)
                Toast.makeText(getContext(), "Added note successfully with row id "+rowId, Toast.LENGTH_SHORT).show();
        }
        else if(uri!=null && checkForChange())
        {
            long updtd = getContext().getContentResolver().update(uri, values, null, null);
            if (updtd>0)
                Toast.makeText(getContext(), "note updated!", Toast.LENGTH_SHORT).show();
        }
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editfragment, menu);
    }

    //menu options onClickListener


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_delete:
                if(uri==null)
                {
                    head.setText("");
                    content.setText("");
                }
                else
                {
                    long dltd = getContext().getContentResolver().delete(uri, null, null);
                    if(dltd>0)
                        Toast.makeText(getContext(), "Note Deleted!", Toast.LENGTH_SHORT).show();
                }
                getActivity().getSupportFragmentManager().popBackStackImmediate();
                return true;
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
        return super.onOptionsItemSelected(item);
    }

    //loader callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(getContext(), uri, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        data.moveToFirst();
        try
        {
            prev_head = data.getString(data.getColumnIndexOrThrow(UsersContract.NotesEntries.COL_TOPIC));
            head.setText(prev_head);
            prev_content = data.getString(data.getColumnIndexOrThrow(UsersContract.NotesEntries.COL_TEXT));
            content.setText(prev_content);
        }
        catch (CursorIndexOutOfBoundsException e)
        {
            return;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        head.setText("");
        content.setText("");
    }
}
