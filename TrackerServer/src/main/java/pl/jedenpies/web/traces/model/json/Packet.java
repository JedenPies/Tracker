package pl.jedenpies.web.traces.model.json;

import java.util.List;

public class Packet {

	private List<Coordinate> coordinates;

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
}
