package pl.jedenpies.web.traces.service;

import java.awt.image.BufferedImage;

import pl.jedenpies.web.traces.model.domain.Location;

public interface PaintingService {

	public BufferedImage createPicture(Location leftBottomCorner, Location rightTopCorner, int picSize);
	
}
