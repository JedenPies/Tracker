package pl.jedenpies.web.traces.controller.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.jedenpies.web.traces.model.domain.AreaType;
import pl.jedenpies.web.traces.utils.AreaFileLocationProvider;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/map")
public class MapController {

	private static final Logger LOG = LoggerFactory.getLogger(MapController.class);
	
	@Autowired
	private AreaFileLocationProvider fileProvider;
	
	@RequestMapping
	public String map() {
		return "map";
	}
	
	@RequestMapping(value = "/area/{level}/{x}/{y}.img", produces = MediaType.IMAGE_PNG_VALUE, method = RequestMethod.GET)
	public void area(
			@PathVariable("level") int level,
			@PathVariable("x") int x,
			@PathVariable("y") int y,
			HttpServletResponse response) throws IOException {
		
		LOG.trace("Displaying area: " + x + ", " + y + ", LEVEL" + level);
		AreaType areaType = AreaType.getByName("LEVEL" + level);
		File file = fileProvider.findFile(x, y, areaType);
		if (!file.exists()) file = fileProvider.findDefault();
		FileInputStream fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, response.getOutputStream());
	}

}
