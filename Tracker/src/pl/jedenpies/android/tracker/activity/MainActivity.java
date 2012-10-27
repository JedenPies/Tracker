package pl.jedenpies.android.tracker.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import pl.jedenpies.android.tracker.R;
import pl.jedenpies.android.tracker.db.NotAvailableException;
import pl.jedenpies.android.tracker.db.PacketsDbHelper;
import pl.jedenpies.android.tracker.db.TrackerServiceDao;
import pl.jedenpies.android.tracker.db.TrackerServiceDbHelper;
import pl.jedenpies.android.tracker.service.LocalTrackerServiceListener;
import pl.jedenpies.android.tracker.service.TrackerService;
import pl.jedenpies.android.tracker.service.TrackerServiceManager;
import pl.jedenpies.android.tracker.tools.Preferences;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The main screen of application.
 * Contains monitor and button to {@link PreferencesActivity} screen.
 * @author Patryk Dobrowolski
 */
public class MainActivity extends Activity implements LocalTrackerServiceListener {

	private static final String LOG_TAG = MainActivity.class.getName();
	
	private TrackerServiceManager serviceManager;
	private GuiHandler guiHandler;
	private GuiUpdater updateHandler;
	private TrackerServiceDao dao;
	private Timer monitorTimer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	Log.v(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        guiHandler = new GuiHandler();

        updateHandler = new GuiUpdater(this, guiHandler);        

        serviceManager = new TrackerServiceManager(this);
        serviceManager.addServiceListener(this);
        
        // If user credentials are not set, switch to credentials screen
        Preferences prefs = new Preferences(getApplication());        
        if (TextUtils.isEmpty(prefs.getUserLogin())	&& TextUtils.isEmpty(prefs.getUserPassword())) {        	
        	Intent intent = new Intent(getApplicationContext(), UserCredentialsActivity.class);
        	startActivity(intent);        	
        }    
        
        dao = new TrackerServiceDao(this);
    }
    
    @Override
	protected void onStart() {
    	Log.v(LOG_TAG, "onStart()");    	
    	dao.open();
    	super.onStart();    	
        serviceManager.bindService();
        monitorTimer = new Timer();
        monitorTimer.schedule(new UpdateMonitorTimerTask(), 0);
	}

	@Override
	protected void onStop() {
		Log.v(LOG_TAG, "onStop()");
		serviceManager.unbindService();
		monitorTimer.schedule(new FinishJobTask(), 2000);
		super.onStop();
	}
	
	protected void onDestroy() {
		serviceManager.removeServiceListener(this);
		super.onDestroy();
	}

	/**
	 * Handles all clicks in the GUI.
	 * @param view view which was clicked.
	 */
	public void onClick(View view) {
		
		if (view == guiHandler.btnPreferences) {
			// Go to preferences screen
			Intent intent = new Intent(this, PreferencesActivity.class);
			startActivity(intent);
			return;
		}
		if (view == guiHandler.btnChkService || view == guiHandler.lblServiceStatus) {
			// Switch service, turn it on or off
			if (serviceManager.getStatus() == TrackerService.STATUS_RUNNING) {
				serviceManager.stopService();
			} else {
				serviceManager.startService();
			}
			return;
		}
	}

	@Override
	public void statusChanged(int status) {
		
		Message m = new Message();
		m.what = GuiUpdater.WHAT_STATUS;
		m.arg1 = status;
		updateHandler.sendMessage(m);	
	}
	
	public void monitorUpdated(MonitorData monitorData) {
		
		Message m = new Message();
		m.what = GuiUpdater.WHAT_MONITOR;
		m.obj = monitorData;
		updateHandler.sendMessage(m);
	}
	
	@Override
	public void messageReceived(int level, String text) {
	}
	

	/**
	 * Class that holds all gui elements on related screen.
	 * @author Patryk
	 */
	private class GuiHandler {
		
