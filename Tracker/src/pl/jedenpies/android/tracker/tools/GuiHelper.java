package pl.jedenpies.android.tracker.tools;

import pl.jedenpies.android.tracker.R;
import android.content.Context;
import android.widget.Button;

public class GuiHelper {

	private Context ctx;
	
	public GuiHelper(Context ctx) {
		this.ctx = ctx;
	}
	
	public void setDisabled(Button button) {
		button.setEnabled(false);				
		button.setTextColor(ctx.getResources().getColor(R.color.c03));
	}
	public void setEnabled(Button button) {
		button.setEnabled(true);
		button.setTextColor(ctx.getResources().getColor(R.color.c01));
	}
}
