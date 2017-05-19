package com.example.rakshit.spiderginternship;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.autoplay_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
        boolean currentvalue = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getBoolean(preference.getKey(), true);
        onPreferenceChange(preference, currentvalue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o)
    {
        ((CheckBoxPreference) preference).setChecked((boolean)o);

        return true;
    }

    @Override
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
    }
}
