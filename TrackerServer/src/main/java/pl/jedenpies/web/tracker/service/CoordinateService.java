package pl.jedenpies.web.tracker.service;

import org.hibernate.ScrollableResults;

import pl.jedenpies.web.tracker.Location;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;

public interface CoordinateService {

	public Coordinate createCoordinate(Double longitude, Double latitude, Long timestamp);
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner);
}