		private Button   btnPreferences   = (Button)   findViewById(R.id.btn_preferences);
		private Button 	 btnChkService    = (Button)   findViewById(R.id.chk_service);
		private TextView lblServiceStatus = (TextView) findViewById(R.id.lbl_service_status);
		
		private TextView txtMonCoordsValue = (TextView) findViewById(R.id.txt_monitor_coords_val);
		private TextView txtMonNotSentValue = (TextView) findViewById(R.id.txt_monitor_not_sent_val);
		private TextView txtMonToSendValue = (TextView) findViewById(R.id.txt_monitor_to_sent_val);
		private TextView txtMonSentValue = (TextView) findViewById(R.id.txt_monitor_sent_val);
		private TextView txtMonDbSize = (TextView) findViewById(R.id.txt_monitor_dbsize_val);
	}
	
	/**
	 * Handler to update gui in UI Thread.
	 * @author Patryk
	 *
	 */
	private static class GuiUpdater extends Handler {
		
		private final static int WHAT_STATUS = 1;
		private final static int WHAT_MONITOR = 2;
		
		private Context ctx;
		private GuiHandler guiHandler;

		private GuiUpdater(Context ctx, GuiHandler guiHandler) {
			this.ctx = ctx;
			this.guiHandler = guiHandler;
		}
		
		@Override
		public void handleMessage(Message msg) {
			
			if (msg.what == WHAT_STATUS) handleStatus(msg.arg1);
			else if (msg.what == WHAT_MONITOR) handleMonitor((MonitorData) msg.obj);
		}
		
		private void handleMonitor(MonitorData monitorData) {
			
			guiHandler.txtMonCoordsValue.setText(String.valueOf(monitorData.notPackaged));
			guiHandler.txtMonNotSentValue.setText(String.valueOf(monitorData.notSent));
			guiHandler.txtMonToSendValue.setText(String.valueOf(monitorData.toSent));
			guiHandler.txtMonSentValue.setText(String.valueOf(monitorData.sent));
			guiHandler.txtMonDbSize.setText(String.valueOf(monitorData.databaseSize) + " kb");
		}

		private void handleStatus(int status) {
			
			if (status == TrackerService.STATUS_RUNNING) {
				guiHandler.btnChkService.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.checkbox_checked));
				guiHandler.lblServiceStatus.setText(R.string.l_service_status_running);
				return;
			}
			if (status == TrackerService.STATUS_STOPPED) {
				guiHandler.btnChkService.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.checkbox_unchecked));
				guiHandler.lblServiceStatus.setText(R.string.l_service_status_off);
				return;			
			}
			guiHandler.btnChkService.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.checkbox_unknown));
			guiHandler.lblServiceStatus.setText(R.string.l_service_status_unknown);
		}
	}

	private class UpdateMonitorTimerTask extends TimerTask {

		@Override
		public void run() {
			try {
				MonitorData monitorData = new MonitorData();
				monitorData.notPackaged = dao.countNotPackaged();
				monitorData.notSent = dao.countPackages(PacketsDbHelper.STATUS_CREATED);
				monitorData.toSent = dao.countPackages(PacketsDbHelper.STATUS_PROCESSED);
				monitorData.sent = dao.countPackages(PacketsDbHelper.STATUS_SENT);
				File f = getDatabasePath(TrackerServiceDbHelper.DATABASE_NAME);
				monitorData.databaseSize = f.length() / 1024;
				monitorUpdated(monitorData);
			} catch (NotAvailableException e) {
				/* Nothing can be done */
			} finally {
				monitorTimer.schedule(new UpdateMonitorTimerTask(), 1000);				
			}
		}		
	}
	
	private class FinishJobTask extends TimerTask {

		@Override
		public void run() {
			dao.close();			
		}
		
	}

	/**
	 * Class to pass monitor data between {@link UpdateMonitorTimerTask} and GUI
	 * @author Patryk
	 *
	 */
	private static class MonitorData {
		
		private long notPackaged;
		private long notSent;
		private long toSent;
		private long sent;
		private long databaseSize;
	}
}
