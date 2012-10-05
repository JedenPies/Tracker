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

public class PasswordDialog extends DialogWrapper {

	public PasswordDialog(Activity context, OnClickListener listener, String login) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(
				R.layout.dialog_password, 
				(ViewGroup) context.findViewById(R.id.dialog_root));
		EditText loginText = (EditText) layout.findViewById(R.id.login);
		loginText.setText(login);
		
		this.dialog = new AlertDialog.Builder(context)
			.setView(layout)
			.setTitle(R.string.user_data)
			.setNegativeButton(R.string.cancel, listener)
			.setPositiveButton(R.string.modify, listener)		
			.setCancelable(true)
			.create();
	}
	
	@Override
	public int getDialogType() {
		return DialogWrapper.PASSWORD_DIALOG;
	}

	@Override
	public void restore() {
		((EditText) dialog.findViewById(R.id.login)).setText(Preferences.getInstance().getUserLogin());	
	}

	@Override
	public void save() {
		// nothing TODO ?		
	}

}
