package com.example.laundrytimer;

import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.laundrytimer.Machine.Type;

public class MachineFragment extends Fragment {
	public static final String EXTRA_MACHINE_ID = "com.example.laundrytimer.machine_id"; 
	private SharedPreferences mPrefs;
	
	private Machine mMachine;
	private Button mNumPickerButton;
	private EditText mTitleField;
	private RadioButton mWasherButton;
	private RadioButton mDryerButton;
	private Button mStartButton;
	
	public static final int REQUEST_MACHINE_NUM = 5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		UUID machineID = (UUID)getArguments().getSerializable(EXTRA_MACHINE_ID);
		mMachine = MachineLab.get(getActivity()).getMachine(machineID);
		
		// Enables up-navigation button
		getActivity().getActionBar().setDisplayShowHomeEnabled(true);
	}
	
	// Inflate the fragments view and set up its components
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_machine, parent, false);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		// Sets up the machine number picker button
		mNumPickerButton = (Button)v.findViewById(R.id.machine_number_picker_button);
		mNumPickerButton.setText(""+ mMachine.getMachineNumber());		// Sets the text of the button to the current value
		mNumPickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int max_num = mPrefs.getInt(SettingsFragment.NUM_MACHINES,
										   SettingsFragment.DEFAULT_MACHINE_NUM);
				
				NumberDialogFragment dialog = NumberDialogFragment.newInstance(1, max_num, R.string.select_machine_num_dialog_message, null);
	    		dialog.show(getFragmentManager(), null);
	    		dialog.setTargetFragment(MachineFragment.this, REQUEST_MACHINE_NUM);
			}
		});
		
		// Configures the title field to change the title of this machine
		mTitleField = (EditText)v.findViewById(R.id.machine_title_field);
		mTitleField.setText(mMachine.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before, int count) {
				mMachine.setTitle(c.toString());
				if (mMachine.getTitle().equals("")) {
					mMachine.setCustomTitle(true);
				} else {
					mMachine.setCustomTitle(false);
				}
			}
			
			// Blank but necessary methods
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
			public void afterTextChanged(Editable c) {}
		});
		
		// Sets up the radio buttons
		mWasherButton = (RadioButton)v.findViewById(R.id.radio_washer);
		mWasherButton.setChecked(mMachine.getType() == Type.WASHER);
		mDryerButton = (RadioButton)v.findViewById(R.id.radio_dryer);
		mDryerButton.setChecked(mMachine.getType() == Type.DRYER);
		mWasherButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mMachine.setType(Type.WASHER);
				updateComponents();
			}
		});
		mDryerButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mMachine.setType(Type.DRYER);
				updateComponents();
			}
		});
		
		// Sets up the start button. The button is disabled initially, and is later enabled
		// when one of the radio buttons is checked.
		mStartButton = (Button)v.findViewById(R.id.start_button);
		mStartButton.setEnabled(mMachine.getType() != Type.MACHINE);
		mStartButton.setOnClickListener(new OnClickListener() {
			// Sets the timer value (in seconds) for the machine
			@Override
			public void onClick(View v) {
				if (mMachine.getType() == Type.WASHER) {
					mMachine.setTimerValue(mPrefs.getInt(SettingsFragment.WASHER_TIME,
										   SettingsFragment.DEFAULT_TIME) * 60);
				} else if (mMachine.getType() == Type.DRYER) {
					mMachine.setTimerValue(mPrefs.getInt(SettingsFragment.DRYER_TIME,
							   SettingsFragment.DEFAULT_TIME) * 60);
				}
				
				// Set the end time
				mMachine.setEndTime(mMachine.getTimerValue() * 1000 + System.currentTimeMillis());
				
				// Return to the list fragment
				if (NavUtils.getParentActivityName(getActivity()) != null)	
					NavUtils.navigateUpFromSameTask(getActivity());
			}
		});
		
		return v;
	}
	
	// EFFECTS: Creates and returns a new instance of MachineFragment
	//			containing the UUID of the shown crime as an argument.
	public static MachineFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_MACHINE_ID, crimeId);
		
		MachineFragment fragment = new MachineFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	// REQUIRES: mMachine is of type WASHER or DRYER, else nothing happens
	// MODIFIES: mTitleField text
	// EFFECTS:  If mTitleField is blank, a simple title is generated based on the user's selections
	private void updateComponents() {
		if (mMachine.getType() == Type.WASHER) {
			if (!mMachine.isCustomTitle())
				mTitleField.setText("Washer " + mMachine.getMachineNumber());
		} else if (mMachine.getType() == Type.DRYER) {
			if (!mMachine.isCustomTitle())
				mTitleField.setText("Dryer " + mMachine.getMachineNumber());
		}
		mStartButton.setEnabled(true);
	}
	
	// Sets the text of mNumPickerButton when the user changes the value
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		int num = (Integer) data.getSerializableExtra(NumberDialogFragment.EXTRA_MACHINE_NUM);
		mMachine.setMachineNumber(num);
		mNumPickerButton.setText("" + num);
		updateComponents();
	}
	
	// Displays the icons on the action bar
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.context_machine_list, menu);
	}
	
	// Sets up actions for icons on the action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null)		// Navigate up
					NavUtils.navigateUpFromSameTask(getActivity());
				return true;
			case R.id.menu_item_delete_machine:
				MachineLab.get(getActivity()).deleteMachine(mMachine);			
				if (NavUtils.getParentActivityName(getActivity()) != null)	
					NavUtils.navigateUpFromSameTask(getActivity());			
				return true;	
			default:
				return super.onOptionsItemSelected(item);
		}
	}	
}
