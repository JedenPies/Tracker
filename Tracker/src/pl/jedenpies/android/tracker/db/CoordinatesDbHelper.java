package pl.jedenpies.android.tracker.db;

import android.database.sqlite.SQLiteDatabase;

public class CoordinatesDbHelper {
	
	public static final String TABLE_NAME = "coordinates";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_PACKET_ID = "packet_id";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_NAME + "(" +
			 COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 COLUMN_LATITUDE + " REAL NOT NULL, " +
			 COLUMN_LONGITUDE + " REAL NOT NULL," +
			 COLUMN_PACKET_ID + " INTEGER," +
			 COLUMN_TIMESTAMP + " INTEGER);";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase db, int fromVersion, int toVersion) {}
}
