package com.authDevs.sail;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by spiros on 8/2/13.
 */
public class SailApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = SailApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private boolean serviceRunning;
	private String MAC = "78:c4:e:1:fd:38";

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
}
