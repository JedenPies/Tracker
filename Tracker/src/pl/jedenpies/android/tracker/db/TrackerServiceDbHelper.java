package pl.jedenpies.android.tracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TrackerServiceDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "tracker_service.db";
	private static final int    DATABASE_VERSION = 1;
		
	public TrackerServiceDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		PacketsDbHelper.onCreate(db);
		CoordinatesDbHelper.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CoordinatesDbHelper.onUpdate(db, oldVersion, newVersion);
		PacketsDbHelper.onUpdate(db, oldVersion, newVersion);
	}

}
