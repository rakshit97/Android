package com.example.rakshit.sunshine;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.units_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
        String preferencesString = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "");
        onPreferenceChange(preference, preferencesString);
    }

    public boolean onPreferenceChange(Preference preference, Object value)
    {
        String valueString = value.toString();
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(valueString);
            if(prefIndex >= 0)
                preference.setSummary(listPreference.getEntries()[prefIndex]);
        }
        else
            preference.setSummary(valueString);

        return true;
    }

    @Override
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
    }
}
