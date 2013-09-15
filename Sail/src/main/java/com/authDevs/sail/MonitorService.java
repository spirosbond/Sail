package com.authDevs.sail;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by spiros on 8/1/13.
 */

public class MonitorService extends Service {

	private static final String TAG = "MonitorService";
	private static final int DELAY = 9000;
	public Updater updater;
	boolean runFlag = false;
	private int serverPort = 48000;// the port used by the server
	private String hostIPstr = "83.212.121.161";
	private String MAC;
	private SailApplication sailApplication;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d(TAG, "onCreated");
		this.updater = new Updater();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStarted");
		sailApplication = (SailApplication) getApplication();
		MAC = sailApplication.getMAC();
		runFlag = true;
		sailApplication.setServiceRunning(true);
		updater.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		runFlag = false;
		updater.interrupt();
		updater = null;
		sailApplication.setServiceRunning(false);
		Log.d(TAG, "onDestroyed");
	}

	private class Updater extends Thread {

		public Updater() {
			super("UpdaterService-Updater");
		}

		@Override
		public void run() {

			Socket sk = null;
			DataOutputStream dos = null;
			DataInputStream dis = null;
			String measurement = "";

			try {
				Log.d(TAG, "Trying to open socket");
				sk = new Socket(hostIPstr, serverPort);
				Log.d(TAG, "Socket opened");
				dos = new DataOutputStream(sk.getOutputStream());
				dis = new DataInputStream(sk.getInputStream());
				dos.writeBytes(MAC + "\r\n");
				Log.d(TAG, "MAC sent " + MAC);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (runFlag) {
				Log.d(TAG, "Updater running");

				try {
					measurement = dis.readLine();

					if (measurement.contains("Ending Connection")) {
						runFlag = false;
						break;
					}
					sailApplication.addMeasurement(measurement);

				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.d(TAG, measurement);
				Log.d(TAG, "Updater ran");
				try {
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.d(TAG, "Thread terminated");
			try {
				sk.close();
				//monitorService.onDestroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
