package pl.jedenpies.android.tracker.db;

import android.os.AsyncTask;
import android.util.Log;

public class AddCoordinatesTask extends AsyncTask<Double, Void, Void> {

	private static final String LOGGER_NAME = AddCoordinatesTask.class.getName();
	
	private TrackerServiceDao dao;

	public AddCoordinatesTask(TrackerServiceDao dao) {
		this.dao = dao;
	}

	public void doJob(Double... params) {
		
		try {			
			double latitude = params[0];
			double longitude = params[1];
			Log.v(LOGGER_NAME, "new coordinates: " + latitude + "|" + longitude);
			dao.createCoordinates(latitude, longitude);	
			dao.createPackets(50);
		} catch (NotAvailableException e) {
			Log.w(AddCoordinatesTask.class.getCanonicalName(), "Cannot finish job. Database unavailable.");
			e.printStackTrace();
		}
	}
	
	@Override
	protected Void doInBackground(Double... params) {		
		doJob(params);
		return null;
	}

}
