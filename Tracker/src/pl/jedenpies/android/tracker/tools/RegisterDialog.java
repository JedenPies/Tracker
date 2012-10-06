package pl.jedenpies.android.tracker.tools;

import pl.jedenpies.android.tracker.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RegisterDialog extends DialogWrapper {

	public RegisterDialog(Activity context, OnClickListener listener) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(
				R.layout.dialog_register, 
				(ViewGroup) context.findViewById(R.id.dialog_root));

		this.dialog = new AlertDialog.Builder(context)
			.setView(layout)
			.setTitle(R.string.user_data)
			.setNegativeButton(R.string.cancel, listener)
			.setPositiveButton(R.string.l_sign_up, listener)		
			.setCancelable(true)
			.create();
	}
	
	@Override
	public int getDialogType() {
		return DialogWrapper.REGISTER_DIALOG;
	}

	@Override
	public void restore() {
		// nothing TODO ?		
	}

	@Override
	public void save() {
		// nothing TODO ?		
	}

}
