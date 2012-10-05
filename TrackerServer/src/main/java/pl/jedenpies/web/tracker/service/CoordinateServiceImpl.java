package pl.jedenpies.web.tracker.service;

import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jedenpies.web.tracker.Location;
import pl.jedenpies.web.tracker.dao.CoordinateDao;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;

@Service("coordinateService")
public class CoordinateServiceImpl implements CoordinateService {

	@Autowired
	private CoordinateDao dao;
	
	@Transactional
	public Coordinate createCoordinate(Double longitude, Double latitude, Long timestamp) {
		Coordinate coordinate = new Coordinate();
		coordinate.setLatitude(latitude);
		coordinate.setLongitude(longitude);
		coordinate.setTimestamp(timestamp);
		coordinate.setId(dao.save(coordinate));
		return coordinate;
	}
	
	@Transactional
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner) {
		return dao.findCoordinates(lowCorner, highCorner);
	}
	
}
