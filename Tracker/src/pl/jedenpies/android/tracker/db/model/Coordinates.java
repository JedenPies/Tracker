package pl.jedenpies.android.tracker.db.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Coordinates {
	
	private static final String LOGGER_NAME = Coordinates.class.getName();
	
	private Long id;
	private Double latitude;
	private Double longitude;
	private Long packetId;
	private Long timestamp;
	
	public Coordinates(long id, double latitude, double longitude) {
		this(id, latitude, longitude, new Date().getTime());
	}
	public Coordinates(long id, double latitude, double longitude, long timestamp) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}
	public Coordinates(long id, double latitude, double longitude, long timestamp, long packetId) {
		this(id, latitude, longitude, timestamp);
		this.packetId = packetId;
	}
	public Long getId() {
		return id;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public Long getPacketId() {
		return packetId;
	}
	public void setPacketId(Long packetId) {
		this.packetId = packetId;
	}
	protected Long getTimestamp() {
		return timestamp;
	}
	
	public JSONObject getJSONObject() {
		JSONObject res = new JSONObject();
		try {
			res.put("latitude", latitude);
			res.put("longitude", longitude);
			res.put("timestamp", timestamp);
		} catch (JSONException e) {
			Log.e(LOGGER_NAME, "Error while making JSON object", e);
		}
		return res;
	}
	
}
