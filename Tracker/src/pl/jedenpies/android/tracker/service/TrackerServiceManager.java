package pl.jedenpies.android.tracker.service;

import java.util.HashSet;
import java.util.Set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class TrackerServiceManager {

	private static final String LOG_TAG = TrackerServiceManager.class.getName();
	
	private Context context;
	private TrackerServiceAPI serviceAPI;
	private Set<LocalTrackerServiceListener> serviceListeners = 
			new HashSet<LocalTrackerServiceListener>();
	
	public TrackerServiceManager(Context context) {
		this.context = context;
	}

	/**
	 * Attempts to start service if it is not started yet.
	 * @return true if service is started, false otherwise
	 */
	public boolean startService() {
		Log.d(LOG_TAG, "Attempting to start service...");
		context.startService(
				new Intent(TrackerService.class.getName())); 
		boolean result = bindService();
		if (result) Log.d(LOG_TAG, "started"); else Log.w(LOG_TAG, "starting failed");
		return result;
	}

	/**
	 * Attempts to bind existing running service.
	 * @return true if service is bound, false otherwise
	 */
	public boolean bindService() {
		Log.d(LOG_TAG, "Attemtping to bind the service...");
		boolean result = context.bindService(
				new Intent(TrackerService.class.getName()), 
				this.serviceConnection, 0);
		if (result) Log.d(LOG_TAG, "bound"); else Log.d(LOG_TAG, "not bound");
		return result;
	}
	
	/**
	 * Unbind service.
	 * @return true
	 */
	public boolean unbindService() {
		Log.d(LOG_TAG, "Attempting to unbind the service...");
		context.unbindService(serviceConnection);
		Log.d(LOG_TAG, "Unbound");
		return true;
	}
	
	/**
	 * Attempts to stop running service.
	 * @return true if service was stopped, false otherwise
	 */
	public boolean stopService() {
		Log.d(LOG_TAG, "Attempting to stop the service...");
		boolean result = context.stopService(
				new Intent(this.context, TrackerService.class));
		if (result) Log.d(LOG_TAG, "Service stopped"); else Log.w(LOG_TAG, "Service not stopped");
		return result;
	}
	
	/**
	 * Returns currents status of background service.
	 * @return one w statuses from TrackerService.
	 */
	public int getStatus() {
		if (serviceAPI == null) return TrackerService.STATUS_STOPPED;
		try {
			int status = serviceAPI.getStatus();
			return status;
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.w(LOG_TAG, e.getMessage());
			// TODO: handle
			return TrackerService.STATUS_STOPPED;
		}
	}
	
	public void addServiceListener(LocalTrackerServiceListener l) {
		synchronized (serviceListeners) {
			serviceListeners.add(l);
		}
	}
	public void removeServiceListener(LocalTrackerServiceListener l) {
		synchronized (serviceListeners) {
			serviceListeners.remove(l);
		}
	}	
	
	/**
	 * Notify all listeners that status has changed.
	 * @param status status to be sent
	 */
	private void fireStatus(int status) {
		synchronized (serviceListeners) {
			for (LocalTrackerServiceListener l : serviceListeners) {
				l.statusChanged(status);
			}
		}		
	}
	
	/**
	 * Notify all listeners that message from service has just arrived.
	 * @param level level of the message
	 * @param text message text
	 */
	private void fireMessage(int level, String text) {
		synchronized (serviceListeners) {
			for (LocalTrackerServiceListener l : serviceListeners) {
				l.messageReceived(level, text);
			}
		}		
	}
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceAPI = null;
			fireStatus(getStatus());			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			try {
				Log.d(LOG_TAG, "bound " + service.getClass());
				serviceAPI = TrackerServiceAPI.Stub.asInterface(service);
				serviceAPI.addServiceListener(statusListener);
				fireStatus(getStatus());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private TrackerServiceListener.Stub statusListener = new TrackerServiceListener.Stub() {
		
		@Override
		public void statusChanged(int status) throws RemoteException {
			fireStatus(status);
		}

		@Override
		public void messageSent(TrackerServiceMessage message) throws RemoteException {
			fireMessage(message.getLevel(), message.getText());
			
		}
	};
}
