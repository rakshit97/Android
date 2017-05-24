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
    private String currPointer;
    private View.OnTouchListener listener;
    private RelativeLayout parent;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.text);

        parent = (RelativeLayout)findViewById(R.id.parent);

    }

    public void connect(View view)
    {
        new ASyncClass(parent).execute();

    }

    class ASyncClass extends AsyncTask<Void, Void, String>
    {
        BufferedWriter bw;
        Socket socket;

        public ASyncClass(View view)
        {
            listener = new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent event)
                {
                    if((event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) && event.getAction()!=MotionEvent.ACTION_OUTSIDE)
                    {
                        float x = event.getX();
                        float y = event.getY();
                        String sx = String.format("%08.3f", x);
                        String sy = String.format("%08.3f", y);
                        Log.e("main", sx+" "+sy);
                        tv.setText(sx+","+sy);
                        currPointer = String.valueOf(sx+","+sy+",");
                    }
                    return true;
                }
            };
            parent.setOnTouchListener(listener);
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            try
            {
                socket = new Socket(InetAddress.getByName("192.168.1.53"), 10000);
                socket.setKeepAlive(true);
                socket.setSendBufferSize(1024);
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                while(true)
                {
                    String msg = currPointer;
                    bw.write(msg);
                    bw.flush();
                    Thread.sleep(1);
                }
            }
            catch (Exception e)
            {
                return e.getMessage();
            }
            finally
            {

            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                bw.close();
                socket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
