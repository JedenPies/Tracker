package pl.jedenpies.android.tracker.tools;

import android.app.Dialog;

public abstract class DialogWrapper {

	public static final int REGISTER_DIALOG = 0;
	public static final int LOGIN_DIALOG = 1;
	public static final int PASSWORD_DIALOG = 2;
	public static final int GPS_INTERVAL_DIALOG = 3;
	
	protected Dialog dialog;
	
	public Dialog getDialog() {
		return dialog;
	}
	public abstract int getDialogType();
	public abstract void restore();
	public abstract void save();
}
