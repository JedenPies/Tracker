package pl.jedenpies.web.tracker.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.jedenpies.web.tracker.model.hibernate.User;

@Repository
public class HibernateUserDao extends AbstractHibernateCRUDDao<User, Long> implements UserDao {

	@Override
	public User loadByUsername(String username) {		
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		User user = (User) criteria.uniqueResult();
		return user;
	}

}
