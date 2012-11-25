package pl.jedenpies.web.tracker.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.model.json.SimpleJSONResponse;
import pl.jedenpies.web.tracker.utils.UserUtils;

@Controller
@RequestMapping("user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserUtils userUtils;
	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody SimpleJSONResponse login(
			@RequestParam String username,
			@RequestParam String password) {

		LOG.debug("Trying to authenticate user " + username);
		boolean result = userUtils.authenticate(username, password);
		if (result) return SimpleJSONResponse.RESPONSE_OK;
		return SimpleJSONResponse.RESPONSE_NOK;
	}
}
