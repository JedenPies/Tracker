package pl.jedenpies.web.traces.dao;

import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.jedenpies.web.traces.model.domain.Location;
import pl.jedenpies.web.traces.model.hibernate.Coordinate;

@Repository("hibernateCoordinateDao")
public class HibernateCoordinateDao extends AbstractHibernateDao implements CoordinateDao {

	@Override
	public Long save(Coordinate coordinate) {
		Long id = (Long) getCurrentSession().save(coordinate);
		return id;
	}

	@Override
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner) {
		return getCurrentSession().createCriteria(Coordinate.class)
			.add(Restrictions.gt("latitude", lowCorner.getLatitude()))
			.add(Restrictions.lt("latitude", highCorner.getLatitude()))
			.add(Restrictions.gt("longitude", lowCorner.getLongitude()))
			.add(Restrictions.lt("longitude", highCorner.getLongitude()))
			.scroll();
	}

	@Override
	public int findCoordinatesCount(Location lowCorner, Location highCorner) {
		return (Integer) getCurrentSession().createCriteria(Coordinate.class)
				.add(Restrictions.gt("latitude", lowCorner.getLatitude()))
				.add(Restrictions.lt("latitude", highCorner.getLatitude()))
				.add(Restrictions.gt("longitude", lowCorner.getLongitude()))
				.add(Restrictions.lt("longitude", highCorner.getLongitude()))
				.setProjection(Projections.rowCount())
				.uniqueResult();
	}

}
