package pl.jedenpies.web.tracker.service;

import java.awt.image.BufferedImage;

import pl.jedenpies.web.tracker.model.domain.Location;

public interface PaintMapService {

	BufferedImage drawArea(Location lowCorner, Location highCorner);

}
