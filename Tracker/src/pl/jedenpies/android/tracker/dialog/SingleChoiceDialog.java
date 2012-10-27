package pl.jedenpies.android.tracker.dialog;

import java.util.HashMap;
import java.util.Map;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.SingleChoice;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleChoiceDialog extends AlertDialog implements OnClickListener {

	private static final String LOG_TAG = SingleChoiceDialog.class.getName();
	
	private GUIHandler guiHandler;
	private Map<SingleChoice, View> choices = new HashMap<SingleChoice, View>();
	private SingleChoice currentChoice;
	private SingleChoiceDialogDataProvider provider;
	private int titleResourceId;
	private SingleChoiceDialogListener listener;
	
	private class GUIHandler {
		private LinearLayout lstListOfChoices = (LinearLayout) findViewById(R.id.dlg_list_of_choices);
		private Button btnCancel = (Button) findViewById(R.id.btn_cancel);
		private Button btnOk = (Button) findViewById(R.id.btn_ok);
		private LinearLayout linTitleBar = (LinearLayout) findViewById(R.id.lin_title_bar);
		private TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		
	}
	
	public SingleChoiceDialog(Context context, SingleChoiceDialogDataProvider provider) {
		this(context, provider, -1);
	}
	public SingleChoiceDialog(Context context, SingleChoiceDialogDataProvider provider, int titleResourceId) {		
		super(context);
		this.provider = provider;
		this.titleResourceId = titleResourceId;
	}
	public void setListener(SingleChoiceDialogListener l) {
		this.listener = l;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_single_choice);
		
		this.guiHandler = new GUIHandler();
		
		if (titleResourceId == -1) {
			guiHandler.linTitleBar.setVisibility(View.GONE);
		} else {
			guiHandler.txtTitle.setText(getContext().getResources().getString(titleResourceId));
		}
		
		SingleChoice[] values = provider.getElements();
		Preferences prefs = getPreferences();
		int i = 1;
		for (SingleChoice value : values) {
			View view = getLayoutInflater().inflate(
					R.layout.dialog_single_choice_element, 
					null);			
			view.setOnClickListener(this);
			guiHandler.lstListOfChoices.addView(view);
			
			if (i++ < values.length) {
				getLayoutInflater().inflate(
						R.layout.dialog_single_choice_separator, guiHandler.lstListOfChoices);
			}
			
			choices.put(value, view);
			TextView text = (TextView) view.findViewById(R.id.text);
			text.setText(getContext().getResources().getString(value.getResourceId()));
		}
		setCurrentChoice(provider.getCurrentValue(prefs));
		guiHandler.btnCancel.setOnClickListener(this);
		guiHandler.btnOk.setOnClickListener(this);
	}

	private void setCurrentChoice(SingleChoice newValue) {
		
		if (currentChoice != null) {
			Button check = (Button) choices.get(currentChoice).findViewById(R.id.check);
			check.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.checkbox_unchecked));
		}
		currentChoice = newValue;
		if (currentChoice != null) {
			Button check = (Button) choices.get(currentChoice).findViewById(R.id.check);
			check.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.checkbox_checked));			
		}
	}
	
	@Override
	public void onClick(View v) {
		
		if (v == guiHandler.btnOk) {
			Preferences prefs = getPreferences();
			provider.setValue(prefs, currentChoice.getValue());
			listener.onDialogChanged(this);
			dismiss();
			return;
		}
		if (v == guiHandler.btnCancel) {
			dismiss();
			setCurrentChoice(provider.getCurrentValue(getPreferences()));
			return;
		}
		
		for (SingleChoice choice : choices.keySet()) {
			if (v == choices.get(choice)) {
				setCurrentChoice(choice);
				return;
			}
		}		
	}
	
	private Preferences getPreferences() {
		return new Preferences((Application) getContext().getApplicationContext());
	}
}
