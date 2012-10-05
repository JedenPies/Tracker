package pl.jedenpies.android.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.jedenpies.android.tracker.list.JPListAdapter;
import pl.jedenpies.android.tracker.list.JPListItem;
import pl.jedenpies.android.tracker.list.JPListItemFactory;
import pl.jedenpies.android.tracker.list.JPListItemFactory.JPDialogListItem;
import pl.jedenpies.android.tracker.service.TrackerServiceStatus;
import pl.jedenpies.android.tracker.service.local.TrackerServiceManager;
import pl.jedenpies.android.tracker.service.local.TrackerServiceStatusUpdateListener;
import pl.jedenpies.android.tracker.tools.DialogWrapper;
import pl.jedenpies.android.tracker.tools.GPSIntervalDialog;
import pl.jedenpies.android.tracker.tools.LoginDialog;
import pl.jedenpies.android.tracker.tools.PasswordDialog;
import pl.jedenpies.android.tracker.tools.Preferences;
import pl.jedenpies.android.tracker.tools.RegisterDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

public class MainActivity extends Activity implements TrackerServiceStatusUpdateListener, OnItemClickListener, OnClickListener {

	private static final String LOG_TAG = MainActivity.class.getName();

	private static final String MENU_ITEM_MONITOR = "monitor";
	
	private TrackerServiceManager serviceManager;
	private List<JPListItem> menuItems;
	private Map<String, JPListItem> menuItemsMap = new HashMap<String, JPListItem>();
	@SuppressLint("UseSparseArrays")
	private Map<Integer, DialogWrapper> dialogs = new HashMap<Integer, DialogWrapper>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v(LOG_TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        serviceManager = new TrackerServiceManager(this);
        setContentView(R.layout.activity_main);
        
        populateMenu();
        Preferences.create(getApplication());        
    }
    
    @Override
	protected void onStart() {
    	Log.v(LOG_TAG, "onStart()");
    	super.onStart();    	
    	serviceManager.getBroadcastReceiver().addStatusUpdateListener(this);
    	serviceManager.start();
	}

	@Override
	protected void onDestroy() {
		Log.v(LOG_TAG, "onDestroy()");
		serviceManager.getBroadcastReceiver().removeStatusUpdateListener(this);
		serviceManager.stop();		
		super.onDestroy();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onCheckbox(View view) {
    	switch (view.getId()) {
    	case R.id.checkbox_service:
    		Log.i(MainActivity.class.getName(), "Trying to run service...");
    		CheckBox ck = (CheckBox) view;
    		serviceManager.startService(ck.isChecked());
    	}
    }
    
    public void onSettingsClicked(View view) {    	
    	Log.e(LOG_TAG, "clicked");
    	Intent intent = new Intent(this, pl.jedenpies.android.tracker.listold.MyListActivity.class);
    	startActivity(intent);
    }

	@Override
	public void statusUpdated(TrackerServiceStatus status) {
		CheckBox ck = (CheckBox) findViewById(R.id.checkbox_service);
		ck.setChecked(status == TrackerServiceStatus.RUNNING);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		JPListItem item = menuItems.get(position);
		if (item instanceof JPDialogListItem) {
			DialogWrapper dw = dialogs.get(((JPDialogListItem) item).getDialogId());
			if (dw != null) dw.restore();
			showDialog(((JPDialogListItem) item).getDialogId());
		}
		if (menuItems.get(position) == menuItemsMap.get(MENU_ITEM_MONITOR)) {
			// TODO nowe activity Monitor
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
			
		DialogWrapper dialogw;
		switch (id) {
		case DialogWrapper.LOGIN_DIALOG:
			dialogw = new LoginDialog(
					this, this, 
					Preferences.getInstance().getUserLogin(), 
					Preferences.getInstance().getUserPassword());
			dialogs.put(DialogWrapper.LOGIN_DIALOG, dialogw);
			return dialogw.getDialog();
		
		case DialogWrapper.REGISTER_DIALOG:
			dialogw = new RegisterDialog(this, this);
			dialogs.put(DialogWrapper.REGISTER_DIALOG, dialogw);
			return dialogw.getDialog();			
		
		case DialogWrapper.PASSWORD_DIALOG:
			dialogw = new PasswordDialog(
						this, this,
						Preferences.getInstance().getUserLogin());
			dialogs.put(DialogWrapper.PASSWORD_DIALOG, dialogw);
			return dialogw.getDialog();

		case DialogWrapper.GPS_INTERVAL_DIALOG:
			dialogw = new GPSIntervalDialog(this, this, Preferences.getInstance().getGPSInterval());
			dialogs.put(DialogWrapper.GPS_INTERVAL_DIALOG, dialogw);
			return dialogw.getDialog();
		}
		return null;
	}
	
	
	private void populateMenu() {
		
		ListView mainMenu = (ListView) findViewById(R.id.main_menu);
        menuItems = new ArrayList<JPListItem>(); 
        JPListItem item;
        
        item = JPListItemFactory.iconListItem("Monitor", "brak aktywnoœci", R.drawable.monitor_icon);
        menuItemsMap.put(MENU_ITEM_MONITOR, item);
        menuItems.add(item);
        
        menuItems.add(JPListItemFactory.dialogListItem("Dane logowania do serwera", "nazwa u¿ytkownika i has³o", R.drawable.user_data_icon, DialogWrapper.LOGIN_DIALOG));
        menuItems.add(JPListItemFactory.dialogListItem("Zmieñ has³o", "do serwera", R.drawable.change_password_icon, DialogWrapper.PASSWORD_DIALOG));
        menuItems.add(JPListItemFactory.dialogListItem("Zarejestruj siê", "na serwerze", R.drawable.register_icon, DialogWrapper.REGISTER_DIALOG));        
        
        menuItems.add(JPListItemFactory.dialogListItem("Czêstotliwoœc GPS", "co sekundê", R.drawable.gps_interval_icon, DialogWrapper.GPS_INTERVAL_DIALOG));
                
        JPListAdapter adapter = new JPListAdapter(this, menuItems);
        mainMenu.setOnItemClickListener(this);
        mainMenu.setAdapter(adapter);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		DialogWrapper dialogWrapper = null;
		for (DialogWrapper dw : dialogs.values()) {
			if (dialog == dw.getDialog()) {
				dialogWrapper = dw;
				break;
			}
		}
		
		if (dialogWrapper != null && which == Dialog.BUTTON_POSITIVE) {
			dialogWrapper.save();
		}
	}
}
