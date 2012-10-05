package pl.jedenpies.web.tracker.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller("welcomePageController")
@RequestMapping({"/", "/main"})
public class WelcomePageController {

	@RequestMapping
	public String showPage() {
		return "main";
	}	
}
