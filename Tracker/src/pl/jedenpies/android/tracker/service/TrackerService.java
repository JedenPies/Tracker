package pl.jedenpies.android.tracker.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.activity.MainActivity;
import pl.jedenpies.android.tracker.client.TrackerServerClient;
import pl.jedenpies.android.tracker.client.TrackerServerResponse;
import pl.jedenpies.android.tracker.db.NotAvailableException;
import pl.jedenpies.android.tracker.db.TrackerServiceDao;
import pl.jedenpies.android.tracker.db.model.Packet;
import pl.jedenpies.android.tracker.enums.NetworkType;
import pl.jedenpies.android.tracker.tools.Message;
import pl.jedenpies.android.tracker.tools.MessageListener;
import pl.jedenpies.android.tracker.tools.Preferences;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TrackerService extends Service {
	
	public final static int STATUS_STOPPED  = 0;
	public final static int STATUS_STARTING = 1;
	public final static int STATUS_RUNNING  = 2;
	public final static int STATUS_STOPPING = 3;

	private final static String LOG_NAME = TrackerService.class.getName();
	
	private Set<TrackerServiceListener> listeners = new HashSet<TrackerServiceListener>();
		
	private TrackerServiceDao dao;
	private PacketsSender packetsSender;
	
	private int status;
	private Object statusLock = new Object();	
	
	private NetworkType networksAllowed;
	private int gpsFrequency;
	private int packetSize;	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
				
		Log.d(LOG_NAME, "Starting TrackerService...");
		setStatus(STATUS_STARTING);
		
		dao = new TrackerServiceDao(this);
		dao.open();

		Preferences prefs = new Preferences(getApplication());
		networksAllowed = NetworkType.getByValue(prefs.getNetworkType());
		gpsFrequency = prefs.getGPSInterval();
		packetSize = prefs.getDataPacketSize();
		Log.d(LOG_NAME, "gpsFrequency: " + gpsFrequency);
		
		packetsSender = new PacketsSender();
		packetsSender.start();
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				gpsFrequency * 1000, 0, this.locationListener);
		
		sendNotification(
				getResources().getString(R.string.srv_notification_title),
				getResources().getString(R.string.srv_notification_text));
		
		setStatus(STATUS_RUNNING);
		Log.d(LOG_NAME, "TrackerService started.");
		
		sendNextPacket();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	@Override
	public void onDestroy() {
		
		Log.d(LOG_NAME, "Stopping TrackerService...");
		setStatus(STATUS_STOPPING);
		
		cancelNotification();
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this.locationListener);		
		
		packetsSender.getHandler().postAtFrontOfQueue(new FinishJobTask());		
		
		super.onDestroy();		
		setStatus(STATUS_STOPPED);
		Log.d(LOG_NAME, "TrackerService stoped.");
	}	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return apiEndpoint;
	}
	
	
	private void setStatus(int status) {
		synchronized (statusLock) {
			this.status = status;
		}
		fireStatusChanged(status);
	}
	
	private void fireStatusChanged(int status) {
		synchronized (listeners) {
			for (TrackerServiceListener l : listeners) {			
				try {
					l.statusChanged(status);
				} catch (RemoteException e) {
					Log.e(LOG_NAME, e.getMessage());
				}
			}
		}
	}
	
	private void sendNotification(String message, String details) {				
				
		NotificationCompat.Builder b = new NotificationCompat.Builder(this);	
		b.setOnlyAlertOnce(true)			
			.setTicker(message)
			.setContentTitle(message)
			.setContentText(details)
			.setOnlyAlertOnce(true)
			.setWhen(System.currentTimeMillis()).setSmallIcon(R.drawable.icon_application)
			.setOngoing(true)
			.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, b.getNotification());
	}

	private void cancelNotification() {
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(0);
	}
	
	private void sendMessage(String messageText) {
		Log.d(LOG_NAME, "Sending message: " + messageText);
		TrackerServiceMessage message = new TrackerServiceMessage(0, messageText);
		sendMessage(message);
	}
	
	private void sendMessage(TrackerServiceMessage message) {
		Log.d(LOG_NAME, "Broacasting message to listeners...");
		for (TrackerServiceListener l : listeners) {
			try {
				l.messageSent(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void sendNextPacket() {
		try {
			Packet pendingPacket = dao.findFirstPendingPacket();
			if (pendingPacket != null) packetsSender.getHandler().post(new SendPacketTask(pendingPacket));
		} catch (NotAvailableException e) { /* Nothing can be done */ }				
	}
	
	private MessageListener messageListener = new MessageListener() {
		
		@Override
		public void onMessage(Message message) {
			TrackerServiceMessage tsMessage = new TrackerServiceMessage(message);
			sendMessage(tsMessage);			
		}
	};
	
	private LocationListener locationListener = new LocationListener() {
		
		private double lastLatitude = 0;
		private double lastLongitude = 0;
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		@Override
		public void onProviderEnabled(String provider) {}
		
		@Override
		public void onProviderDisabled(String provider) {}
		
		@Override
		public void onLocationChanged(Location location) {
			
			DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.ENGLISH);
			DecimalFormat df = new DecimalFormat("#.####", s);
			double longitude = Double.valueOf(df.format(location.getLongitude()));
			double latitude  = Double.valueOf(df.format(location.getLatitude()));
			if (longitude != lastLongitude || latitude != lastLatitude) {
				lastLongitude = longitude;
				lastLatitude = latitude;
				packetsSender.getHandler().post(new AddCoordinatesTask(latitude, longitude));
			}
		}
	};
	
	private TrackerServiceAPI.Stub apiEndpoint = new TrackerServiceAPI.Stub() {

		@Override
		public int getStatus() throws RemoteException {
			return status;
		}

		@Override
		public void addServiceListener(TrackerServiceListener l) throws RemoteException {
			Log.d(LOG_NAME, "New status listener registered");
			synchronized (listeners) {
				listeners.add(l);				
			}			
		}

		@Override
		public void removeServiceListener(TrackerServiceListener l) throws RemoteException {
			Log.d(LOG_NAME, "status listener unregistered");
			synchronized (listeners) {				
				listeners.remove(l);			
			}
		}
	};

	private class FinishJobTask implements Runnable {

		@Override
		public void run() {
			dao.close();
		}	
	}
	
	private class AddCoordinatesTask implements Runnable {

		private double latitude;
		private double longitude;
		
		private AddCoordinatesTask(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		@Override
		public void run() {
			
			try {			
				dao.createCoordinates(latitude, longitude);
				if (dao.createPackets(packetSize) > 0) sendNextPacket();
				
			} catch (NotAvailableException e) {
				Log.w(AddCoordinatesTask.class.getCanonicalName(), "Cannot finish job. Database unavailable.");
				e.printStackTrace();
			}
			
		}
		
	}
	
	private class SendPacketTask implements Runnable {

		private Packet packet;
		
		private SendPacketTask(Packet packet) {
			this.packet = packet;
		}
		
		@Override
		public void run() {
			
			try {			
				// Check data connection type
				ConnectivityManager cm =
						(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = cm.getActiveNetworkInfo();
				if (!networksAllowed.hasType(info.getType())) {
					dao.markPacketCreated(packet.getId());
					return;			
				}
				
				// Check server availability
				TrackerServerClient client = new TrackerServerClient();			
				if (!client.checkStatus()) {
					Log.d(LOG_NAME, "Server is not available.");
					dao.markPacketCreated(packet.getId());
					return;
				}
				
				// Try to log in
				Preferences prefs = new Preferences(getApplication());
				TrackerServerResponse response = client.userLogin(prefs.getUserLogin(), prefs.getUserPassword());
				if (!TrackerServerResponse.STATUS_OK.equals(response.getStatus())) {
					Log.d(LOG_NAME, "Login failed");
					dao.markPacketCreated(packet.getId());
					return;
				}
				
				response = client.sendPacket(packet);
				if (!TrackerServerResponse.STATUS_OK.equals(response.getStatus())) {
					Log.d(LOG_NAME, "Sending packet failed: " + response.getErrorMessage());
					dao.markPacketCreated(packet.getId());
					return;
				}
				dao.markPacketSent(packet.getId());
				sendNextPacket();
				
			} catch (NotAvailableException e) { /* Nothing can be done here */ }
		}
	}

}
