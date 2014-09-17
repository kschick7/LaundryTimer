package com.example.laundrytimer;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SettingsFragment extends PreferenceFragment {
	private static final int MIN_TIME_INPUT_VALUE = 20;
	private static final int MAX_TIME_INPUT_VALUE = 90;
	
    // REQUIRES: min and max must be positive integers, message refers to a String
	// EFFECTS:  Creates an onClick listener to set the value ranges and message for a number picker.
    private OnPreferenceClickListener getNumberPickerListener(final int min, final int max,
    														  final int message, final String label) {
        return new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				TimeDialogFragment dialog = TimeDialogFragment.newInstance(min, max, message);
	    		dialog.show(getFragmentManager(), label);
				return true;
			}       	
        };
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        setHasOptionsMenu(true);
        
        // Enables up-navigation button
        getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        
        // Assign onClick listeners to certain preferences
        Preference washerTimePref = findPreference("washer_default_time_key");
        Preference dryerTimePref = findPreference("dryer_default_time_key");
        Preference numMachinesPref = findPreference("number_machines_key");
        washerTimePref.setOnPreferenceClickListener(getNumberPickerListener(MIN_TIME_INPUT_VALUE,
				  															MAX_TIME_INPUT_VALUE,
				  															R.string.time_dialog_message,
				   															"washer_time"));
        dryerTimePref.setOnPreferenceClickListener(getNumberPickerListener(MIN_TIME_INPUT_VALUE,
        																   MAX_TIME_INPUT_VALUE,
        																   R.string.time_dialog_message,
        																   "dryer_time"));
        numMachinesPref.setOnPreferenceClickListener(getNumberPickerListener(1,
        																	 100,
        																	 R.string.num_machines_dialog_message,
        																	 "num_machines"));
        
    }
    
	// Sets up actions for icons on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null)
					NavUtils.navigateUpFromSameTask(getActivity());
				return true;
			default:
				return super.onOptionsItemSelected(item);
    	}
    }

    
}
