package pl.jedenpies.web.tracker.dao;

import org.hibernate.ScrollableResults;

import pl.jedenpies.web.tracker.Location;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;

public interface CoordinateDao {

	public Long save(Coordinate coordinate);
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner);
}
