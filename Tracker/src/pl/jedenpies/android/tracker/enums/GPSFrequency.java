package pl.jedenpies.android.tracker.enums;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.dialog.SingleChoiceDialogDataProvider;
import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.SingleChoice;

public enum GPSFrequency implements SingleChoice {

	CONTINUOSLY(0, R.string.prf_gps_interval_0),  
	EVERY_3_SECONDS(3, R.string.prf_gps_interval_3), 
	EVERY_5_SECONDS(5, R.string.prf_gps_interval_5),
	EVERY_15_SECONDS(15, R.string.prf_gps_interval_15);
	
	private int value;
	private int resourceId;
	
	private GPSFrequency(int value, int resourceId) {
		this.value = value;
		this.resourceId = resourceId;
	}
	
	public int getValue() {
		return value;
	}
	public int getResourceId() {
		return resourceId;
	}
	
	public static GPSFrequency getByValue(int value) {
		for (GPSFrequency gpsi : GPSFrequency.values()) {
			if (gpsi.value == value) {
				return gpsi;
			}
		}
		return null;
	}
	
	public static class Provider implements SingleChoiceDialogDataProvider {

		@Override
		public SingleChoice[] getElements() {
			return GPSFrequency.values();
		}
		@Override
		public SingleChoice getCurrentValue(Preferences preferences) {
			return GPSFrequency.getByValue(preferences.getGPSInterval());
		}

		@Override
		public void setValue(Preferences prefs, int value) {
			prefs.setGPSInterval(value);				
		}		
	}	
}
