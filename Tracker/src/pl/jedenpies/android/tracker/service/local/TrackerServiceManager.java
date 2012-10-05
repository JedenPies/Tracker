package pl.jedenpies.android.tracker.service.local;

import pl.jedenpies.android.tracker.service.TrackerService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class TrackerServiceManager implements ServiceConnection {

	private static final String LOG_TAG = TrackerServiceManager.class.getName();
	
	private Context context;
	private TrackerServiceBroadcastReceiver broadcastReceiver = new TrackerServiceBroadcastReceiver();
	//private TrackerServiceBinder serviceBinder;
	
	public TrackerServiceManager(Context context) {
		this.context = context;
	}
	
	public void start() {
		bindService();
		registerReceiver();
	}
	public void stop() {
		unregisterReceiver();
		unbindService();
	}
	public void startService(boolean start) {
		Intent serviceIntent = new Intent();
		serviceIntent.setAction(TrackerService.SERVICE_NAME);
		if (start) {
			context.startService(serviceIntent);
		} else {
			context.stopService(serviceIntent);
		}
	}
	
	public TrackerServiceBroadcastReceiver getBroadcastReceiver() {
		return broadcastReceiver;
	}
		
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		//this.serviceBinder = TrackerServiceBinder.Stub.asInterface(service);	
		this.broadcastReceiver.onConnected();
	}


	@Override
	public void onServiceDisconnected(ComponentName name) {
		//this.serviceBinder = null;		
	}	
	
	/*
	 * Pomocnicze metody prywatne
	 */
	private boolean bindService() {
		Log.v(LOG_TAG, "bindService()");
		return context.bindService(
				new Intent(this.context, TrackerService.class), 
				this, Context.BIND_ADJUST_WITH_ACTIVITY);
	}
	private void unbindService() {
		Log.v(LOG_TAG, "unbindService()");
		context.unbindService(this);
	}
	private void registerReceiver() {
		Log.v(LOG_TAG, "registerReceiver()");
		context.registerReceiver(
				broadcastReceiver, 
				new IntentFilter(TrackerService.MESSAGE_TYPE));
	}
	private void unregisterReceiver() {
		Log.v(LOG_TAG, "unregisterReceiver()");
		context.unregisterReceiver(broadcastReceiver);
	}
}
