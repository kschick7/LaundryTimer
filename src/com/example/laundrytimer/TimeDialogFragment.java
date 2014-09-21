package com.example.laundrytimer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.NumberPicker;

public class TimeDialogFragment extends DialogFragment{
	private NumberPicker mNumberPicker;
	
	// List of keys for arguments
	private static final String MIN_VALUE = "min";
	private static final String MAX_VALUE = "max";
	private static final String MESSAGE = "message";
	private static final String KEY = "key";

	// REQUIRES: min and max must be positive integers, message refers to a String
	// EFFECTS:  Adds two inputs (the minimum and max values of the number picker) as 
	//			 arguments in a new instance of the fragment
	public static TimeDialogFragment newInstance(int min, int max, int message, String key) {
		assert(min >= 0 && max >= 0);
		
		TimeDialogFragment f = new TimeDialogFragment();
		Bundle args = new Bundle();
		args.putInt(MIN_VALUE, min);
		args.putInt(MAX_VALUE, max);
		args.putInt(MESSAGE, message);
		args.putString(KEY, key);
		f.setArguments(args);
		return f;
	}
	
	// EFFECTS: Notifies the target fragment that the user has updated the number picker value
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, new Intent());
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Inflate view to set range of the number picker
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		// Sets the value range depending on the given arguments
		mNumberPicker = (NumberPicker)v.findViewById(R.id.dialog_time_number_picker);
		mNumberPicker.setMinValue(getArguments().getInt(MIN_VALUE));
		mNumberPicker.setMaxValue(getArguments().getInt(MAX_VALUE));
		
		// Sets the initial value of the number picker to the value saved in preferences or the default value
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mNumberPicker.setValue(prefs.getInt(getArguments().getString(KEY), getArguments().getInt(MAX_VALUE) / 2));
		
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
        	   .setMessage(getArguments().getInt(MESSAGE))
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            	   // Saves the value chosen in SharedPreferences
                   public void onClick(DialogInterface dialog, int id) {
                	   Editor editor = prefs.edit();
                	   editor.putInt(getArguments().getString(KEY), mNumberPicker.getValue());
                	   editor.commit();
                	   sendResult(Activity.RESULT_OK);
                   }
               });        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
