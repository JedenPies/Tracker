package pl.jedenpies.web.tracker.model.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "coordinate")
@Table(name = "coordinates")
@SequenceGenerator(name = "COORDINATE_SEQ", sequenceName = "coordinates_seq", allocationSize = 1)
public class Coordinate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COORDINATE_SEQ")
	private Long id;
	private Double longitude;
	private Double latitude;
	private Long timestamp;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
