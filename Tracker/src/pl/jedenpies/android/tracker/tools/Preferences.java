package pl.jedenpies.android.tracker.tools;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

public class Preferences {

	private static final String SHARED_PREFERENCES_NAME = "pl.jedenpies.android.tracker";
	private static final String PREF_KEY_LOGIN = "user_login";
	private static final String PREF_KEY_PASSWORD = "user_password";
	private static final String PREF_KEY_GPS_INTERVAL = "gps_interval";
	
	private Application context;
	private static Preferences instance;	
	
	private Preferences(Application context) {
		this.context = context;
	}
	
	public static Preferences create(Application context) {
		if (instance == null) {
			instance = new Preferences(context);
		}
		return instance;		
	}	
	public static Preferences getInstance() {
		return instance;
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
	
	
	private SharedPreferences getPreferences() {
		return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE);
	}
	private SharedPreferences.Editor getEditor() {
		return getPreferences().edit();
	}
}
