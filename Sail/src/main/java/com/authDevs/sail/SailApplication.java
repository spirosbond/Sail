package com.authDevs.sail;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by spiros on 8/2/13.
 */
public class SailApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = SailApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private boolean serviceRunning;
	private String MAC = "78:c4:e:1:fd:38";
	private ArrayList<String> measurements = new ArrayList<String>();
	private Time now = new Time();

	public void onCreate() { //
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		Log.i(TAG, "onCreated");
	}

	public boolean isServiceRunning() { //
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) { //
		this.serviceRunning = serviceRunning;
	}

	@Override
	public void onTerminate() { //
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		this.MAC = this.prefs.getString("MAC", "");
	}

	public String getMAC() {
		this.MAC = this.prefs.getString("MAC", "");
		return MAC;
	}

	public boolean addMeasurement(String meas) {
		try {
			now.setToNow();
			measurements.add(meas + " received on: " + now.monthDay + "/" + now.month + " " + now.hour + ":" + now
					.minute + " " + now
					.second);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList getMeasurements() {
		return measurements;
	}

	public String getLastMeasurement() {
		if (!measurements.isEmpty()) return measurements.get(measurements.size() - 1);
		return null;
	}
}
