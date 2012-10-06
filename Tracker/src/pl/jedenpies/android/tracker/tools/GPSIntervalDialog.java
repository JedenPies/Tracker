package pl.jedenpies.android.tracker.tools;

import pl.jedenpies.android.tracker.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class GPSIntervalDialog extends DialogWrapper {

	private static class Handler {
		
		private RadioButton gpsAllTheTime;
		private RadioButton gpsEverySecond;
		private RadioButton gpsEvery3Seconds;
		
		private void uncheck() {			
			if (gpsAllTheTime != null) gpsAllTheTime.setChecked(false);
			if (gpsEverySecond != null) gpsEverySecond.setChecked(false);
			if (gpsEvery3Seconds != null) gpsEvery3Seconds.setChecked(false);
		}
	}

	public GPSIntervalDialog(Activity context, OnClickListener listener, int interval) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(
				R.layout.dialog_gps_interval, 
				(ViewGroup) context.findViewById(R.id.dialog_root));

		Handler handler = new Handler();
		handler.gpsAllTheTime = (RadioButton) layout.findViewById(R.id.gps_all_the_time);
		handler.gpsEverySecond = (RadioButton) layout.findViewById(R.id.gps_every_second);
		handler.gpsEvery3Seconds = (RadioButton) layout.findViewById(R.id.gps_every_3_seconds);
		handler.uncheck();
		
		if (interval == 1000) handler.gpsEverySecond.setChecked(true);
		else if (interval == 3000) handler.gpsEvery3Seconds.setChecked(true);
		else handler.gpsAllTheTime.setChecked(true);
		
		this.dialog = new AlertDialog.Builder(context)
			.setTitle(R.string.l_user_data)
			.setView(layout)			
			.setNegativeButton(R.string.l_cancel, listener)
			.setPositiveButton(R.string.l_ok, listener)
			.setCancelable(true)
			.create();
	}
	
	@Override
	public int getDialogType() {
		return DialogWrapper.GPS_INTERVAL_DIALOG;
	}

	@Override
	public void restore() {
		int interval = Preferences.getInstance().getGPSInterval();
		Handler handler = getHandler();
		switch (interval) {
		case 1000:
			handler.gpsEverySecond.setChecked(true);
			break;
		case 3000:
			handler.gpsEvery3Seconds.setChecked(true);
			break;
		default:
			handler.gpsAllTheTime.setChecked(true);
			break;
		}		
	}

	@Override
	public void save() {
		
		Handler handler = getHandler();
		if (handler.gpsEverySecond.isChecked()) Preferences.getInstance().setGPSInterval(1000);
		else if (handler.gpsEvery3Seconds.isChecked()) Preferences.getInstance().setGPSInterval(3000);
		else Preferences.getInstance().setGPSInterval(0);
	}

	private Handler getHandler() {
		
		Handler h = new Handler();		
		h.gpsAllTheTime = (RadioButton) dialog.findViewById(R.id.gps_all_the_time);
		h.gpsEverySecond = (RadioButton) dialog.findViewById(R.id.gps_every_second);
		h.gpsEvery3Seconds = (RadioButton) dialog.findViewById(R.id.gps_every_3_seconds);
		return h;
	}
}
