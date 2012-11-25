package pl.jedenpies.web.tracker.controller.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.model.json.SimpleJSONResponse;
import pl.jedenpies.web.tracker.model.json.UserLoginForm;
import pl.jedenpies.web.tracker.utils.UserUtils;

@Controller
@RequestMapping("user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserUtils userUtils;
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody SimpleJSONResponse loginUser(@RequestBody UserLoginForm loginForm) {
			
		LOG.debug("Trying to authenticate user " + loginForm.getUsername());
		boolean result = userUtils.authenticate(loginForm.getUsername(), loginForm.getPassword());
		if (result) return SimpleJSONResponse.RESPONSE_OK;
		return SimpleJSONResponse.RESPONSE_NOK;
	}
		
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public @ResponseBody SimpleJSONResponse logoutUser() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return SimpleJSONResponse.RESPONSE_OK;
	}
}
