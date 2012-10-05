package pl.jedenpies.web.tracker.controller.web;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.jedenpies.web.tracker.Location;
import pl.jedenpies.web.tracker.service.PaintMapService;

@Controller
@RequestMapping("/drawMap/{ltcLatitude}/{ltcLongitude}/{rbcLatitude}/{rbcLongitude}/coords")
public class PaintMapController {

	@Autowired
	private PaintMapService mapService;
	
	@RequestMapping
	@ResponseBody
	public BufferedImage drawMap(
			@PathVariable("ltcLatitude")  Double ltcLatitude,
			@PathVariable("ltcLongitude") Double ltcLongitude,
			@PathVariable("rbcLatitude")  Double rbcLatitude,
			@PathVariable("rbcLongitude") Double rbcLongitude,
			HttpServletResponse response
			) {
		
		BufferedImage image;
		try {
			double lLat = Math.min(ltcLatitude, rbcLatitude) - 0.01;
			double lLon = Math.min(ltcLongitude, rbcLongitude) - 0.01;
			double hLat = Math.max(ltcLatitude, rbcLatitude) + 0.01;
			double hLon = Math.max(ltcLongitude, rbcLongitude) + 0.01;
			Location lowCorner = new Location(lLat, lLon);
			Location highCorner = new Location(hLat, hLon);
			System.out.println("" + lLon + " " + hLon);
			image = mapService.drawArea(lowCorner, highCorner);
			ImageIO.write(image, "PNG", new File("map.png"));
			System.out.println("written image");
			response.setContentType("image/png");
			FileCopyUtils.copy(new FileInputStream(new File("map.png")), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
