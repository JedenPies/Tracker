package pl.jedenpies.web.tracker.service.assembler;

import pl.jedenpies.web.tracker.model.domain.UserInfo;
import pl.jedenpies.web.tracker.model.hibernate.User;

public class UserAssembler {

	public static UserInfo transform(User user) {
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(user.getUsername());
		userInfo.setPassword(user.getPassword());
		userInfo.setEmail(user.getEmail());
		return userInfo;
	}
	
	public static User transform(UserInfo userInfo) {
		User user = new User();
		user.setUsername(userInfo.getUsername());
		user.setPassword(userInfo.getPassword());
		user.setEmail(userInfo.getEmail());
		return user;
	}
}
