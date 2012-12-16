package pl.jedenpies.web.traces.model.domain;

public class Location {

	private double latitude;
	private double longitude;
	
	public Location(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	
	public String toString() {
		return "(" + (Math.round(latitude * 100) / 100) + "," + (Math.round(longitude * 100) / 100) + ")";
	}
}
