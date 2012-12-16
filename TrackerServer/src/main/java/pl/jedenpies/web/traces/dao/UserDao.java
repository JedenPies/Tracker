package pl.jedenpies.web.traces.dao;

import pl.jedenpies.web.traces.model.hibernate.User;

public interface UserDao {

	public User create(User user);

	public User loadByUsername(String username);
}
