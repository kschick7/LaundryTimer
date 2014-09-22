// Generic Machine object. Can be used to create a Washer or Dryer

package com.example.laundrytimer;

import java.util.UUID;

public class Machine {

	private UUID mID;
	private String mTitle;			// Name of the machine
	private int mMachineNumber;
	private Type mType;
	private boolean mCustomTitle;	// Whether or not the user changed the title
	private int mTimerValue;		// Seconds left on the timer, -1 if not started
	private long mEndTime;			// System time (in millis) when the countdown will end

	// Type of Machine
	public enum Type {
		MACHINE, WASHER, DRYER
	}
	
	public Machine() {
		mID = UUID.randomUUID();
		mTitle = "";
		mMachineNumber = 1;
		mType = Type.MACHINE;
		mCustomTitle = false;
		mTimerValue = -1;
		mEndTime = -1;
	}

	public int getTimerValue() {
		return mTimerValue;
	}

	public void setTimerValue(int time) {
		mTimerValue = time;
	}

	public boolean isCustomTitle() {
		return mCustomTitle;
	}

	public void setCustomTitle(boolean customTitle) {
		mCustomTitle = customTitle;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public int getMachineNumber() {
		return mMachineNumber;
	}

	public void setMachineNumber(int machineNumber) {
		mMachineNumber = machineNumber;
	}

	public UUID getID() {
		return mID;
	}

	public Type getType() {
		return mType;
	}

	public void setType(Type type) {
		mType = type;
	}
	
	public long getEndTime() {
		return mEndTime;
	}

	public void setEndTime(long endTime) {
		this.mEndTime = endTime;
	}
	
}
