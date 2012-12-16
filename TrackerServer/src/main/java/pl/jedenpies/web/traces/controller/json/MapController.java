package pl.jedenpies.web.traces.controller.json;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.traces.model.domain.AreaType;
import pl.jedenpies.web.traces.model.json.AreaInfo;

@Controller
@RequestMapping("/map")
public class MapController {

	@RequestMapping(value = "/area/info/{level}")
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
