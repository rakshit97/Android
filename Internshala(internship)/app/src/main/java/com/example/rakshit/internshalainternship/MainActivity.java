package com.example.rakshit.internshalainternship;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SessionManager session = new SessionManager(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        if(!session.isLoggedIn())
            fm.beginTransaction().add(R.id.container_main, new LoginFragment()).commit();
        else
            fm.beginTransaction().replace(R.id.container_main, new NotesFragment(), "notes").commit();
    }

    public void setupActionBarForEdit(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setupActionBarForDisplay(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }
}
