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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;

public class MachineFragment extends Fragment {
	public static final String EXTRA_MACHINE_ID = "com.example.laundrytimer.machine_id"; 
	private SharedPreferences mPrefs;
	
	private Machine mMachine;
	private Button mNumPickerButton;
	private EditText mTitleField;
	private RadioButton mWasherButton;
	private RadioButton mDryerButton;
	private Button mStartButton;
	private Button mTimeButton;
	private CheckBox mAlarmCheckbox;
	private CheckBox mVibrateCheckbox;
	private CheckBox mNotificationCheckbox;
	
	
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
				if (mMachine.getTitle().equals("") 
					|| mMachine.getTitle().equals("Washer " + mMachine.getMachineNumber()) 
					|| mMachine.getTitle().equals("Dryer " + mMachine.getMachineNumber())) {
					mMachine.setCustomTitle(false);
				} else {
					mMachine.setCustomTitle(true);
				}
			}
			
			// Blank but necessary methods
			public void beforeTextChanged(CharSequence c, int start, int count, int after) {}
			public void afterTextChanged(Editable c) {}
		});
		
		// Sets up the radio buttons
		mWasherButton = (RadioButton)v.findViewById(R.id.radio_washer);
		mWasherButton.setChecked(mMachine.getType() == Machine.WASHER);
		mDryerButton = (RadioButton)v.findViewById(R.id.radio_dryer);
		mDryerButton.setChecked(mMachine.getType() == Machine.DRYER);
		mWasherButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mMachine.setType(Machine.WASHER);
				updateComponents();
			}
		});
		mDryerButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mMachine.setType(Machine.DRYER);
				updateComponents();
			}
		});
		
		// Set the default values for the machine time and alarm settings based on preferences
		mTimeButton = (Button)v.findViewById(R.id.time_button);		// Text changed in updateComponents method
		mAlarmCheckbox = (CheckBox)v.findViewById(R.id.alarm_checkbox);
		mAlarmCheckbox.setChecked(mMachine.isPlayAlarm());
		mAlarmCheckbox.setOnCheckedChangeListener(onCheckboxClicked(mAlarmCheckbox));
		mVibrateCheckbox = (CheckBox)v.findViewById(R.id.vibrate_checkbox);
		mVibrateCheckbox.setChecked(mMachine.isVibrate());
		mVibrateCheckbox.setOnCheckedChangeListener(onCheckboxClicked(mVibrateCheckbox));
		mNotificationCheckbox = (CheckBox)v.findViewById(R.id.notification_checkbox);
		mNotificationCheckbox.setChecked(mMachine.isNotification());
		mNotificationCheckbox.setOnCheckedChangeListener(onCheckboxClicked(mNotificationCheckbox));
		
		// Sets up the start button. The button is disabled initially, and is later enabled
		// when one of the radio buttons is checked.
		mStartButton = (Button)v.findViewById(R.id.start_button);
		mStartButton.setOnClickListener(new OnClickListener() {
			// Sets the timer value (in seconds) for the machine
			@Override
			public void onClick(View v) {
				if (mMachine.getType() == Machine.WASHER) {
					mMachine.setTimerValue(mPrefs.getInt(SettingsFragment.WASHER_TIME,
										   SettingsFragment.DEFAULT_TIME) * 60);
				} else if (mMachine.getType() == Machine.DRYER) {
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
		
		updateComponents();
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
		if (mMachine.getType() == Machine.WASHER) {
			mTimeButton.setText(mPrefs.getInt(SettingsFragment.WASHER_TIME, SettingsFragment.DEFAULT_TIME) + "minutes");
			if (!mMachine.isCustomTitle())
				mTitleField.setText("Washer " + mMachine.getMachineNumber());
		} else if (mMachine.getType() == Machine.DRYER) {
			mTimeButton.setText(mPrefs.getInt(SettingsFragment.DRYER_TIME, SettingsFragment.DEFAULT_TIME) + " minutes");
			if (!mMachine.isCustomTitle())
				mTitleField.setText("Dryer " + mMachine.getMachineNumber());
		}
		mStartButton.setEnabled(mMachine.getType() != Machine.MACHINE);
		mTimeButton.setEnabled(mMachine.getType() != Machine.MACHINE);
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
	
	// Saves the current list of machines to JSON when the fragment is paused (or destroyed)
	@Override
	public void onPause() {
		super.onPause();
		MachineLab.get(getActivity()).saveMachines();
	}
	
	// Returns a OnCheckedChangeListener to be used by the alarm, vibrate, and notification
	// checkboxes.
	private OnCheckedChangeListener onCheckboxClicked(final View view) {
		return new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				boolean checked = ((CheckBox)view).isChecked();
				
				switch(view.getId()) {
					case R.id.alarm_checkbox:
						mMachine.setPlayAlarm(checked);
						break;
					case R.id.vibrate_checkbox:
						mMachine.setVibrate(checked);
						break;
					case R.id.notification_checkbox:
						mMachine.setNotification(checked);
						break;
				}	
			}
		};
	}
}
