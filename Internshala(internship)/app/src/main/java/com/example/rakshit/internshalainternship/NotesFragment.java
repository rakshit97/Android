package com.example.rakshit.internshalainternship;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.rakshit.internshalainternship.data.UsersContract;

import static com.example.rakshit.internshalainternship.data.UsersContract.COL_ID;
import static com.example.rakshit.internshalainternship.data.UsersContract.COL_USERNAME;

public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    SessionManager manager;
    NotesAdapter adapter;
    RecyclerView recyclerView;
    Cursor data = null;
    final String[] PROJECTION = new String[]{COL_ID, COL_USERNAME, UsersContract.NotesEntries.COL_TOPIC, UsersContract.NotesEntries.COL_TEXT};
    String SELECTION;
    String[] SELECTION_ARGS;

    public NotesFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View notes_view = inflater.inflate(R.layout.fragment_notes, container, false);
        ((MainActivity)getActivity()).setupActionBarForDisplay("Note It!");
        setHasOptionsMenu(true);
        manager = new SessionManager(getContext());
        SELECTION_ARGS = new String[]{manager.getUsername()};
        SELECTION = COL_USERNAME + "=?";
        getLoaderManager().initLoader(1, null, this);

        //setup adapter
        adapter = new NotesAdapter(getContext(), data);
        recyclerView = (RecyclerView)notes_view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        //setup FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton)notes_view.findViewById(R.id.button_add);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putString("URI", null);
                EditFragment ef = new EditFragment();
                ef.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, ef).addToBackStack("notes").commit();
            }
        });
        return notes_view;
    }



    //inflate menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);

        inflater.inflate(R.menu.menu_notesfragment, menu);
    }

    //listener for menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.menu_del_all:
                long num = getContext().getContentResolver().delete(UsersContract.NotesEntries.CONTENT_URI, SELECTION, SELECTION_ARGS);
                if(num>0)
                    Toast.makeText(getContext(), "Deleted all notes", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Error deleting notes", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_logout:
                SharedPreferences preferences = getContext().getSharedPreferences(SessionManager.PREF_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(SessionManager.KEY_STATUS, false);
                editor.commit();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                manager.beginTransaction().add(R.id.container_main, new LoginFragment()).commit();

        }
        return super.onOptionsItemSelected(item);
    }

    //Loader Callbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(getContext(), UsersContract.NotesEntries.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }
}
