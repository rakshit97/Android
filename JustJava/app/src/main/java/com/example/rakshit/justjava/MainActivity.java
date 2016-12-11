package com.example.rakshit.justjava;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int n = 0;
    int price = 0;
    int count = 0;

    public void calc_price(int n)
    {
        price=n*50;
        CheckBox c1 = (CheckBox)findViewById(R.id.cream);
        CheckBox c2 = (CheckBox)findViewById(R.id.choco);
        if(c1.isChecked() && c2.isChecked())
            price+=(n*20)+(n*30);
        else if(c1.isChecked() && !c2.isChecked())
            price+=n*20;
        else if(!c1.isChecked() && c2.isChecked())
            price+=n*30;
    }

    public void incr(View view)
    {
        if(n==99)
        {
            Toast toast = makeText(this, "You can't order more than this! Sorry!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        count=0;
        n++;
        displayQty(n);
        calc_price(n);
        displayPrice(price);
    }

    public void decr(View view)
    {
        if(n==1)
        {
            Toast toast = Toast.makeText(this, "You need to order at least one coffee", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        count=0;
        n--;
        displayQty(n);
        calc_price(n);
        displayPrice(price);
    }

    public void toppings(View view)
    {
        count=0;
        calc_price(n);
        displayPrice(price);
    }

    public void submitOrder(View view)
    {
        count++;
        displaySummary(n);
    }

    public void displayQty(int n)
    {
        TextView qty_tv = (TextView) findViewById(R.id.qty_tv);
        String d = new Integer(n).toString();
        qty_tv.setText(d);
    }

    public void displayPrice(int n)
    {
        TextView price = (TextView) findViewById(R.id.price);
        price.setText(R.string.price_label);

        TextView price_tv = (TextView) findViewById(R.id.price_tv);
        String p = NumberFormat.getCurrencyInstance().format(n);
        String d = getString(R.string.total) + " " + p;
        price_tv.setText(d);

        Button order = (Button) findViewById(R.id.order_button);
        order.setText(R.string.order);
    }

    public void displaySummary(int n)
    {
        TextView pric = (TextView) findViewById(R.id.price);

        TextView price_tv = (TextView) findViewById(R.id.price_tv);
        EditText nm = (EditText) findViewById(R.id.name_input);
        String p = NumberFormat.getCurrencyInstance().format(price);
        String d = getString(R.string.name) + ": " + nm.getText().toString() + "\n"
                + getString(R.string.qty_label) + ": " + n + "\n" + getString(R.string.price)
                + ": " + p + "\n" + getString(R.string.thanks);

        Button order = (Button) findViewById(R.id.order_button);
        if(count>=2)
        {
            Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
            mail_intent.setData(Uri.parse("mailto:"));
            String[] to = new String[1];
            to[0]="rakshitthakkar97@gmail.com";
            mail_intent.putExtra(Intent.EXTRA_EMAIL, to);
            mail_intent.putExtra(Intent.EXTRA_SUBJECT, "Order for "+nm.getText().toString());
            mail_intent.putExtra(Intent.EXTRA_TEXT, d);
            if (mail_intent.resolveActivity(getPackageManager()) != null)
                startActivity(mail_intent);
        }
        else
        {
            pric.setText(R.string.summary_label);
            price_tv.setText(d);
            order.setText(R.string.confirm);
        }
    }
}
