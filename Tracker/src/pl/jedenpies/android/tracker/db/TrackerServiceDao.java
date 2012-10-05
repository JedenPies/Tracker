package pl.jedenpies.android.tracker.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.jedenpies.android.tracker.db.model.Coordinates;
import pl.jedenpies.android.tracker.db.model.Packet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TrackerServiceDao {

	private static final String LOGGER_NAME = TrackerServiceDao.class.getName(); 
	
	private static final String SQL_COUNT_UNPACKAGED = 
			"SELECT COUNT(*) FROM " + CoordinatesDbHelper.TABLE_NAME + 
			" WHERE " + CoordinatesDbHelper.COLUMN_PACKET_ID + " IS NULL";
	private static final String SQL_ADD_COORDS_TO_PACKET =
		"UPDATE " + CoordinatesDbHelper.TABLE_NAME + " " +
		"SET " + CoordinatesDbHelper.COLUMN_PACKET_ID + " = ? " +
		"WHERE " + CoordinatesDbHelper.COLUMN_PACKET_ID + " IS NULL ";
	
	private SQLiteDatabase database;
	private TrackerServiceDbHelper dbHelper;
	
	public TrackerServiceDao(Context context) {
		dbHelper = new TrackerServiceDbHelper(context);		
	}	
	
	synchronized public void open() {	
		Log.v(LOGGER_NAME, "Getting database handler");
		database = dbHelper.getWritableDatabase();
		Log.v(LOGGER_NAME, "Handler got");
	}
	
	synchronized public void close() {
		Log.v(LOGGER_NAME, "Releasing database handler...");
		dbHelper.close();
		database = null;
		Log.v(LOGGER_NAME, "Released");
	}
	
	synchronized public Coordinates createCoordinates(double latitude, double longitude) throws NotAvailableException {
		long timestamp = new Date().getTime();
		ContentValues cval = new ContentValues();
		cval.put(CoordinatesDbHelper.COLUMN_LATITUDE, latitude);
		cval.put(CoordinatesDbHelper.COLUMN_LONGITUDE, longitude);
		cval.put(CoordinatesDbHelper.COLUMN_TIMESTAMP, timestamp);
		long insertedId = getDatabase().insert(CoordinatesDbHelper.TABLE_NAME, null, cval);
		return new Coordinates(insertedId, latitude, longitude, timestamp);
	}
	
	synchronized public long countNotPackaged() throws NotAvailableException {
		
		Cursor cur = getDatabase().rawQuery(SQL_COUNT_UNPACKAGED, new String[] {});		
		cur.moveToFirst();
		long count = cur.getInt(0);		
		cur.close();
		Log.v(LOGGER_NAME, "Not packaged coords count: " + count);
		return count;
	}
	
	synchronized public List<Coordinates> findCoordinatesForPacket(long packetId) throws NotAvailableException {
		
		List<Coordinates> result = new ArrayList<Coordinates>();
		Cursor curr = getDatabase().query(
			CoordinatesDbHelper.TABLE_NAME, null,
			CoordinatesDbHelper.COLUMN_PACKET_ID + " = ?", 
			new String[] { String.valueOf(packetId) }, 
			null, null, null);
		Log.v(LOGGER_NAME, "Cursor size: " + curr.getCount());
		curr.moveToFirst();
		while (!curr.isAfterLast()) {
			result.add(new Coordinates(
				curr.getLong(curr.getColumnIndexOrThrow(CoordinatesDbHelper.COLUMN_ID)), 
				curr.getDouble(curr.getColumnIndex(CoordinatesDbHelper.COLUMN_LATITUDE)), 
				curr.getDouble(curr.getColumnIndex(CoordinatesDbHelper.COLUMN_LONGITUDE)), 
				curr.getLong(curr.getColumnIndex(CoordinatesDbHelper.COLUMN_TIMESTAMP)),
				curr.getLong(curr.getColumnIndex(CoordinatesDbHelper.COLUMN_PACKET_ID))));
			curr.moveToNext();
		}
		curr.close();
		return result;
	}
	
	synchronized public Packet createPacket(int packetSize) throws NotAvailableException {
		
		ContentValues val = new ContentValues();
		long timestamp = new Date().getTime();
		val.put(PacketsDbHelper.COLUMN_SIZE, packetSize);
		val.put(PacketsDbHelper.COLUMN_CREATED, timestamp);		
		long id = this.getDatabase().insert(PacketsDbHelper.TABLE_NAME, null, val);
		return new Packet(id, packetSize, timestamp);		
	}
	
	synchronized public int createPackets(int packetSize) throws NotAvailableException {
		
		try {
			Log.v(LOGGER_NAME, "creatingPackets");
			getDatabase().beginTransaction();
			int counter = 0;
			while (countNotPackaged() >= packetSize) { 
				Packet packet = createPacket(packetSize);
				// TODO: Tu powinien nastepowac podzial na pakiety o odpowiedniej wielkosci, a nie wszystko jak leci
				// Czy da sie to zaimplementowac w SQLite?
				Cursor c = getDatabase().rawQuery(SQL_ADD_COORDS_TO_PACKET, new String[] { String.valueOf(packet.getId()) });
				c.moveToFirst(); // Niezbedy aby zakomitowac transakcje
				c.close();
				counter++;
			}
			Log.v(LOGGER_NAME, "Commiting");
			getDatabase().setTransactionSuccessful();
			return counter;
		} finally {
			Log.v(TrackerServiceDao.class.getName(), "Ending transaction");
			getDatabase().endTransaction();			
		}
	}
	
	public Packet findFirstPendingPacket() throws NotAvailableException {
		Cursor cursor = getDatabase().query(
			PacketsDbHelper.TABLE_NAME, 
			new String[] { 
				PacketsDbHelper.COLUMN_ID,
				PacketsDbHelper.COLUMN_CREATED,
				PacketsDbHelper.COLUMN_SIZE }, 
				PacketsDbHelper.COLUMN_SENT + " IS NULL LIMIT 1", 
			null, null, null, null);
		if (cursor.getCount() == 0) { return null; }		
		cursor.moveToFirst();
		Packet packet = new Packet(
			cursor.getLong(cursor.getColumnIndex(PacketsDbHelper.COLUMN_ID)), 
			cursor.getInt(cursor.getColumnIndex(PacketsDbHelper.COLUMN_SIZE)), 
			cursor.getLong(cursor.getColumnIndex(PacketsDbHelper.COLUMN_CREATED)));
		cursor.close();		
		packet.setCoordinates(findCoordinatesForPacket(packet.getId()));
		return packet;
	}
	
	public void markPacketSent(long packetId) throws NotAvailableException {
		try {
			getDatabase().beginTransaction();
			getDatabase().delete(CoordinatesDbHelper.TABLE_NAME, CoordinatesDbHelper.COLUMN_PACKET_ID + " = ?", new String[] { String.valueOf(packetId) });
			ContentValues cv = new ContentValues();
			cv.put(PacketsDbHelper.COLUMN_SENT, new Date().getTime());
			getDatabase().update(PacketsDbHelper.TABLE_NAME, cv, PacketsDbHelper.COLUMN_ID + " = ?", new String[] { String.valueOf(packetId) });
			getDatabase().setTransactionSuccessful();
			Log.v(LOGGER_NAME, "Packet: " + packetId + " marked as sent.");
		} finally {
			getDatabase().endTransaction();
		}
	}

	private SQLiteDatabase getDatabase() throws NotAvailableException {
		if (this.database == null) {
			throw new NotAvailableException();
		}
		return this.database;
	}
}
