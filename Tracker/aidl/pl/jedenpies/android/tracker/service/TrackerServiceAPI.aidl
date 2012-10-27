package pl.jedenpies.android.tracker.service;

import pl.jedenpies.android.tracker.service.TrackerServiceListener;

interface TrackerServiceAPI {
	
	/**
	 * Returns current status of the service.
	 * @return current status
	 */
	int getStatus();	
	void addServiceListener(TrackerServiceListener l);
	void removeServiceListener(TrackerServiceListener l);
}