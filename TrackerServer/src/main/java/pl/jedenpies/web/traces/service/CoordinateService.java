package pl.jedenpies.web.traces.service;

import org.hibernate.ScrollableResults;

import pl.jedenpies.web.traces.model.domain.Location;
import pl.jedenpies.web.traces.model.hibernate.Coordinate;

public interface CoordinateService {

	/**
	 * Creates coordinate in database basing on longitude, latitude and timestamp.
	 * @param longitude longitude
	 * @param latitude latitude
	 * @param timestamp time when user taken given coordinates
	 * @return coordinates object created
	 */
	public Coordinate createCoordinate(Double longitude, Double latitude, Long timestamp);
	
	/**
	 * Finds all coordinates from area given by low left down corner and up right corner 
	 * @param lowCorner left down corner location
	 * @param highCorner right up corner location
	 * @return scrollable result of coordinates
	 */
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner);
	public int findCoordinatesCount(Location lowCorner, Location highCorner);
}
