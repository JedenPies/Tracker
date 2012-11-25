package pl.jedenpies.web.tracker.controller.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.model.json.SimpleJSONResponse;

/**
 * Checks status of server. 
 * @author Patryk Dobrowolski
 *
 */
@Controller
@RequestMapping("status")
public class CheckStatusController {

	private static final Logger LOG = LoggerFactory.getLogger(CheckStatusController.class);
	
	/**
	 * Checks status.
	 * Returns {@link SimpleJSONResponse} object as response. 
	 * @return <code>RESPONSE_OK</code> if server is running. 
	 */
	@RequestMapping
	public @ResponseBody SimpleJSONResponse checkStatus() {
		SimpleJSONResponse response = SimpleJSONResponse.RESPONSE_OK;
		LOG.debug("Status is beign checked... OK");
		return response;
	}
	
}
