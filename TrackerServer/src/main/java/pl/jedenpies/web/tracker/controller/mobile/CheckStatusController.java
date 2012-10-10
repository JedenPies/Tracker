package pl.jedenpies.web.tracker.controller.mobile;

import java.util.Date;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("checkStatusController")
@RequestMapping("checkStatus")
//@PreAuthorize("hasRole('usersasdd')")
public class CheckStatusController {

	@RequestMapping
	public String checkStatus() {
		System.out.println("[S]: " + (new Date()));
		return "status_ok";
	}
	
}
