package pl.jedenpies.web.tracker.utils.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.jedenpies.web.tracker.model.domain.AreaType;
import pl.jedenpies.web.tracker.model.domain.Location;
import pl.jedenpies.web.tracker.utils.AreaFileLocationProvider;

@Component
public class DefaultAreaFileLocationProvider implements	AreaFileLocationProvider {

	private String extension = "png";
	
	@Value("${map.area.rootPath}")
	private File root;

	@Value("${map.area.rootPath}/${map.area.defaultPic}")
	private File defaultPic;

//	@Override
//	public File findFile(Location start, Location location, AreaType areaType) {
//		
//		int column = (int) Math.floor((location.getLongitude() - start.getLongitude()) / areaType.getWidth());
//		int row = (int) Math.floor((location.getLatitude() - start.getLatitude()) / areaType.getHeight());
//		String path = areaType.name() + "/" + row + "/" + column + "." + extension;
//		File result = new File(root, path);
//		return result;
//	}
	

	@Override
	public File findFile(int x, int y, AreaType areaType) {
		
		String path = areaType.name() + "/" + y + "/" + x + "." + extension;
		File result = new File(root, path);
		return result;
	}
	
	@Override
	public File findDefault() {		
		return defaultPic;
	}

	public void setRoot(File root) {
		
		if (root.exists() && !root.isDirectory()) {
			root.delete();
		} else if (!root.exists()) {
			root.mkdirs();
		}
		this.root = root;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public File getRoot() {
		return root;
	}


}

