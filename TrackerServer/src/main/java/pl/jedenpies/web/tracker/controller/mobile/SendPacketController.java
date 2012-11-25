package pl.jedenpies.web.tracker.controller.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.model.json.Coordinate;
import pl.jedenpies.web.tracker.model.json.Packet;
import pl.jedenpies.web.tracker.model.json.SimpleJSONResponse;
import pl.jedenpies.web.tracker.service.CoordinateService;

/**
 * Responsible for receiving packets with coordinates as JSON stream.
 * @author Patryk Dobrowolski
 *
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("sendPacket")
public class SendPacketController {

	@Autowired
	private CoordinateService coordService;
	
	private static final Logger LOG = LoggerFactory.getLogger(SendPacketController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody SimpleJSONResponse sendPacket(@RequestBody Packet packet) {
		LOG.debug("Getting packet...");
		try {
			for (Coordinate c : packet.getCoordinates()) {			
				coordService.createCoordinate(c.getLongitude(), c.getLatitude(), c.getTimestamp());
			}
			LOG.debug("Packet saved.");
			return SimpleJSONResponse.RESPONSE_OK;
		} catch (Exception e) {
			LOG.error("Unexpected error during packet getting.", e);
			return new SimpleJSONResponse(0, e.getMessage());
		}
	}
}
