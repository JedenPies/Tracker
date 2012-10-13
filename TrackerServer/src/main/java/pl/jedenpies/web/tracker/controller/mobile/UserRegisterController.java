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

@Controller
@RequestMapping("user")
public class UserRegisterController {

	@Autowired
	private CaptchaHandler captchaHandler;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "isAvailable/{username}", method = RequestMethod.GET)
	public @ResponseBody SimpleJSONResponse checkAvailable(@PathVariable("username") String username) {		
		if (userService.isAvailable(username)) return SimpleJSONResponse.RESPONSE_OK;	
		return SimpleJSONResponse.RESPONSE_NOK;
	}
	
	@RequestMapping(value = "getCaptcha", method = RequestMethod.GET)
	@ResponseBody
	public void getCaptcha(HttpServletResponse response) {
		try {
			captchaHandler.create();
			response.setContentType("image/png");
			BufferedImage image = captchaHandler.getImage();
			ImageIO.write(image, "PNG", response.getOutputStream());
			
		} catch (IOException e) {}
	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public @ResponseBody SimpleJSONResponse register(@RequestBody UserRegisterForm userForm) {
		
		System.out.println("Proba rejestracji");
		if (!userForm.getPassword().equals(userForm.getPassword2())) return reject("temp: passwords");
		if (!captchaHandler.isCorrect(userForm.getCaptchaAnswer())) return reject("temp: captcha");
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
}
