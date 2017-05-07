package com.example.rakshit.internshalainternship;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rakshit.internshalainternship.data.UsersContract.UserEntries;

import static com.example.rakshit.internshalainternship.data.UsersContract.COL_USERNAME;

public class LoginFragment extends Fragment
{
    SessionManager session;

    public LoginFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        FrameLayout frameLayout = (FrameLayout)getActivity().findViewById(R.id.container_main);
        frameLayout.removeAllViews();
        final View login_view = inflater.inflate(R.layout.fragment_login, container, false);
        EditText username = (EditText)login_view.findViewById(R.id.username);
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(username.getWindowToken(), InputMethodManager.SHOW_FORCED);

        //register button listener
        Button button = (Button)login_view.findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String uname = ((TextView)login_view.findViewById(R.id.username)).getText().toString().trim();
                String pwd = ((TextView)login_view.findViewById(R.id.password)).getText().toString().trim();
                register(uname, pwd);
            }
        });

        //login button listener
        Button button1 = (Button)login_view.findViewById(R.id.button_login);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String uname = ((TextView)login_view.findViewById(R.id.username)).getText().toString().trim();
                String pwd = ((TextView)login_view.findViewById(R.id.password)).getText().toString().trim();
                login(uname, pwd);
            }
        });
        return login_view;
    }

    private boolean checkEmpty(String uname, String pwd)
    {
        return (uname.isEmpty() || uname == null || uname.length() <= 0 || pwd.isEmpty() || pwd == null || pwd.length() <= 0);
    }

    private void register(String uname, String pwd)
    {
        //check if username and password fields are empty
        if(checkEmpty(uname, pwd))
        {
            Toast.makeText(getContext(), "Cannot register", Toast.LENGTH_SHORT).show();
            return;
        }

        //check if username already exists
        String selection = "(" + COL_USERNAME + " = ?)";
        String[] selectionArgs = new String[]{uname};
        Cursor cursor = getContext().getContentResolver().query(UserEntries.CONTENT_URI, null, selection, selectionArgs, null);
        if(cursor.getCount()>0)
        {
            Toast.makeText(getContext(), "This username can't be used", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }

        //if valid username, insert into users database and check if insertion was successful
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, uname);
        values.put(UserEntries.COL_PASSWORD, pwd);

        long rowID = ContentUris.parseId(getContext().getContentResolver().insert(UserEntries.CONTENT_URI, values));
        if(rowID == -1)
            Toast.makeText(getContext(), "Cannot register", Toast.LENGTH_SHORT).show();
        else
        {
            session = new SessionManager(getContext());
            session.createSession(uname);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NotesFragment(), "notes").commit();
        }
    }

    private void login(String uname, String pwd)
    {
        //check if fields are empty
        if(checkEmpty(uname, pwd))
        {
            Toast.makeText(getContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        //check if credentials are correct
        String selection = "((" + COL_USERNAME + " = ?) AND (" + UserEntries.COL_PASSWORD + " = ?))";
        String[] selectionArgs = new String[]{uname, pwd};
        Cursor cursor = getContext().getContentResolver().query(UserEntries.CONTENT_URI, null, selection, selectionArgs, null);
        if(cursor.getCount() <= 0)
        {
            Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        else
        {
            session = new SessionManager(getContext());
            session.createSession(uname);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NotesFragment(), "notes").commit();
        }
    }
}
