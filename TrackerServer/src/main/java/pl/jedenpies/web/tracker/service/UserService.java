package pl.jedenpies.web.tracker.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import pl.jedenpies.web.tracker.model.domain.UserInfo;

public interface UserService extends UserDetailsService {

	@Override
	public UserInfo loadUserByUsername(String username);
	
	public boolean isAvailable(String username);
	public UserInfo registerUser(String username, String password, String email);
}
