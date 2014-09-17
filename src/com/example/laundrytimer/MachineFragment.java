package com.example.laundrytimer;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayShowHomeEnabled(true);		// Enables up-navigation button
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
}
