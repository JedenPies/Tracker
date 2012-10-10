package pl.jedenpies.web.tracker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jedenpies.web.tracker.dao.UserDao;
import pl.jedenpies.web.tracker.model.domain.UserInfo;
import pl.jedenpies.web.tracker.model.hibernate.User;
import pl.jedenpies.web.tracker.service.UserService;
import pl.jedenpies.web.tracker.service.assembler.UserAssembler;

@Transactional @Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	private ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
	
	@Override
	public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userDao.loadByUsername(username);
		if (user == null) throw new UsernameNotFoundException("Cannot find user: " + username);
		return UserAssembler.transform(user);
	}
	
	@Override
	public boolean isAvailable(String username) {
		return userDao.loadByUsername(username) == null;
	}

	@Override
	public UserInfo registerUser(String username, String password, String email) {
		
		User anotherUser = userDao.loadByUsername(username);
		if (anotherUser != null) 
			return null;
		
		User user = new User();
		user.setPassword(passwordEncoder.encodePassword(password, username));
		user.setUsername(username);
		user.setEmail(email);
		userDao.create(user);
		return UserAssembler.transform(user);
		
	}
}
