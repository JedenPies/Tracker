package pl.jedenpies.web.traces.service.impl;

import java.util.Date;

import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jedenpies.web.traces.dao.CoordinateDao;
import pl.jedenpies.web.traces.model.domain.Location;
import pl.jedenpies.web.traces.model.hibernate.Coordinate;
import pl.jedenpies.web.traces.service.CoordinateService;

@Transactional @Service
public class CoordinateServiceImpl implements CoordinateService {

	@Autowired
	private CoordinateDao dao;
	
	public Coordinate createCoordinate(Double longitude, Double latitude, Long timestamp) {
		
		Coordinate coordinate = new Coordinate();
		coordinate.setLatitude(latitude);
		coordinate.setLongitude(longitude);
		coordinate.setTimestamp(new Date(timestamp));
		coordinate.setId(dao.save(coordinate));
		return coordinate;
	}
	
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner) {
		return dao.findCoordinates(lowCorner, highCorner);
	}

	@Override
	public int findCoordinatesCount(Location lowCorner, Location highCorner) {
		int result = dao.findCoordinatesCount(lowCorner, highCorner);
		return result;
		
	}
	
}
