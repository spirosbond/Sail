package com.authDevs.sail;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by spiros on 8/2/13.
 */

public class PrefsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}
