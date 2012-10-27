package pl.jedenpies.android.tracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Dao {

	private static final String LOG_TAG = Dao.class.getName();
	
	private SQLiteDatabase database;
	private TrackerServiceDbHelper dbHelper;
	
	public Dao(Context context) {
		dbHelper = new TrackerServiceDbHelper(context);		
	}	
	
	synchronized public void open() {	
		database = dbHelper.getWritableDatabase();
	}
	
	synchronized public void close() {
		dbHelper.close();
		database = null;
	}
	
	protected SQLiteDatabase getDatabase() throws NotAvailableException {
		
		if (this.database == null) {
			throw new NotAvailableException();
		}
		return this.database;
	}
}
