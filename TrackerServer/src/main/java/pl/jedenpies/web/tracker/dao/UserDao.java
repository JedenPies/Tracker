package pl.jedenpies.web.tracker.dao;

import pl.jedenpies.web.tracker.model.hibernate.User;

public interface UserDao {

	public User create(User user);

	public User loadByUsername(String username);
}
