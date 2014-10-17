// Generic Machine object. Can be used to create a Washer or Dryer

package com.example.laundrytimer;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Machine {

	private UUID mID;
	private String mTitle;			// Name of the machine
	private int mMachineNumber;
	private int mType;
	private boolean mCustomTitle;	// Whether or not the user changed the title
	private boolean mPlayAlarm;
	private boolean mVibrate;
	private boolean mNotification;
	private int mTimerValue;		// Seconds left on the timer, -1 if not started
	private long mEndTime;			// System time (in millis) when the countdown will end
	
	// JSON Keys
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_MACHINENUMBER = "machinenumber";
	private static final String JSON_TYPE = "type";
	private static final String JSON_CUSTOM = "custom";
	private static final String JSON_TIMER = "timer";
	private static final String JSON_ALARM = "alarm";
	private static final String JSON_VIBRATE = "vibrate";
	private static final String JSON_NOTIFICATION = "notification";
	private static final String JSON_ENDTIME = "endtime";

	// Types of Machine
	public static final int MACHINE = 0;
	public static final int WASHER = 1;
	public static final int DRYER = 2;
	
	public Machine() {
		mID = UUID.randomUUID();
		mTitle = "";
		mMachineNumber = 1;
		mType = MACHINE;
		mCustomTitle = false;
		mPlayAlarm = false;
		mVibrate = false;
		mNotification = false;
		mTimerValue = -1;
		mEndTime = -1;
	}
	
	public Machine(JSONObject json) throws JSONException{
		mID = UUID.fromString(json.getString(JSON_ID));
		mTitle = json.getString(JSON_TITLE);
		mMachineNumber = json.getInt(JSON_MACHINENUMBER);
		mType = json.getInt(JSON_TYPE);
		mCustomTitle = json.getBoolean(JSON_CUSTOM);
		mPlayAlarm = json.getBoolean(JSON_ALARM);
		mVibrate = json.getBoolean(JSON_VIBRATE);
		mNotification = json.getBoolean(JSON_NOTIFICATION);
		mTimerValue = json.getInt(JSON_TIMER);
		mEndTime = json.getLong(JSON_ENDTIME);
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

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}
	
	public long getEndTime() {
		return mEndTime;
	}

	public void setEndTime(long endTime) {
		this.mEndTime = endTime;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mID.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_MACHINENUMBER, mMachineNumber);
		json.put(JSON_TYPE, mType);
		json.put(JSON_CUSTOM, mCustomTitle);
		json.put(JSON_ALARM, mPlayAlarm);
		json.put(JSON_VIBRATE, mVibrate);
		json.put(JSON_NOTIFICATION, mNotification);
		json.put(JSON_TIMER, mTimerValue);
		json.put(JSON_ENDTIME, mEndTime);
		return json;
	}

	public boolean isPlayAlarm() {
		return mPlayAlarm;
	}

	public void setPlayAlarm(boolean alarm) {
		mPlayAlarm = alarm;
	}

	public boolean isVibrate() {
		return mVibrate;
	}

	public void setVibrate(boolean vibrate) {
		mVibrate = vibrate;
	}

	public boolean isNotification() {
		return mNotification;
	}

	public void setNotification(boolean notification) {
		mNotification = notification;
	}
	
}
