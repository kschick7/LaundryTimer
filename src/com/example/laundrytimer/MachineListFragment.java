package com.example.laundrytimer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
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
			if (titleText.getText().equals(""))
				titleText.setText("(No Title)");
			
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
		// Resumes the count-down
		mTimerHandler.postDelayed(mTimerRunnable, 500);
		super.onResume();
		// Get rid of delay for updating the list upon adding a new machine
		((MachineAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		// Prevents the count-down from using resources while the fragment is not active
		mTimerHandler.removeCallbacks(mTimerRunnable);
	    super.onPause();
	}
	
	// Opens a window to add a new machine to the list
	public void addMachine() {
		Machine m = new Machine();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		m.setPlayAlarm(prefs.getBoolean("play_alarm_by_default_key", false));
		m.setVibrate(prefs.getBoolean("vibrate_by_default_key", false));
		m.setNotification(prefs.getBoolean("notification_by_default_key", false));
		
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
		
		// registerForContextMenu(listView);		USED FOR CONTEXT MENUS, COME BACK WHEN LOWERING VERSION REQS
		
		// Enable context menus
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				// Required, but not used.
			}
			
			// ActionMode.Callback methods
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.context_machine_list, menu);
				return true;
			}
			
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
				// Required, but not used
			}
		
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_item_delete_machine:
						MachineAdapter adapter = (MachineAdapter)getListAdapter();
						MachineLab machineLab = MachineLab.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i))
								machineLab.deleteMachine(adapter.getItem(i));
						}
						mode.finish();
						MachineLab.get(getActivity()).saveMachines();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
				}
			}
			
			public void onDestroyActionMode(ActionMode mode) {
				// Required, but not used
			}
		});
		
		return v;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Machine m = ((MachineAdapter)getListAdapter()).getItem(position);

		// Start MachineActivity with this machine
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
	
	/*			USED FOR CONTEXT MENUS, COME BACK WHEN LOWERING VERSION REQS
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.context_machine_list, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		MachineAdapter adapter = (MachineAdapter)getListAdapter();
		Machine machine = adapter.getItem(position);
		
		switch(item.getItemId()) {
			case R.id.menu_item_delete_machine:
				MachineLab.get(getActivity()).deleteMachine(machine);
				adapter.notifyDataSetChanged();
				return true;
		}
		
		return super.onContextItemSelected(item);
	}
	*/
}
