package pl.jedenpies.web.traces.controller.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jedenpies.web.traces.utils.impl.DrawMapsJob;

/**
 * To jest do wyrzucenia, job nie powinien byc wywolywany przez serwlet.
 * @author Patryk
 *
 */
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
