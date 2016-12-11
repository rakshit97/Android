package com.example.rakshit.miwok;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsListAdapter extends ArrayAdapter<WordsList>
{

    public WordsListAdapter(Activity context, ArrayList<WordsList> words)
    {
        super(context, 0, words);
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

        WordsList currentWord = getItem(position);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok);
        miwokTextView.setText(currentWord.getmiwok());

        TextView engTextView = (TextView) listItemView.findViewById((R.id.eng));
        engTextView.setText(currentWord.geteng());

        return listItemView;
    }
}
