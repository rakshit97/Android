package com.example.rakshit.courtcounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int A=0;
    int B=0;

    public void three_A(View view)
    {
        A+=3;
        display_A(A);
    }

    public void two_A(View view)
    {
        A+=2;
        display_A(A);
    }

    public void fthrow_A(View view)
    {
        A++;
        display_A(A);
    }

    public void three_B(View view)
    {
        B+=3;
        display_B(B);
    }

    public void two_B(View view)
    {
        B+=2;
        display_B(B);
    }

    public void fthrow_B(View view)
    {
        B++;
        display_B(B);
    }

    public void display_A(int A)
    {
        TextView score_A = (TextView)findViewById(R.id.score_A);
        score_A.setText(""+A);
    }

    public void display_B(int B)
    {
        TextView score_B = (TextView)findViewById(R.id.score_B);
        score_B.setText(""+B);
    }

    public void reset(View view)
    {
        A=0; B=0;
        display_A(A);
        display_B(B);
    }
}
