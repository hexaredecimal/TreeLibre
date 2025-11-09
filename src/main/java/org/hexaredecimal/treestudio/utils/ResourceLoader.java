package org.hexaredecimal.treestudio.utils;

import java.awt.Image;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author hexaredecimal
 */
public class ResourceLoader {

	public static Image loadImageResource(String name) {
		var path = String.format("/%s.png", name);
		var icn = ResourceLoader.class.getResource(path);
		if (icn == null) {
			throw new RuntimeException("Cannot load internal resource");
		}

		var icon = new ImageIcon(icn);
		return icon.getImage();
	}

	public static File loadFileResource(String name) {
		var path = String.format("/%s", name);
		URL uri = ResourceLoader.class.getResource(path);
		if (uri == null) {
			throw new RuntimeException("Cannot load internal resource");
		}

		try {
			return new File(uri.toURI());
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public static URL loadUrlResource(String name) {
		var path = String.format("/%s", name);
		URL uri = ResourceLoader.class.getResource(path);
		if (uri == null) {
			throw new RuntimeException("Cannot load internal resource");
		}
		return uri;
	}

	
}
