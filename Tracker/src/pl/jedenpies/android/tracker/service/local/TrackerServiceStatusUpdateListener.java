package pl.jedenpies.android.tracker.service.local;

import pl.jedenpies.android.tracker.service.TrackerServiceStatus;

public interface TrackerServiceStatusUpdateListener {

	public void statusUpdated(TrackerServiceStatus status);
}
