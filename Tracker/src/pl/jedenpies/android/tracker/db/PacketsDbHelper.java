package pl.jedenpies.android.tracker.db;

import android.database.sqlite.SQLiteDatabase;

public class PacketsDbHelper {

	public static final String TABLE_NAME = "packets";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SIZE = "size";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_SENT = "sent";
	
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_NAME + "(" +
			 COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 COLUMN_SIZE + " INTEGER NOT NULL, " +
			 COLUMN_CREATED + " INTEGER NOT NULL," +
			 COLUMN_SENT + " INTEGER);";
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}
	
	public static void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
