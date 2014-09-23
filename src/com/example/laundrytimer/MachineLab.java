package com.example.laundrytimer;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;


public class MachineLab {
	private static MachineLab sMachineLab;
	private Context mAppContext;
	private ArrayList<Machine> mMachines;
	private JSONSerializer mSerializer;
	
	private static final String TAG = "MachineLab";
	private static final String FILENAME = "machines.json";
	
	private MachineLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new JSONSerializer(mAppContext, FILENAME);
		
		try {
			mMachines = mSerializer.loadMachines();
			Log.d(TAG, "Loading successful: " + mMachines.size());
		} catch (Exception e) {
			mMachines = new ArrayList<Machine>();
			Log.e(TAG, "Error loading machines: ", e);
		}
		
	}
	
	public static MachineLab get(Context c) {
		if (sMachineLab == null) 
			sMachineLab = new MachineLab(c.getApplicationContext());
		return sMachineLab;
	}
	
	public ArrayList<Machine> getMachines() {
		return mMachines;
	}
	
	public Machine getMachine(UUID id) {
		for (Machine m : mMachines) {
			if (m.getID().equals(id))
				return m;
		}
		return null;
	}
	
	public void addMachine(Machine m) {
		mMachines.add(m);
	}
	
	public void deleteMachine(Machine m) {
		mMachines.remove(m);
	}
	
	
	public boolean saveMachines() {
		try {
			mSerializer.saveMachines(mMachines);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch(Exception e) {
			Log.e(TAG, "Error saving crimes: ", e);
			return false;
		}
	}
	  
	 
}
