package com.example.rakshit.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class myFragmentPagerAdapter extends FragmentPagerAdapter
{
    private Context context;

    public myFragmentPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position)
    {
        if(position==0)
            return new NumbersFragment();
        else if(position==1)
            return new FamilyFragment();
        else if(position==2)
            return new ColorsFragment();
        else
            return new PhrasesFragment();
    }

    @Override
    public int getCount()
    {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        if(position==0)
            return context.getString(R.string.category_numbers);
        else if(position==1)
            return context.getString(R.string.category_family);
        else if(position==2)
            return context.getString(R.string.category_colors);
        else
            return context.getString(R.string.category_phrases);
    }
}
