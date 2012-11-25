package pl.jedenpies.web.tracker.beans;

import java.util.Set;

import pl.jedenpies.web.tracker.model.domain.AreaType;
import pl.jedenpies.web.tracker.model.domain.Location;

public class MapDescriptor {

	private Location leftBottomCorner;
	private Location rightTopCorner;
	private Set<AreaType> areaTypes;
	
	private int picSize;
	private String fileFormat;
	
	public Location getLeftBottomCorner() {
		return leftBottomCorner;
	}
	public void setLeftBottomCorner(Location leftBottomCorner) {
		this.leftBottomCorner = leftBottomCorner;
	}
	public Location getRightTopCorner() {
		return rightTopCorner;
	}
	public void setRightTopCorner(Location rightTopCorner) {
		this.rightTopCorner = rightTopCorner;
	}
	public Set<AreaType> getAreaTypes() {
		return areaTypes;
	}
	public void setAreaTypes(Set<AreaType> areaTypes) {
		this.areaTypes = areaTypes;
	}
	public int getPicSize() {
		return picSize;
	}
	public void setPicSize(int picSize) {
		this.picSize = picSize;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
}
