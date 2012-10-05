package pl.jedenpies.web.tracker.dao;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.jedenpies.web.tracker.Location;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;

@Repository("hibernateCoordinateDao")
public class HibernateCoordinateDao extends AbstractHibernateDao implements CoordinateDao {

	@Override
	public Long save(Coordinate coordinate) {
		Long id = (Long) getCurrentSession().save(coordinate);
		return id;
	}

	@Override
	public ScrollableResults findCoordinates(Location lowCorner, Location highCorner) {
		Criteria c = getCurrentSession().createCriteria(Coordinate.class);
		c.add(Restrictions.between("latitude", lowCorner.getLatitude(), highCorner.getLatitude()));
		c.add(Restrictions.between("longitude", lowCorner.getLongitude(), highCorner.getLatitude()));
		return c.scroll();
	}

}
