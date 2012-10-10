package pl.jedenpies.web.tracker.service.impl;

import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jedenpies.web.tracker.Location;
import pl.jedenpies.web.tracker.dao.CoordinateDao;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;
import pl.jedenpies.web.tracker.service.CoordinateService;

@Transactional
@Service("coordinateService")
public class CoordinateServiceImpl implements CoordinateService {

	@Autowired
	private CoordinateDao dao;
	
	public Coordinate createCoordinate(Double longitude, Double latitude, Long timestamp) {
		Coordinate coordinate = new Coordinate();
		coordinate.setLatitude(latitude);
		coordinate.setLongitude(longitude);
		coordinate.setTimestamp(timestamp);
		coordinate.setId(dao.save(coordinate));
		return coordinate;
	}
	
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner) {
		return dao.findCoordinates(lowCorner, highCorner);
	}
	
}
