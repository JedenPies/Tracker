package pl.jedenpies.web.tracker.controller.mobile;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.model.json.Coordinate;
import pl.jedenpies.web.tracker.model.json.Packet;
import pl.jedenpies.web.tracker.service.CoordinateService;

@Controller("sendPacketController")
@RequestMapping("sendPacket")
public class SendPacketController {

	@Autowired
	private CoordinateService coordService;
	
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Packet sendPacket(@RequestBody Packet packet) {	
		for (Coordinate c : packet.getCoordinates()) {			
			coordService.createCoordinate(c.getLongitude(), c.getLatitude(), c.getTimestamp());
		}
		System.out.println("[P]: " + (new Date()));		
		return packet;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String sendPacket() {		
		return "main";
	}
}
