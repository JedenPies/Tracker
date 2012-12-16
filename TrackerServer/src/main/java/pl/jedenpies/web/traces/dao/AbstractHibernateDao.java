package pl.jedenpies.web.traces.dao;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component("abstractHibernateDao")
public abstract class AbstractHibernateDao {

	@Resource(name = "hibernateSessionFactory")
	protected SessionFactory sessionFactory;
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
}
