package pl.jedenpies.android.tracker.service;

public interface LocalTrackerServiceListener {

	/**
	 * Handles situation where message from service was received.
	 * @param level level of the message, currently not in use
	 * @param text text of the message
	 */
	public void messageReceived(int level, String text);
	
	/**
	 * Handles service status changes
	 * @param status current status
	 */
	public void statusChanged(int status);
}
