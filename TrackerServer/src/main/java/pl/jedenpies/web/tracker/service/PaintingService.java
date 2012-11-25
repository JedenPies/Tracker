package pl.jedenpies.web.tracker.service;

import java.awt.image.BufferedImage;

import pl.jedenpies.web.tracker.model.domain.Location;

public interface PaintingService {

	public BufferedImage createPicture(Location leftBottomCorner, Location rightTopCorner, int picSize);
	
}
