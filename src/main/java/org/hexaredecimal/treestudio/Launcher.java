package org.hexaredecimal.treestudio;

import javax.swing.Timer;
import org.hexaredecimal.treestudio.ui.Splash;

/**
 *
 * @author hexaredecimal
 */
public class Launcher {
	public static void main(String[] args) {
		var splash = new Splash();
		splash.setup();
		splash.setVisible(true);

		Timer timer = new Timer(5000, e -> {
			splash.dispose();
			TreeStudio.start(args);
		});

		timer.setRepeats(false);
		timer.start();
	}
}
