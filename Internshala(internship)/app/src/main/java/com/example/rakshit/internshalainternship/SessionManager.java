package com.example.rakshit.internshalainternship;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager
{
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    public static final String PREF_NAME = "UserStatus";
    public static final String KEY_NAME = "name";
    public static final String KEY_STATUS = "status";

    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void createSession(String uname)
    {
        editor.putBoolean(KEY_STATUS, true);
        editor.putString(KEY_NAME, uname);
        editor.commit();
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(KEY_STATUS, false);
    }

    public String getUsername()
    {
        return pref.getString(KEY_NAME, null);
    }
}
