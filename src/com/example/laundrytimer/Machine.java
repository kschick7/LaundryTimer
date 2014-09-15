// Generic Machine object. Can be used to create a Washer or Dryer

package com.example.laundrytimer;

import java.util.UUID;

public class Machine {

	private UUID mID;
	private String mTitle;			// Name of the machine
	private int mMachineNumber;
	private Type mType;
	
	// Type of Machine
	public enum Type {
		WASHER, DRYER
	}
	
	public Machine() {
		mID = UUID.randomUUID();
		mTitle = "New Machine";
		mMachineNumber = 0;
		mType = Type.WASHER;
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
	
}
