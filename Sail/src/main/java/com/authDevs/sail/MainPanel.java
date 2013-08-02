package com.authDevs.sail;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainPanel extends Activity implements View.OnClickListener {

	private static final String TAG = "MainActivity";
	public static ProgressBar progressBar;
	Button startButton, stopButton;
	TextView textView;
	private SailApplication sailApplication = new SailApplication();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail_panel);

		textView = (TextView) findViewById(R.id.textView);
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(this);
		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(this);
		this.sailApplication = (SailApplication) getApplication();
		if (sailApplication.isServiceRunning()) {
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		} else {
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	public void onClick(View view) {
		Log.d(TAG, "onClicked");
		Button buttonPressed = (Button) view;

		if (buttonPressed.getId() == R.id.startButton) {

			textView.setText(null);
			progressBar = (ProgressBar) findViewById(R.id.progressBar);

			textView.setText("Receiving measurements...\n");

			//Toast.makeText(getApplicationContext(), "Executing...", Toast.LENGTH_SHORT).show();

			startService(new Intent(this, MonitorService.class));
			new ShowMeasurements().execute(sailApplication.getLastMeasurement());
			startButton.setEnabled(false);
			stopButton.setEnabled(true);

		} else if (buttonPressed.getId() == R.id.stopButton) {
			//Toast.makeText(getApplicationContext(), "Cancelling", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "stopping service");
			stopService(new Intent(this, MonitorService.class));
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
			textView.setText("Stopped\n");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//return super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();  // Menu inflater object
		inflater.inflate(R.menu.main_panel_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//return super.onOptionsItemSelected(item);
		switch (item.getItemId()) {     // figure out what was pressed

			case R.id.itemPrefs:
				startActivity(new Intent(this, PrefsActivity.class));   // start the PrefsActivity
				break;
		}

		return true;
	}

	public class ShowMeasurements extends AsyncTask<String, Integer, String> {

		private String measurement, previousMeasurement = null;

		@Override
		protected String doInBackground(String... strings) {
			previousMeasurement = strings[0];

			while ((measurement == null) || (measurement.equals(previousMeasurement))) measurement = sailApplication
					.getLastMeasurement();
			//Log.d(TAG, "Measurement read: " + measurement);

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);


			textView.append(measurement.replaceAll("%", ", ") + "\n");

			if (sailApplication.isServiceRunning()) new ShowMeasurements().execute(measurement);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}
}