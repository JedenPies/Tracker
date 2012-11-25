package pl.jedenpies.web.tracker.service.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.jedenpies.web.tracker.dao.CoordinateDao;
import pl.jedenpies.web.tracker.model.domain.Location;
import pl.jedenpies.web.tracker.model.hibernate.Coordinate;
import pl.jedenpies.web.tracker.service.PaintMapService;

@Transactional @Service("paintMapService")
public class PaintMapServiceImpl implements PaintMapService {

	@Autowired
	private CoordinateDao coordDao;
	
	@Override
	public BufferedImage drawArea(Location lowCorner, Location highCorner) {
		
		BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);		
		double latScale  = 1000 / (highCorner.getLatitude()  - lowCorner.getLatitude());
		double longScale = 1000 / (highCorner.getLongitude() - lowCorner.getLongitude());
		System.out.println("roznica " + highCorner.getLongitude() + " i " + lowCorner.getLongitude() + " = " + (highCorner.getLongitude() - lowCorner.getLongitude()));
		double scale = Math.min(latScale, longScale);
		Graphics2D gr = (Graphics2D) image.getGraphics();
		gr.setPaint(Color.white);
		gr.fillRect(0, 0, 1000, 1000);
		gr.setPaint(Color.black);	
		
		System.out.println("ls: " + longScale + " " + latScale);
		ScrollableResults coords = coordDao.findCoordinates(lowCorner, highCorner);		
		coords.beforeFirst();
		int a = 0;
		while (coords.next()) {
			Coordinate coord = (Coordinate) coords.get(0);			
			Ellipse2D.Double ellipse = new Ellipse2D.Double(
					(coord.getLongitude() - lowCorner.getLongitude()) * scale,
					1000 - (coord.getLatitude()  - lowCorner.getLatitude()) * scale,
					3, 3);
			gr.draw(ellipse);
			gr.fill(ellipse);
			a++;
			
		}
		System.out.println(a);
		return image;
	}
}
