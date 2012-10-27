package pl.jedenpies.android.tracker.activity;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.dialog.SingleChoiceDialog;
import pl.jedenpies.android.tracker.dialog.SingleChoiceDialogListener;
import pl.jedenpies.android.tracker.enums.DataPacketSize;
import pl.jedenpies.android.tracker.enums.GPSFrequency;
import pl.jedenpies.android.tracker.enums.NetworkType;
import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.SingleChoice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreferencesActivity extends FragmentActivity implements SingleChoiceDialogListener {
	
	private GuiHandler guiHandler;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);           
        
        guiHandler = new GuiHandler();
        guiHandler.dataPacketSizeDialog =
        		new SingleChoiceDialog(this, new DataPacketSize.Provider(), R.string.l_settings_packet_size);
        guiHandler.dataPacketSizeDialog.setListener(this);
        guiHandler.gpsFrequencyDialog =
        		new SingleChoiceDialog(this, new GPSFrequency.Provider(), R.string.l_settings_gps_frequency);
        guiHandler.gpsFrequencyDialog.setListener(this);
        guiHandler.networkTypeDialog =
        		new SingleChoiceDialog(this, new NetworkType.Provider(), R.string.l_settings_upload_network);
        guiHandler.networkTypeDialog.setListener(this);
        
        refreshValues();
    } 
    
    public void onClick(View view) {
    	
    	if (view == guiHandler.btn_back) {
    		this.finish();
    		return;
    	}
    	if (view == guiHandler.lst_user_credentials) {
    		Intent intent = new Intent(
    				getApplicationContext(), UserCredentialsActivity.class);
    		startActivity(intent);
    		return;
    	}
    	if (view == guiHandler.lst_sign_up) {
    		Intent intent = new Intent(
    				getApplicationContext(), UserSignUpActivity.class);
    		startActivity(intent);
    		return;    		
    	}
    	if (view == guiHandler.lst_gps_frequency) {
    		guiHandler.gpsFrequencyDialog.show();
    		return;
    	}
    	if (view == guiHandler.lst_data_packet_size) {    		
    		guiHandler.dataPacketSizeDialog.show();
    		return;
    	}
    	if (view == guiHandler.lst_network_type) {
    		guiHandler.networkTypeDialog.show();
    		return;    		
    	}
    }

	@Override
	public void onDialogChanged(SingleChoiceDialog dialog) {
		refreshValues();		
	}
	
	private void refreshValues() {
		Preferences prefs = new Preferences(getApplication());
		refreshTextView(guiHandler.txtGPSFrequencyValue, GPSFrequency.getByValue(prefs.getGPSInterval()));
		refreshTextView(guiHandler.txtPacketSizeValue, DataPacketSize.getByValue(prefs.getDataPacketSize()));
		refreshTextView(guiHandler.txtUploadNetworkValue, NetworkType.getByValue(prefs.getNetworkType()));
	}

	private void refreshTextView(TextView view, SingleChoice value) {
		String text;
		if (view == null || value == null) {
			text = "";
		} else {
			text = getResources().getString(
					value.getResourceId());
		}
		view.setText(text);
	}

	private class GuiHandler {
		
		private Button 		 btn_back 			    = (Button) findViewById(R.id.btn_back);
		private LinearLayout lst_user_credentials   = (LinearLayout) findViewById(R.id.lst_user_credentials);
		private LinearLayout lst_sign_up 		    = (LinearLayout) findViewById(R.id.lst_sign_up);
		private LinearLayout lst_gps_frequency	    = (LinearLayout) findViewById(R.id.lst_gps_frequency);
		private LinearLayout lst_data_packet_size   = (LinearLayout) findViewById(R.id.lst_data_packet_size);
		private LinearLayout lst_network_type       = (LinearLayout) findViewById(R.id.lst_network_type);
		
		private TextView txtGPSFrequencyValue  = (TextView) findViewById(R.id.txt_gps_frequency_value);
		private TextView txtUploadNetworkValue = (TextView) findViewById(R.id.txt_upload_network_value);
		private TextView txtPacketSizeValue    = (TextView) findViewById(R.id.txt_packet_size_value);
		
		private SingleChoiceDialog gpsFrequencyDialog;
		private SingleChoiceDialog dataPacketSizeDialog;
		private SingleChoiceDialog networkTypeDialog;
	}
}
