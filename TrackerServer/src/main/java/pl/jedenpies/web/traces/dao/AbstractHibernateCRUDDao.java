package pl.jedenpies.web.traces.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public class AbstractHibernateCRUDDao<T, I extends Serializable> extends AbstractHibernateDao {

	private Class<T> myClass;
	
	@SuppressWarnings("unchecked")
	public AbstractHibernateCRUDDao() {
		this.myClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	}
	
	public T create(T obj) {
		getCurrentSession().save(obj);
		return obj;		
	}
	
	@SuppressWarnings("unchecked")
	public T read(T obj, I id) {		
		return (T) getCurrentSession().load(myClass, id);
	}
	
	public void update(T obj) {
		getCurrentSession().update(obj);
	}
	
	public void delete(T obj) {
		getCurrentSession().delete(obj);
	}
}
