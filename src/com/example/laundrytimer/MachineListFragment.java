package com.example.laundrytimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MachineListFragment extends ListFragment {
	private ArrayList<Machine> mMachines;
	private MachineAdapter mAdapter;
	private Handler mTimerHandler;
	private Runnable mTimerRunnable;
	
	private class MachineAdapter extends ArrayAdapter<Machine> {
		TextView mCountdownText;
		Machine m;
		
		public MachineAdapter(ArrayList<Machine> machines) {
			super(getActivity(), 0, machines);
		}
		
		// Configure the view of each list item
		@SuppressLint("SimpleDateFormat")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// If we weren't given a view, inflate one.
			if (convertView == null)
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
			
			// Configure the view for this Machine
			m = getItem(position);
			TextView titleText = (TextView)convertView.findViewById(R.id.list_item_titleTextView);
			titleText.setText(m.getTitle());
			mCountdownText = (TextView)convertView.findViewById(R.id.list_item_countdownView);
			
			if (m.getTimerValue() == -1) {
				mCountdownText.setText("Not Started");
			} else {
				// Set the count-down text view to the current time remaining
				long outputTime = m.getEndTime() - System.currentTimeMillis();
				if (outputTime > 0) {
					Date date = new java.util.Date(outputTime);
					String result = new SimpleDateFormat("mm:ss").format(date);
					mCountdownText.setText(result);
				} else {
					mCountdownText.setText("Done!");
				}
			}
			
			return convertView;
		}	
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		mMachines = MachineLab.get(getActivity()).getMachines();
		
		mAdapter = new MachineAdapter(mMachines);
		setListAdapter(mAdapter);
		setRetainInstance(true);
		
		// Initializes a handler and runnable which update the adapter every second,
		// which allows the count-down to work
		mTimerHandler = new Handler();
		mTimerRunnable = new Runnable() {
		    @Override
		    public void run() {
		        mAdapter.notifyDataSetChanged();
		        mTimerHandler.postDelayed(this, 1000); //run every minute
		    }
		};
		mTimerHandler.postDelayed(mTimerRunnable, 500);

		/** UNCOMMENT OUT TO CLEAR PREFERENCES DATA
		PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().commit();
		*/
	}
	
	@Override
	public void onResume() {
		mTimerHandler.postDelayed(mTimerRunnable, 500);
		super.onResume();
		//((MachineAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		mTimerHandler.removeCallbacks(mTimerRunnable);
	    super.onPause();
	}
	
	// Opens a window to add a new machine to the list
	public void addMachine() {
		//startActivity(new Intent(getActivity(), MachineActivity.class));
		Machine m = new Machine();
		MachineLab.get(getActivity()).addMachine(m);
		Intent i = new Intent(getActivity(), MachineActivity.class);
		i.putExtra(MachineFragment.EXTRA_MACHINE_ID, m.getID());
		startActivityForResult(i, 0);
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Machine m = ((MachineAdapter)getListAdapter()).getItem(position);

		// Start CrimePagerActivity with this crime
		Intent i = new Intent(getActivity(), MachineActivity.class);
		i.putExtra(MachineFragment.EXTRA_MACHINE_ID, m.getID());
		startActivity(i);
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
