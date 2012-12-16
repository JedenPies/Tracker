package pl.jedenpies.web.traces.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("welcomePageController")
@RequestMapping
public class WelcomePageController {

	@RequestMapping({"/", "/about"})
	public String showAboutPage() {
		return "about";
	}
	
	@RequestMapping({"/download"})
	public String showDownloadPage() {
		return "download";
	}
	
	@RequestMapping({"/desc"})
	public String showDescPage() {
		return "description";
	}
}
