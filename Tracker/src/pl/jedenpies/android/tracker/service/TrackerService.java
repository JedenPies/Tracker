package pl.jedenpies.android.tracker.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import pl.jedenpies.android.tracker.MainActivity;
import pl.jedenpies.android.tracker.db.AddCoordinatesTask;
import pl.jedenpies.android.tracker.db.TrackerServiceDao;
import pl.jedenpies.android.tracker.tools.Preferences;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class TrackerService extends Service implements LocationListener {
	
	public static final String MESSAGE_TYPE = "pl.jedenpies.android.tracker.BROADCAST";
	public static final String SERVICE_NAME = "pl.jedenpies.android.tracker.TRACKER_SERVICE";

	private static String LOG_TAG = TrackerService.class.getName();
	
	private TrackerServiceDao dao = new TrackerServiceDao(this);
	private PacketsSender pSender = new PacketsSender(dao, this);

	
	private double lastLatitude = 0;
	private double lastLongitude = 0;
//	private PowerManager.WakeLock wakeLock;
	
	private TrackerServiceBinder.Stub binder = new TrackerServiceBinder.Stub() {};
		
	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	@Override
	public void onCreate() {		
		Log.i(LOG_TAG, "onCreate()");
		super.onCreate();
		dao.open();
		if (Preferences.getInstance() == null) Preferences.create(getApplication());
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 
				Preferences.getInstance().getGPSInterval(), 0, this);
		pSender.start();
		sendNotification("Tracker Service is running", "started");
	}

	@Override
	public void onDestroy() {
		Log.i(LOG_TAG, "onDestroy()");
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);		
		pSender.stop();
		dao.close();
		cancelNotification();
		super.onDestroy();		
	}

	
	@Override
	public void onLocationChanged(Location location) {	
		Log.d(LOG_TAG, "Location received");
		DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.ENGLISH);
		DecimalFormat df = new DecimalFormat("#.######", s);
		double longitude = Double.valueOf(df.format(location.getLongitude()));
		double latitude  = Double.valueOf(df.format(location.getLatitude()));
		if (longitude != lastLongitude || latitude != lastLatitude) {
			lastLongitude = longitude;
			lastLatitude = latitude;
			new AddCoordinatesTask(dao).execute(latitude, longitude);
			pSender.notifyWorker();
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (status != LocationProvider.AVAILABLE) Log.e(LOG_TAG, "HUJ");
	}

	private void sendNotification(String message, String details) {				
		
		Builder b = new Notification.Builder(this);	
		b.setOnlyAlertOnce(true)			
			.setTicker(message)
			.setContentTitle(message)
			.setContentText(details)
			.setOnlyAlertOnce(true)
			.setWhen(System.currentTimeMillis()).setSmallIcon(android.R.drawable.star_off)
			.setOngoing(true)
			.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, b.getNotification());
	}
	
	private void cancelNotification() {
		NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(0);
	}
	
	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}

}
