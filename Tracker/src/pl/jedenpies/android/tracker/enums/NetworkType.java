package pl.jedenpies.android.tracker.enums;

import java.util.HashSet;
import java.util.Set;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.dialog.SingleChoiceDialogDataProvider;
import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.SingleChoice;
import android.net.ConnectivityManager;

public enum NetworkType implements SingleChoice {

	ALWAYS(0, R.string.prf_network_type_all, new int[] 
			{ ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_MOBILE_DUN, 
			  ConnectivityManager.TYPE_MOBILE_HIPRI, ConnectivityManager.TYPE_MOBILE_MMS, 
			  ConnectivityManager.TYPE_MOBILE_SUPL, ConnectivityManager.TYPE_WIFI,
			  ConnectivityManager.TYPE_WIMAX }),
	HIGH(5, R.string.prf_network_type_high, new int[] 
			{ ConnectivityManager.TYPE_MOBILE_HIPRI, ConnectivityManager.TYPE_WIFI,
			  ConnectivityManager.TYPE_WIMAX }),
	WIFI(10, R.string.prf_network_type_wifi, new int[] 
			{ ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX });
	
	private int value;
	private int resourceId;
	private Set<Integer> types = new HashSet<Integer>();

	private NetworkType(int value, int resourceId, int[] types) {
		this.value = value;
		this.resourceId = resourceId;
		for (int i = 0; i < types.length; i++) this.types.add(types[i]);		
	}
	
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public int getResourceId() {
		return resourceId;
	}
	public boolean hasType(int type) {
		return types.contains(type);
	}

	public static NetworkType getByValue(int value) {
		
		for (NetworkType dps : NetworkType.values()) {
			if (dps.value == value) {
				return dps;
			}
		}
		return null;
	}
	
	public static class Provider implements SingleChoiceDialogDataProvider {

		@Override
		public SingleChoice[] getElements() {
			return NetworkType.values();
		}
		@Override
		public SingleChoice getCurrentValue(Preferences preferences) {
			return NetworkType.getByValue(preferences.getNetworkType());
		}

		@Override
		public void setValue(Preferences prefs, int value) {
			prefs.setNetworkType(value);				
		}		
	}
}
