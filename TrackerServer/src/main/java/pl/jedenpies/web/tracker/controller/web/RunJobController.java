package pl.jedenpies.web.tracker.controller.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jedenpies.web.tracker.utils.impl.DrawMapsJob;

@Controller
@RequestMapping("/runJob")
public class RunJobController {

	@Autowired
	private DrawMapsJob mapsJob;
	
	@RequestMapping(method = RequestMethod.GET)
	public String runJob() {
		
		try {
			mapsJob.doJob();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		return "main";
	}
}
