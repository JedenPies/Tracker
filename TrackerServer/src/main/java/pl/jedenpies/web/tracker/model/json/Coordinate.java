package pl.jedenpies.web.tracker.model.json;

public class Coordinate {

	private double latitude;
	private double longitude;
	private long   timestamp;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double lat) {
		this.latitude = lat;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double lng) {
		this.longitude = lng;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long tim) {
		this.timestamp = tim;
	}
}
