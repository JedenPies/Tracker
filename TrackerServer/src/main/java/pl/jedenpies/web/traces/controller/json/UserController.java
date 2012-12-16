package pl.jedenpies.web.traces.controller.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.traces.beans.CaptchaHandler;
import pl.jedenpies.web.traces.model.domain.UserInfo;
import pl.jedenpies.web.traces.model.json.SimpleJSONResponse;
import pl.jedenpies.web.traces.model.json.UserLoginForm;
import pl.jedenpies.web.traces.model.json.UserRegisterForm;
import pl.jedenpies.web.traces.service.UserService;
import pl.jedenpies.web.traces.utils.UserUtils;
import pl.jedenpies.web.traces.validation.SyntaxValidator;

@Controller
@RequestMapping("user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	private static final String RM_USERNAME_INVALID = "USERNAME_INVALID";
	private static final String RM_USERNAME_EXISTS = "USERNAME_EXISTS";
	private static final String RM_PASSWORD_INVALID = "PASSWORD_INVALID";	
	private static final String RM_PASSWORDS_DIFF = "PASSWORDS_DIFF";
	private static final String RM_EMAIL_INVALID = "EMAIL_INVALID";
	private static final String RM_CAPTCHA_INVALID = "CAPTCHA_INVALID";
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CaptchaHandler captchaHandler;
	
	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
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
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public @ResponseBody SimpleJSONResponse register(@RequestBody UserRegisterForm userForm) {
		
		LOG.debug("User registration: " + userForm.getUsername() + "[" + userForm.getEmail() + "]");
		String validationResult = validateForm(userForm);
		if (validationResult != null) {
			LOG.debug("User registration validation: " + validationResult);
			return reject(validationResult);
		}
		UserInfo user = userService.registerUser(userForm.getUsername(), userForm.getPassword(), userForm.getEmail());
		if (user != null) {
			LOG.debug("User registered: " + user.getUsername());
			return accept();
		}
		return reject(null);
	}
		
	@RequestMapping(value = "isAvailable/{username}", method = RequestMethod.GET)
	public @ResponseBody SimpleJSONResponse checkAvailable(@PathVariable("username") String username) {
		LOG.debug("Checking username availability (" + username + ")");
		if (userService.isAvailable(username)) return SimpleJSONResponse.RESPONSE_OK;	
		return SimpleJSONResponse.RESPONSE_NOK;
	}
	@RequestMapping(value = "isAvailable", method = RequestMethod.GET)
	public @ResponseBody SimpleJSONResponse checkAvailable2(@RequestParam("username") String username) {
		return checkAvailable(username);
	}
	
	
	private SimpleJSONResponse accept() {
		return SimpleJSONResponse.RESPONSE_OK;
	}
	private SimpleJSONResponse reject(String message) {
		return new SimpleJSONResponse(0, message);
	}
		
	private String validateForm(UserRegisterForm form) {
	
		if (!SyntaxValidator.USERNAME_VALIDATOR.validate(form.getUsername()))  { return RM_USERNAME_INVALID; }
		if (!userService.isAvailable(form.getUsername())) { return RM_USERNAME_EXISTS; }
		if (form.getEmail().length() > 0 && !SyntaxValidator.EMAIL_VALIDATOR.validate(form.getEmail())) { return RM_EMAIL_INVALID; }
		if (!SyntaxValidator.PASSWORD_VALIDATOR.validate(form.getPassword()))  { return RM_PASSWORD_INVALID; }
		if (!form.getPassword().equals(form.getPassword2())) { return RM_PASSWORDS_DIFF; }
		if (!captchaHandler.isCorrect(form.getCaptchaAnswer())) { return RM_CAPTCHA_INVALID; }
		return null;
	}
}
