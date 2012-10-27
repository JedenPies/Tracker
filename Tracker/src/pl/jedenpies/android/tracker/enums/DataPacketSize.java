package pl.jedenpies.android.tracker.enums;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.dialog.SingleChoiceDialogDataProvider;
import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.SingleChoice;

public enum DataPacketSize implements SingleChoice {

	SIZE_200(200, R.string.prf_packet_size_200),
	SIZE_500(500, R.string.prf_packet_size_500),
	SIZE_1000(1000, R.string.prf_packet_size_1000);
	
	private int value;
	private int resourceId;

	private DataPacketSize(int value, int resourceId) {
		this.value = value;
		this.resourceId = resourceId;
	}
	
	@Override
	public int getValue() {
		return value;
	}
	@Override
	public int getResourceId() {
		return resourceId;
	}

	public static DataPacketSize getByValue(int value) {
		
		for (DataPacketSize dps : DataPacketSize.values()) {
			if (dps.value == value) {
				return dps;
			}
		}
		return null;
	}
	
	public static class Provider implements SingleChoiceDialogDataProvider {

		@Override
		public SingleChoice[] getElements() {
			return DataPacketSize.values();
		}
		@Override
		public SingleChoice getCurrentValue(Preferences preferences) {
			return DataPacketSize.getByValue(preferences.getDataPacketSize());
		}

		@Override
		public void setValue(Preferences prefs, int value) {
			prefs.setDataPacketSize(value);				
		}		
	}
}
