package pl.jedenpies.android.tracker.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.jedenpies.android.tracker.db.model.Coordinates;
import pl.jedenpies.android.tracker.db.model.Packet;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class TrackerServiceDao extends Dao {

	private static final String LOG_TAG = TrackerServiceDao.class.getName();
	
	private static final String SQL_COUNT_UNPACKAGED = 
		"SELECT COUNT(*) FROM " + CoordinatesDbHelper.TABLE_NAME + " " +
		"WHERE " + CoordinatesDbHelper.COLUMN_PACKET_ID + " IS NULL";

	private static final String SQL_COUNT_PACKETS_NOT_SENT = 
		"SELECT COUNT(*) FROM " + PacketsDbHelper.TABLE_NAME + " " +
		"WHERE " + PacketsDbHelper.COLUMN_STATUS + " = ? ";
	
	private static final String SQL_ADD_COORDS_TO_PACKET =
		"UPDATE " + CoordinatesDbHelper.TABLE_NAME + " " +
		"SET " + CoordinatesDbHelper.COLUMN_PACKET_ID + " = ? " +
		"WHERE " + CoordinatesDbHelper.COLUMN_PACKET_ID + " IS NULL ";
	
	private static final String SQL_CLEAN_PROCESSED =
		"UPDATE " + PacketsDbHelper.TABLE_NAME + " " +
		"SET " + PacketsDbHelper.COLUMN_STATUS + " = ? " +
		"WHERE " + PacketsDbHelper.COLUMN_STATUS + " = ? ";

	
	public TrackerServiceDao(Context ctx) {
		super(ctx);
	}
			
	synchronized public void cleanProcessed() throws NotAvailableException {

		Log.d(LOG_TAG, "Cleaning up the database...");
		getDatabase().beginTransaction();
		try {
			Cursor c = getDatabase().rawQuery(
					SQL_CLEAN_PROCESSED, 
					new String[] { 
						String.valueOf(PacketsDbHelper.STATUS_CREATED), 
						String.valueOf(PacketsDbHelper.STATUS_PROCESSED) });
			c.moveToFirst(); // Niezbedy aby zakomitowac transakcje
			c.close();
			getDatabase().setTransactionSuccessful();
			Log.d(LOG_TAG, "Clean.");
		} finally {
			getDatabase().endTransaction();		
		}
	}
	
	synchronized public Coordinates createCoordinates(double latitude, double longitude) throws NotAvailableException {
		
		try {
			long timestamp = new Date().getTime();
			ContentValues cval = new ContentValues();
			cval.put(CoordinatesDbHelper.COLUMN_LATITUDE, latitude);
			cval.put(CoordinatesDbHelper.COLUMN_LONGITUDE, longitude);
			cval.put(CoordinatesDbHelper.COLUMN_TIMESTAMP, timestamp);
			long insertedId = getDatabase().insert(CoordinatesDbHelper.TABLE_NAME, null, cval);
			return new Coordinates(insertedId, latitude, longitude, timestamp);
		} finally {

		}
	}
	
	synchronized public long countNotPackaged() throws NotAvailableException {
		
		try {
			Cursor cur = getDatabase().rawQuery(SQL_COUNT_UNPACKAGED, new String[] {});		
			cur.moveToFirst();
			long count = cur.getInt(0);		
			cur.close();
			Log.v(LOG_TAG, "Not packaged coords count: " + count);
			return count;
		} finally {

		}
	}
	
	synchronized public long countPackages(int status) throws NotAvailableException {

		try {
			Cursor cur = getDatabase().rawQuery(SQL_COUNT_PACKETS_NOT_SENT, new String[] { String.valueOf(status) });
			cur.moveToFirst();
			long count = cur.getInt(0);
			cur.close();
			return count;
		} finally {

		}		
	}

	synchronized public List<Coordinates> findCoordinatesForPacket(long packetId) throws NotAvailableException {
		
		try {
			List<Coordinates> result = new ArrayList<Coordinates>();
			Cursor curr = getDatabase().query(
				CoordinatesDbHelper.TABLE_NAME, null,
				CoordinatesDbHelper.COLUMN_PACKET_ID + " = ?", 
				new String[] { String.valueOf(packetId) }, 
				null, null, null);
			Log.v(LOG_TAG, "Cursor size: " + curr.getCount());
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
		} finally {

		}
	}
	
	synchronized public Packet createPacket(int packetSize) throws NotAvailableException {
		
		try {
			ContentValues val = new ContentValues();
			long timestamp = new Date().getTime();
			val.put(PacketsDbHelper.COLUMN_SIZE, packetSize);
			val.put(PacketsDbHelper.COLUMN_CREATED, timestamp);	
			val.put(PacketsDbHelper.COLUMN_STATUS, PacketsDbHelper.STATUS_CREATED);
			long id = this.getDatabase().insert(PacketsDbHelper.TABLE_NAME, null, val);
			return new Packet(id, packetSize, timestamp, PacketsDbHelper.STATUS_CREATED);
		} finally {

		}		
	}
		
	synchronized public int createPackets(int packetSize) throws NotAvailableException {
		
		try {
			Log.d(LOG_TAG, "creatingPackets");
			getDatabase().beginTransaction();
			int counter = 0;
			while (countNotPackaged() >= packetSize) { 
				Packet packet = createPacket(packetSize);
				// TODO: Tu powinien nastepowac podzial na pakiety o odpowiedniej wielkosci, a nie wszystko jak leci
				// Czy da sie to zaimplementowac w SQLite?
				Cursor c = getDatabase().rawQuery(
						SQL_ADD_COORDS_TO_PACKET, 
						new String[] { String.valueOf(packet.getId()) });
				c.moveToFirst(); // Niezbedy aby zakomitowac transakcje
				c.close();
				counter++;
			}
			Log.v(LOG_TAG, counter + " packet(s) created.");
			getDatabase().setTransactionSuccessful();
			return counter;
		} finally {
			getDatabase().endTransaction();	
		}
	}
	
	synchronized public Packet findFirstPendingPacket() throws NotAvailableException {
		
		Log.d(LOG_TAG, "Finding first pending packet and marking it as being processed");
		getDatabase().beginTransaction();
		try {
			Cursor cursor = getDatabase().query(
				PacketsDbHelper.TABLE_NAME, 
				new String[] { 
					PacketsDbHelper.COLUMN_ID,
					PacketsDbHelper.COLUMN_CREATED,
					PacketsDbHelper.COLUMN_SIZE,
					PacketsDbHelper.COLUMN_STATUS }, 
					PacketsDbHelper.COLUMN_STATUS + " = " + PacketsDbHelper.STATUS_CREATED + 
					" LIMIT 1", 
				null, null, null, null);
			
			if (cursor.getCount() == 0) { 
				Log.d(LOG_TAG, "No pending packets. Exiting.");
				return null; 
			}		
			Log.d(LOG_TAG, "Pending packet found. ");
			cursor.moveToFirst();
			Packet packet = new Packet(
				cursor.getLong(cursor.getColumnIndex(PacketsDbHelper.COLUMN_ID)), 
				cursor.getInt(cursor.getColumnIndex(PacketsDbHelper.COLUMN_SIZE)), 
				cursor.getLong(cursor.getColumnIndex(PacketsDbHelper.COLUMN_CREATED)),
				cursor.getInt(cursor.getColumnIndex(PacketsDbHelper.COLUMN_STATUS)));
			int packetId = cursor.getInt(cursor.getColumnIndex(PacketsDbHelper.COLUMN_ID));
			cursor.close();
			
			Log.v(LOG_TAG, "Setting packet as being processed...");
			ContentValues cv = new ContentValues();
			cv.put(PacketsDbHelper.COLUMN_STATUS, PacketsDbHelper.STATUS_PROCESSED);
			getDatabase().update(
					PacketsDbHelper.TABLE_NAME, cv, PacketsDbHelper.COLUMN_ID + " = ?", 
					new String[] { String.valueOf(packetId) });
			
			packet.setCoordinates(findCoordinatesForPacket(packet.getId()));
			getDatabase().setTransactionSuccessful();
			Log.v(LOG_TAG, "Set as being processed.");
			return packet;
		} finally {
			getDatabase().endTransaction();
		}
	}
	
	synchronized public void markPacketCreated(long packetId) throws NotAvailableException {
		
		Log.d(LOG_TAG, "Marking packet as created to resend...");
		getDatabase().beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put(PacketsDbHelper.COLUMN_STATUS, PacketsDbHelper.STATUS_CREATED);
			getDatabase().update(PacketsDbHelper.TABLE_NAME, cv, PacketsDbHelper.COLUMN_ID + " = ?", new String[] { String.valueOf(packetId) });
			getDatabase().setTransactionSuccessful();
			Log.v(LOG_TAG, "Marked packet as created.");
		} finally {
			getDatabase().endTransaction();
		}
	}
	
	synchronized public void markPacketSent(long packetId) throws NotAvailableException {

		getDatabase().beginTransaction();
		try {
			getDatabase().delete(CoordinatesDbHelper.TABLE_NAME, CoordinatesDbHelper.COLUMN_PACKET_ID + " = ?", new String[] { String.valueOf(packetId) });
			ContentValues cv = new ContentValues();
			cv.put(PacketsDbHelper.COLUMN_SENT, new Date().getTime());
			cv.put(PacketsDbHelper.COLUMN_STATUS, PacketsDbHelper.STATUS_SENT);
			getDatabase().update(PacketsDbHelper.TABLE_NAME, cv, PacketsDbHelper.COLUMN_ID + " = ?", new String[] { String.valueOf(packetId) });
			getDatabase().setTransactionSuccessful();
			Log.v(LOG_TAG, "Packet: " + packetId + " marked as sent.");
		} finally {
			getDatabase().endTransaction();
		}
	}

}
