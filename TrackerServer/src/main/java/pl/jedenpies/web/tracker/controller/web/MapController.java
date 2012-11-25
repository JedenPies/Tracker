package pl.jedenpies.web.tracker.controller.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.model.domain.AreaType;
import pl.jedenpies.web.tracker.model.json.AreaInfo;
import pl.jedenpies.web.tracker.utils.AreaFileLocationProvider;

@Controller
@RequestMapping("/map")
public class MapController {

	private static final Logger LOG = LoggerFactory.getLogger(MapController.class);
	
	@Autowired
	private AreaFileLocationProvider fileProvider;
	
	@RequestMapping
	public String map() {
		return "map/index";
	}
	
	@RequestMapping(value = "/area/{level}/{x}/{y}.img", produces = MediaType.IMAGE_PNG_VALUE, method = RequestMethod.GET)
	public void area(
			@PathVariable("level") int level,
			@PathVariable("x") int x,
			@PathVariable("y") int y,
			HttpServletResponse response) throws IOException {
		
		LOG.debug("Displaying area: " + x + ", " + y + ", LEVEL" + level);
		AreaType areaType = AreaType.getByName("LEVEL" + level);
		File file = fileProvider.findFile(x, y, areaType);
		if (!file.exists()) file = fileProvider.findDefault();
		FileInputStream fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, response.getOutputStream());
	}
	
	@RequestMapping(value = "/area/info/{level}.json")
	public @ResponseBody Object areaInfo(@PathVariable("level") int level) {
		
		AreaType areaType = AreaType.getByName("LEVEL" + level);
		if (areaType == null) {
			return new AreaInfo();
		}
		AreaInfo response = new AreaInfo();
		response.setLevel(level);
		response.setWidth(areaType.getWidth());
		response.setHeight(areaType.getHeight());
		return response;
	}
}
