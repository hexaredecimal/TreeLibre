package org.hexaredecimal.treestudio;

import org.hexaredecimal.treestudio.ui.Splash;
import org.hexaredecimal.treestudio.utils.Icons;

/**
 *
 * @author hexaredecimal
 */
public class Launcher {

	public static void main(String[] args) {
		var splash = new Splash();
		splash.start(() -> {
			Icons.loadIcons((progress, length, iconName) -> {
				splash.setLoadingText("Loading Icon Resource: " + iconName);
				double percent = ((double) progress) / length;
				splash.setValue((int) (percent * 100));
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			});

			splash.setLoadingText("Starting TreeStudio");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}

			splash.setDone();
			TreeStudio.start(args);
		});
	}
}
