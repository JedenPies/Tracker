package pl.jedenpies.web.traces.controller.web;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.traces.beans.CaptchaHandler;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private CaptchaHandler captchaHandler;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(value = "getCaptcha", method = RequestMethod.GET)
	@ResponseBody
	public void getCaptcha(HttpServletResponse response) {
		LOG.debug("Creating captcha...");
		try {
			captchaHandler.create();
			response.setContentType("image/png");
			BufferedImage image = captchaHandler.getImage();
			ImageIO.write(image, "PNG", response.getOutputStream());			
		} catch (IOException e) {}
	}
}
