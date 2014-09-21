package com.example.laundrytimer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SettingsFragment extends PreferenceFragment {
	private static final int MIN_TIME_INPUT_VALUE = 20;
	private static final int MAX_TIME_INPUT_VALUE = 90;
	private static final int DEFAULT_TIME = 45;
	private static final int MIN_MACHINE_NUM_INPUT_VALUE = 1;
	private static final int MAX_MACHINE_NUM_INPUT_VALUE = 100;
	private static final int DEFAULT_MACHINE_NUM = 40;

	private static final int REQUEST_NUM = 0;
	
	// List of keys
	private static final String WASHER_TIME = "washer_default_time_key";
	private static final String DRYER_TIME = "dryer_default_time_key";
	private static final String NUM_MACHINES = "num_machines_key";
	
	// List of preference objects
	private SharedPreferences mPrefs;
	private Preference mWasherTimePref;
	private Preference mDryerTimePref;
	private Preference mNumMachinesPref;
	
    // REQUIRES: min and max must be positive integers, message refers to a String
	// EFFECTS:  Creates an onClick listener to set the value ranges and message for a number picker.
    private OnPreferenceClickListener getNumberPickerListener(final int min, final int max, final String key,
    														  final int message) {
        return new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				TimeDialogFragment dialog = TimeDialogFragment.newInstance(min, max, message, key);
	    		dialog.show(getFragmentManager(), key);
	    		dialog.setTargetFragment(SettingsFragment.this, REQUEST_NUM);
				return true;
			}       	
        };
    }
    
    // MODIFIES: mWasherTimePref, mDryerTimePref, mNumMachinesPref summaries
    // EFFECTS:  Updates the summaries of the number picker preferences to either the default value (if
    //			 a value hasn't been set) or the chosen value
    private void setSummaries() {
    	mWasherTimePref.setSummary(Integer.toString(mPrefs.getInt(WASHER_TIME, DEFAULT_TIME)) + " minutes");
        mDryerTimePref.setSummary(Integer.toString(mPrefs.getInt(DRYER_TIME, DEFAULT_TIME)) + " minutes");
        mNumMachinesPref.setSummary(Integer.toString(mPrefs.getInt(NUM_MACHINES, DEFAULT_MACHINE_NUM)) + " machines");
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        setHasOptionsMenu(true);
        
        // Enables up-navigation button
        getActivity().getActionBar().setDisplayShowHomeEnabled(true);
        
        // Initialize preference objects
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mWasherTimePref = findPreference(WASHER_TIME);
        mDryerTimePref = findPreference(DRYER_TIME);
        mNumMachinesPref = findPreference(NUM_MACHINES);
        
        // Assign onClick listeners for preferences using number pickers
        mWasherTimePref.setOnPreferenceClickListener(getNumberPickerListener(MIN_TIME_INPUT_VALUE,
				  															MAX_TIME_INPUT_VALUE,
				  															WASHER_TIME,
				  															R.string.time_dialog_message));
        mDryerTimePref.setOnPreferenceClickListener(getNumberPickerListener(MIN_TIME_INPUT_VALUE,
        																   MAX_TIME_INPUT_VALUE,
        																   DRYER_TIME,
        																   R.string.time_dialog_message));
        mNumMachinesPref.setOnPreferenceClickListener(getNumberPickerListener(MIN_MACHINE_NUM_INPUT_VALUE,
        																	 MAX_MACHINE_NUM_INPUT_VALUE,
        																	 NUM_MACHINES,
        																	 R.string.num_machines_dialog_message));
        
        // Update the number picker preference summaries
        setSummaries();
        
    }
    
	// Sets up actions for icons on the action bar, enable
    // up navigation
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
    
    // Called when number picker dialogs are closed. Updates the summaries of those preferences to show
    // the updated values.
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		setSummaries();
	}
	
    
}
