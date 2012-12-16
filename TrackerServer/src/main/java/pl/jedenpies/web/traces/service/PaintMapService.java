package pl.jedenpies.web.traces.service;

import java.awt.image.BufferedImage;

import pl.jedenpies.web.traces.model.domain.Location;

public interface PaintMapService {

	BufferedImage drawArea(Location lowCorner, Location highCorner);

}
