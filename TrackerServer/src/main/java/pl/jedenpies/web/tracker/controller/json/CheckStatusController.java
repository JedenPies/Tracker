package pl.jedenpies.web.tracker.controller.json;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("checkStatusController")
@RequestMapping("checkStatus")
public class CheckStatusController {

	@RequestMapping
	public String checkStatus() {
		System.out.println("[S]: " + (new Date()));
		return "status_ok";
	}
	
}
