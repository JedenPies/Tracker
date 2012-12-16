package pl.jedenpies.web.traces.model.domain;

public enum AreaType {

	LEVEL1(8, 4),
	LEVEL2(4, 2),
	LEVEL3(2, 1),
	LEVEL4(1, 0.5),
	LEVEL5(0.4, 0.2),
	LEVEL6(0.2, 0.1),
	LEVEL7(0.1, 0.05),
	LEVEL8(0.04, 0.02),
	LEVEL9(0.02, 0.01),
	LEVEL10(0.01, 0.005)
	;
	private double width;
	private double height; 
	
	private AreaType(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	
	public static AreaType getByName(String name) {
		if (name == null) return null;
		for (AreaType type : AreaType.values()) {
			if (type.name().equals(name)) return type;
		}
		return null;
	}
}
