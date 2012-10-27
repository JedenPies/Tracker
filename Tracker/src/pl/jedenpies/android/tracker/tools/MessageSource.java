package pl.jedenpies.android.tracker.tools;

public interface MessageSource {

	public void addMessageListener(MessageListener l);
	public void removeMessageListener(MessageListener l);
}
