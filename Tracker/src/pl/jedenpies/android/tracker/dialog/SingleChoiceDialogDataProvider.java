package pl.jedenpies.android.tracker.dialog;

import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.SingleChoice;

public interface SingleChoiceDialogDataProvider {

	public SingleChoice[] getElements();
	public SingleChoice getCurrentValue(Preferences preferences);
	public void setValue(Preferences prefs, int value);

}
