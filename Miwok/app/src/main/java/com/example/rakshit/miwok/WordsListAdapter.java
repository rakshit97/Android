package com.example.rakshit.miwok;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsListAdapter extends ArrayAdapter<WordsList>
{
    private int color;

    public WordsListAdapter(Activity context, ArrayList<WordsList> words, int color)
    {
        super(context, 0, words);
        this.color=color;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if(listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view, parent, false);
        }

        WordsList current = getItem(position);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok);
        miwokTextView.setText(current.getmiwok());

        TextView engTextView = (TextView) listItemView.findViewById(R.id.eng);
        engTextView.setText(current.geteng());

        ImageView imgView = (ImageView) listItemView.findViewById(R.id.img);
        if(current.hasImg())
            imgView.setImageResource(current.getimg_src());
        else
            imgView.setVisibility(View.GONE);

        LinearLayout tc = (LinearLayout)listItemView.findViewById(R.id.text_container);
        tc.setBackgroundColor(ContextCompat.getColor(getContext(), color));

        return listItemView;
    }
}
