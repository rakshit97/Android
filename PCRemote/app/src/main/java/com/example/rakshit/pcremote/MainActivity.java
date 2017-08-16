package com.example.rakshit.pcremote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity
{
    private String currPointer="";
    private RelativeLayout parent;
    private TextView tv;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.text);

        parent = (RelativeLayout) findViewById(R.id.parent);
        findViewById(R.id.button_connect).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                connect();
            }
        });

        findViewById(R.id.button_disconnect).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    socket.close();
                }
                catch (Exception e)
                {
                    Log.e("main", Log.getStackTraceString(e));
                }
            }
        });
    }

    public void connect()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    socket = new Socket(InetAddress.getByName("192.168.1.53"), 10000);
                    socket.setKeepAlive(true);
                    socket.setSendBufferSize(1024);
                    /*BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("PCRemote connecting...");
                    bw.flush();*/
                } catch (Exception e)
                {
                    Log.e("main", "" + Log.getStackTraceString(e));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                new ASyncClass(parent).execute();
            }
        }.execute();
    }

    private class ASyncClass extends AsyncTask<Void, Void, Void>
    {
        BufferedWriter bw;

        ASyncClass(View view)
        {
            View.OnTouchListener listener = new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent event)
                {
                    if ((event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) && event.getAction() != MotionEvent.ACTION_OUTSIDE)
                    {
                        float x = event.getX();
                        float y = event.getY();
                        String sx = String.format("%08.3f", x);
                        String sy = String.format("%08.3f", y);
                        tv.setText(sx + "," + sy);
                        currPointer = String.valueOf(sx + "," + sy + ",");
                    }
                    return true;
                }
            };
            view.setOnTouchListener(listener);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                while (true)
                {
                    String msg = currPointer;
                    Log.e("main", ""+msg);
                    bw.write(msg);
                    bw.flush();
                    Thread.sleep(1);
                }
            }
            catch (Exception e)
            {
                Log.e("bg", Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            try
            {
                bw.close();
                socket.close();
            } catch (Exception e)
            {
                Log.e("main", Log.getStackTraceString(e));
            }
        }
    }
}
