package pl.jedenpies.web.tracker.utils.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jedenpies.web.tracker.beans.MapDescriptor;
import pl.jedenpies.web.tracker.model.domain.AreaType;
import pl.jedenpies.web.tracker.model.domain.Location;
import pl.jedenpies.web.tracker.service.PaintingService;
import pl.jedenpies.web.tracker.utils.AreaFileLocationProvider;

@Component
public class DrawMapsJob {

	private static final Logger LOG = LoggerFactory.getLogger(DrawMapsJob.class);
	
	@Autowired
	private PaintingService paintingService;
	
	@Autowired
	private MapDescriptor mapDescriptor;
	
	@Autowired
	private AreaFileLocationProvider fileLocationProvider;
	
	public void doJob() throws IOException {
		try {
			LOG.info("Starting DrawMapsJob");
			for (AreaType areaType : mapDescriptor.getAreaTypes()) {
				paintType(areaType);
			}
		} catch(Exception e) {
			LOG.error("DrawMapsJob interrupted", e);
		} finally {
			LOG.info("DrawMapsJob stopped");
		}
	}
	
	private void paintType(AreaType areaType) throws IOException {
		
		int firstX = (int) Math.round(
				Math.floor(
						mapDescriptor.getLeftBottomCorner().getLongitude() / areaType.getWidth()));
		int firstY = (int) Math.round(
				Math.floor(
						mapDescriptor.getLeftBottomCorner().getLatitude() / areaType.getHeight()));
		int y = firstY;
		
		double latitude = mapDescriptor.getLeftBottomCorner().getLatitude();
		while (latitude < mapDescriptor.getRightTopCorner().getLatitude()) {

			int x = firstX;
			double longitude = mapDescriptor.getLeftBottomCorner().getLongitude();
			while (longitude < mapDescriptor.getRightTopCorner().getLongitude()) {
				
				Location leftBottomCorner = new Location(latitude, longitude);
				Location rightTopCorner = new Location(latitude + areaType.getHeight(), longitude + areaType.getWidth());
				BufferedImage image = paintingService.createPicture(
						leftBottomCorner, rightTopCorner, mapDescriptor.getPicSize());
				File file = fileLocationProvider.findFile(x, y, areaType);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				ImageIO.write(image, mapDescriptor.getFileFormat(), file);			
				longitude = longitude + areaType.getWidth();
				x++;
			}			
			latitude = latitude + areaType.getHeight();
			y++;
		}
	}
}
