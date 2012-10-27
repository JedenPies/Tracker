package pl.jedenpies.web.tracker.controller.mobile;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.beans.CaptchaHandler;
import pl.jedenpies.web.tracker.model.domain.UserInfo;
import pl.jedenpies.web.tracker.model.json.UserRegisterForm;
import pl.jedenpies.web.tracker.service.UserService;
import pl.jedenpies.web.tracker.validation.SyntaxValidator;

@Controller
@RequestMapping("user")
public class UserRegisterController {

//	private static final Logger LOGGER = Logger.getLogger(UserRegisterController.class);
	
	@Autowired
	private CaptchaHandler captchaHandler;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "isAvailable/{username}", method = RequestMethod.GET)
	public @ResponseBody SimpleJSONResponse checkAvailable(@PathVariable("username") String username) {
//		LOGGER.debug("Checking username availability");
		if (userService.isAvailable(username)) return SimpleJSONResponse.RESPONSE_OK;	
		return SimpleJSONResponse.RESPONSE_NOK;
	}
	
	@RequestMapping(value = "getCaptcha", method = RequestMethod.GET)
	@ResponseBody
	public void getCaptcha(HttpServletResponse response) {
//		LOGGER.debug("Creating captcha");
		try {
			System.out.println("getting captcha");
			captchaHandler.create();
			response.setContentType("image/png");
			BufferedImage image = captchaHandler.getImage();
			ImageIO.write(image, "PNG", response.getOutputStream());
			
		} catch (IOException e) {}
	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public @ResponseBody SimpleJSONResponse register(@RequestBody UserRegisterForm userForm) {
		
//		LOGGER.debug("User registration: " + userForm.getUsername() + "[" + userForm.getEmail() + "]");
		String validationResult = validateForm(userForm);
		if (validationResult != null) return reject(validationResult);
		UserInfo user = userService.registerUser(userForm.getUsername(), userForm.getPassword(), userForm.getEmail());
		if (user != null) return accept();
		return reject(null);
	}
	
	private SimpleJSONResponse accept() {
		return SimpleJSONResponse.RESPONSE_OK;
	}
	private SimpleJSONResponse reject(String message) {
		return new SimpleJSONResponse(message);
	}
		
	private String validateForm(UserRegisterForm form) {
	
		if (!SyntaxValidator.USERNAME_VALIDATOR.validate(form.getUsername()))  { return "Invalid username"; }
		if (!SyntaxValidator.PASSWORD_VALIDATOR.validate(form.getPassword()))  { return "Invalid password"; }
		if (!SyntaxValidator.PASSWORD_VALIDATOR.validate(form.getPassword2())) { return "Invalid password"; }
		if (!form.getPassword().equals(form.getPassword2())) { return "Paswords do not match"; }
		if (!SyntaxValidator.EMAIL_VALIDATOR.validate(form.getEmail())) { return "Invalid e-mail address"; }
		if (!captchaHandler.isCorrect(form.getCaptchaAnswer())) { return "Invalid captcha answer"; }
		if (!userService.isAvailable(form.getUsername())) { return "Username already exists"; }
		return null;
	}
}
