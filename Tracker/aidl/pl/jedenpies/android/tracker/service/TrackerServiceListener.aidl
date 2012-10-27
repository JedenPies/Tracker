package pl.jedenpies.android.tracker.service;

import pl.jedenpies.android.tracker.service.TrackerServiceMessage;

interface TrackerServiceListener {
	
	void messageSent(in TrackerServiceMessage message);
	void statusChanged(int status);
}