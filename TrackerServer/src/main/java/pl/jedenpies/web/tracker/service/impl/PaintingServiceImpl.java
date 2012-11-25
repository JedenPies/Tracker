package pl.jedenpies.web.tracker.service.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jedenpies.web.tracker.model.domain.Location;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;
import pl.jedenpies.web.tracker.service.CoordinateService;
import pl.jedenpies.web.tracker.service.PaintingService;

@Transactional @Service
public class PaintingServiceImpl implements PaintingService {

//	private static final Logger LOG = LoggerFactory.getLogger(PaintingServiceImpl.class); 
	
	@Autowired
	private CoordinateService coordsService;
	
	@Override
	public BufferedImage createPicture(Location leftBottomCorner, Location rightTopCorner, int picSize) {
		
//		LOG.debug("painting image for area: " + leftBottomCorner.toString() + "=>" + rightTopCorner.toString());
		ScrollableResults coords = coordsService.findCoordinates(leftBottomCorner, rightTopCorner);		
		coords.beforeFirst();

		double latScale  = picSize / (rightTopCorner.getLatitude()  - leftBottomCorner.getLatitude());
		double longScale = picSize / (rightTopCorner.getLongitude() - leftBottomCorner.getLongitude());
		
		BufferedImage image = new BufferedImage(picSize, picSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = (Graphics2D) image.getGraphics();
		// TODO: Colors should be defined externally
//		gr.setPaint(Color.white);
//		gr.fillRect(0, 0, picSize, picSize);
		gr.setPaint(Color.black);
		
		while (coords.next()) {
			Coordinate coord = (Coordinate) coords.get(0);			
			Ellipse2D.Double ellipse = new Ellipse2D.Double(
					(coord.getLongitude() - leftBottomCorner.getLongitude()) * longScale,
					picSize - (coord.getLatitude()  - leftBottomCorner.getLatitude()) * latScale,
					3, 3);
			gr.draw(ellipse);
			gr.fill(ellipse);			
		}
		
		return image;
	}
}
