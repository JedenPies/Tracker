package pl.jedenpies.web.traces.dao;

import org.hibernate.ScrollableResults;

import pl.jedenpies.web.traces.model.domain.Location;
import pl.jedenpies.web.traces.model.hibernate.Coordinate;

public interface CoordinateDao {

	public Long save(Coordinate coordinate);
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner);
	public int findCoordinatesCount(Location lowCorner, Location highCorner);
}
