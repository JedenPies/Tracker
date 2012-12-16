package pl.jedenpies.web.traces.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pl.jedenpies.web.traces.model.domain.UserInfo;

@Component
public class UserUtils {

	private static final Logger LOG = LoggerFactory.getLogger(UserUtils.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public boolean authenticate(String username, String password) {
		
		try {
			UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(username, password);
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(username);
			token.setDetails(userInfo);		
			Authentication auth = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
			LOG.debug("User " + username + " authenticated.");			
			return true;
			
		} catch (BadCredentialsException e) {
			LOG.debug("User " + username + " not authenticated. Bad credentials.");
			return false;
		}		
	}
	
}
