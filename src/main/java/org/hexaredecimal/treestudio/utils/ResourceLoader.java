package org.hexaredecimal.treestudio.utils;

import java.awt.Image;
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
}
