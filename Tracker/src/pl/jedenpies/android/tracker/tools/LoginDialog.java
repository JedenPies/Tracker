package pl.jedenpies.android.tracker.tools;

import pl.jedenpies.android.tracker.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginDialog extends DialogWrapper {

	public LoginDialog(Activity context, OnClickListener listener, String login, String password) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(
				R.layout.dialog_login, 
				(ViewGroup) context.findViewById(R.id.dialog_root));
		EditText loginField = (EditText) layout.findViewById(R.id.login);
		loginField.setText(login);
		EditText passwordField = (EditText) layout.findViewById(R.id.password);
		passwordField.setText(password);
		
		this.dialog = new AlertDialog.Builder(context)
			.setTitle(R.string.user_data)
			.setView(layout)			
			.setNegativeButton(R.string.cancel, listener)
			.setPositiveButton(R.string.ok, listener)
			.setCancelable(true)
			.create();
	}
	
	public String getPassword() {
		return ((EditText) dialog.findViewById(R.id.password)).getText().toString();		
	}
	public void setPassword(String password) {
		((EditText) dialog.findViewById(R.id.password)).setText(password);
	}

	@Override
	public int getDialogType() {		
		return DialogWrapper.LOGIN_DIALOG;
	}

	@Override
	public void restore() {
		((EditText) this.dialog.findViewById(R.id.login)).setText(Preferences.getInstance().getUserLogin());
		((EditText) this.dialog.findViewById(R.id.password)).setText(Preferences.getInstance().getUserPassword());		
	}

	@Override
	public void save() {
		Preferences.getInstance().setUserLogin(
				((EditText) this.dialog.findViewById(R.id.login)).getText().toString());
		Preferences.getInstance().setUserPassword(
				((EditText) this.dialog.findViewById(R.id.password)).getText().toString());
	}
}
