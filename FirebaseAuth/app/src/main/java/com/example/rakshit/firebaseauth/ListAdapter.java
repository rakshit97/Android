package com.example.rakshit.firebaseauth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Object>
{
    private ArrayList<Object> list = new ArrayList<>();
    private Context _context;

    public ListAdapter(Context context, ArrayList<Object> list)
    {
        super(context, 0, list);
        this.list = list;
        this._context = context;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        POJOExpense curr = (POJOExpense) list.get(position);
        if (convertView==null)
                convertView = LayoutInflater.from(_context).inflate(R.layout.list_item, parent, false);

        ((TextView)convertView.findViewById(R.id.list_name)).setText(curr.getName());
        ((TextView)convertView.findViewById(R.id.list_category)).setText(String.valueOf(curr.getCategory()));
        ((TextView)convertView.findViewById(R.id.list_payment)).setText(String.valueOf(curr.getPayment()));
        ((TextView)convertView.findViewById(R.id.list_cost)).setText(String.valueOf(curr.getCost()));
        ((TextView)convertView.findViewById(R.id.list_timestamp)).setText(String.valueOf(curr.getTimestamp()));
        return convertView;
    }
}
