package com.example.laundrytimer;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MachineFragment extends Fragment {
	public static final String EXTRA_MACHINE_ID = "com.example.laundrytimer.machine_id"; 
	private Machine mMachine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		UUID machineID = (UUID)getArguments().getSerializable(EXTRA_MACHINE_ID);
		mMachine = MachineLab.get(getActivity()).getMachine(machineID);
		
		// Enables up-navigation button
		getActivity().getActionBar().setDisplayShowHomeEnabled(true);
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_machine, parent, false);
		
		// Sets up the spinner
		Spinner spinner = (Spinner)v.findViewById(R.id.machine_number_spinner);
		String[] testArray = new String[35];
		for (int i = 1; i <= 35; i++) {
			testArray[i-1] = Integer.toString(i);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, testArray);
		spinner.setAdapter(adapter);
		
		
		
		return v;
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
