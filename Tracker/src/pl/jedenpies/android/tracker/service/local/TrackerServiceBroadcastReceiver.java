package pl.jedenpies.android.tracker.service.local;

import java.util.HashSet;
import java.util.Set;

import pl.jedenpies.android.tracker.service.TrackerServiceStatus;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TrackerServiceBroadcastReceiver extends BroadcastReceiver {

	private Set<TrackerServiceStatusUpdateListener> tssListeners = new HashSet<TrackerServiceStatusUpdateListener>(1);
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	}
	
	void onConnected() {
		fireStatusUpdate(TrackerServiceStatus.RUNNING);
	}
	void onDisconnected() {		
	}
	
	
	public void addStatusUpdateListener(TrackerServiceStatusUpdateListener l) {
		tssListeners.add(l);
	}
	public void removeStatusUpdateListener(TrackerServiceStatusUpdateListener l) {
		tssListeners.remove(l);
	}
	protected void fireStatusUpdate(TrackerServiceStatus status) {
		for (TrackerServiceStatusUpdateListener l : tssListeners) {
			l.statusUpdated(status);
		}
	}

}
