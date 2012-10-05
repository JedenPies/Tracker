package pl.jedenpies.android.tracker.service;

import java.util.Calendar;

import pl.jedenpies.android.tracker.client.TrackerServerClient;
import pl.jedenpies.android.tracker.db.NotAvailableException;
import pl.jedenpies.android.tracker.db.TrackerServiceDao;
import pl.jedenpies.android.tracker.db.model.Packet;
import pl.jedenpies.android.tracker.service.Timer.SleepyHead;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

public class PacketsSender implements Runnable, SleepyHead {

	private static final String LOGGER_NAME = PacketsSender.class.getName();
	
	private final Timer timer = new Timer(this);
	
	private boolean running;
	private Thread myThread;
	private TrackerServiceDao dao;
	private Context context;
	private Calendar nextTry;
	private int tryCount;
	
	public PacketsSender(TrackerServiceDao dao, Context context) {
		this.dao = dao;		
		this.context = context;
	}
	
	public void start() {
		if (running) return;
		running = true;
		myThread = new Thread(this);
		myThread.start();
	}
	
	public void stop() {
		this.running = false;
		myThread.interrupt();
	}
	
	synchronized public void notifyWorker() {
		notify();
	}

	@Override
	synchronized public void run() {
		
		try {	
			while (running) {						
				Log.v(LOGGER_NAME, "still running");
				doSending();
				wait();					
			}
		} catch (InterruptedException e) {
			running = false;
			Log.i(LOGGER_NAME, "PacketsSenderThread interrupted");
		}
	}
	
	private void doSending() {
		try {
			if (Calendar.getInstance().before(nextTry)) return;
			if (dao.findFirstPendingPacket() == null) return;

			Packet packet;						
			TrackerServerClient client = new TrackerServerClient();
			boolean success = true;			
			if (client.checkStatus()) {
				tryCount = 0;
				Log.d(LOGGER_NAME, "Server available. Sending packets");
				while ((packet = dao.findFirstPendingPacket()) != null && success) {					
					sendNotification("Packets found. Trying to send.");
					if (client.sendPacket(packet)) {
						Log.v(LOGGER_NAME, "Packet sent");
						dao.markPacketSent(packet.getId());
					}	
				}
				sendNotification("Sending done.");
				return;
			}			
			tryCount++;
			Log.d(LOGGER_NAME, "Server unavailable");
			//sendNotification("Sending failed.", "Next try in " + Math.min(tryCount + 1, 15) + " minute(s).");					
			nextTry = Calendar.getInstance();
			nextTry.add(Calendar.MINUTE, Math.min(tryCount, 15));
			timer.setTimer(nextTry);
			
		} catch (NotAvailableException e) {
			Log.e(LOGGER_NAME, "Database not available. Stop sending packets.");
			sendNotification("Sending failed");
		}
	}
	
	private void sendNotification(String message) {
		//sendNotification(message, null);
	}
	private void sendNotification(String message, String details) {				
		Builder b = new Notification.Builder(context);			
		b.setOnlyAlertOnce(true).			
			setTicker(message).
			setContentTitle(message).
			setContentText(details).
			setOnlyAlertOnce(true).
			setWhen(System.currentTimeMillis()).
			setSmallIcon(android.R.drawable.star_off);
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, b.getNotification());
	}

	@Override
	public void wakeUp() {
		this.notifyWorker();		
	}
	
}
