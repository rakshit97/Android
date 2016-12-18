package com.example.rakshit.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class PreferencesFrag extends PreferenceFragment implements Preference.OnPreferenceChangeListener
    {
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference minMagnitude = findPreference(getString(R.string.min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);
            Preference orderBy = findPreference(getString(R.string.order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String value_string = value.toString();
            preference.setSummary(value_string);
            if (preference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(value_string);
                if (prefIndex >= 0)
                {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
            else
            {
                preference.setSummary(value_string);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
