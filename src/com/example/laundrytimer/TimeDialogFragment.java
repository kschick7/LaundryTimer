package com.example.laundrytimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class TimeDialogFragment extends DialogFragment{
	private NumberPicker mNumberPicker;
	private static final String MIN_VALUE = "min";
	private static final String MAX_VALUE = "max";

	// REQUIRES: min and max must be positive integers, message refers to a String
	// EFFECTS:  Adds two inputs (the minimum and max values of the number picker) as 
	//			 arguments in a new instance of the fragment
	public static TimeDialogFragment newInstance(int min, int max, int message) {
		assert(min >= 0 && max >= 0);
		
		TimeDialogFragment f = new TimeDialogFragment();
		Bundle args = new Bundle();
		args.putInt("min", min);
		args.putInt("max", max);
		args.putInt("message", message);
		f.setArguments(args);
		return f;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Inflate view to set range of the number picker
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		mNumberPicker = (NumberPicker)v.findViewById(R.id.dialog_time_number_picker);
		mNumberPicker.setMinValue(getArguments().getInt(MIN_VALUE));
		mNumberPicker.setMaxValue(getArguments().getInt(MAX_VALUE));
		
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
        	   .setMessage(getArguments().getInt("message"))
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // OK
                   }
               });        
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	public int getValue() {
		return mNumberPicker.getValue();
	}
}
