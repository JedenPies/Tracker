package pl.jedenpies.android.tracker.tools;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

public class Preferences {

	public static final String SHARED_PREFERENCES_NAME = "pl.jedenpies.android.tracker";
	public static final String PREF_KEY_LOGIN = "user_login";
	public static final String PREF_KEY_PASSWORD = "user_password";
	public static final String PREF_KEY_GPS_INTERVAL = "gps_interval";
	public static final String PREF_KEY_DATA_PACKET_SIZE = "data_packet_size";
	public static final String PREF_KEY_NETWORK_TYPE = "network_type";
	
	private Application context;	
	
	public Preferences(Application context) {
		this.context = context;
	}
	
	public void setUserLogin(String login) {
		SharedPreferences.Editor editor = getEditor();
		editor.putString(PREF_KEY_LOGIN, login);
		editor.commit();
	}
	public String getUserLogin() {
		return getPreferences().getString(PREF_KEY_LOGIN, "");
	}

	public void setUserPassword(String password) {
		SharedPreferences.Editor editor = getEditor();
		editor.putString(PREF_KEY_PASSWORD, password);
		editor.commit();
	}
	public String getUserPassword() {
		return getPreferences().getString(PREF_KEY_PASSWORD, "");
	}
	public void setGPSInterval(int miliseconds) {
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(PREF_KEY_GPS_INTERVAL, miliseconds);
		editor.commit();
	}
	public int getGPSInterval() {
		return getPreferences().getInt(PREF_KEY_GPS_INTERVAL, 1000);
	}
	public void setDataPacketSize(int size) {
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(PREF_KEY_DATA_PACKET_SIZE, size);
		editor.commit();
	}
	public int getDataPacketSize() {
		return getPreferences().getInt(PREF_KEY_DATA_PACKET_SIZE, 1000);
	}
	public void setNetworkType(int type) {
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(PREF_KEY_NETWORK_TYPE, type);
		editor.commit();
	}
	public int getNetworkType() {
		return getPreferences().getInt(PREF_KEY_NETWORK_TYPE, 10);
	}
	
	
	private SharedPreferences getPreferences() {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_MULTI_PROCESS);
	}
	private SharedPreferences.Editor getEditor() {
		return getPreferences().edit();
	}
}
