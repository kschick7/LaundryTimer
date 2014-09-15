package com.example.laundrytimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class MachineListFragment extends ListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	// Opens a window to add a new machine to the list
	public void addMachine() {
		startActivity(new Intent(getActivity(), MachineActivity.class));
		// TO-DO
		// Finish
	}
	
	// Inflates the list view. If the list is empty, the view displays a message and button prompting
	// the user to add a machine.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_view, parent, false);
		
		// Sets the list id to a variable and sets the empty list view
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		listView.setEmptyView(v.findViewById(android.R.id.empty));
		
		Button addMachine = (Button)v.findViewById(R.id.addMachineButton);
		addMachine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addMachine();
			}
		});
		
		return v;
	}

	// Displays the icons on the action bar
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.machine_list, menu);
	}
	
	// Sets up actions for icons on the action bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_settings:
				startActivity(new Intent(getActivity(), SettingsActivity.class));
				return true;
			case R.id.menu_item_add_machine:
				addMachine();
				return true;
			default:
				return super.onOptionsItemSelected(item);	
		}
	}
	
}
