package pl.jedenpies.web.tracker.controller.mobile;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("checkStatus")
public class CheckStatusController {

	@RequestMapping
	public @ResponseBody SimpleJSONResponse checkStatus() {
		System.out.println("[S]: " + (new Date()));
		SimpleJSONResponse response = SimpleJSONResponse.RESPONSE_OK;
		return response;
	}
	
}
