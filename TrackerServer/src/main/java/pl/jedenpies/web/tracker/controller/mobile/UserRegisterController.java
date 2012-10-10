package pl.jedenpies.web.tracker.controller.mobile;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.beans.CaptchaHandler;
import pl.jedenpies.web.tracker.model.domain.UserInfo;
import pl.jedenpies.web.tracker.service.UserService;

@Controller
@RequestMapping("user")
public class UserRegisterController {

	@Autowired
	private CaptchaHandler captchaHandler;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "isAvailable/{username}", method = RequestMethod.GET)
	public String checkAvailable(@PathVariable("username") String username) {		
		if (userService.isAvailable(username)) return accept();	
		return reject();
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
	public String register(
			@RequestParam("username") String username, 
			@RequestParam("password") String password, 
			@RequestParam("password2") String password2, 
			@RequestParam("captchaAnswer") String captchaAnswer,
			@RequestParam(value = "email", required = false) String email) {
		
		if (!password.equals(password2)) return reject();
		if (!captchaHandler.isCorrect(captchaAnswer)) return reject();
		UserInfo user = userService.registerUser(username, password, email);
		if (user != null) return accept();
		return reject();
	}
	
	private String accept() {
		return "mobile/status_ok";
	}
	private String reject() {
		return "mobile/status_nok";
	}
}
