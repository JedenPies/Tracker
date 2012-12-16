package pl.jedenpies.web.traces.utils;

import java.io.File;

import pl.jedenpies.web.traces.model.domain.AreaType;

public interface AreaFileLocationProvider {

	/**
	 * Provides file for given location.
	 * Provides file name basing on the left bottom corner of the map, given location and type of the area,
	 * mostly its size.
	 * @param start left bottom corner of the map
	 * @param location location 
	 * @param areaType type of the area
	 * @return file where location is painted
	 */
//	public File findFile(Location start, Location location, AreaType areaType);
	
	public File getRoot();
	public File findFile(int x, int y, AreaType areaType);
	
	/**
	 * Provides default.
	 * @return
	 */
	public File findDefault();
}
