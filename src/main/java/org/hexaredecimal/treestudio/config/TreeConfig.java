
package org.hexaredecimal.treestudio.config;

/**
 *
 * @author hexaredecimal
 */
import java.awt.Color;
import javax.swing.*;
import org.hexaredecimal.treestudio.TreeStudio;



public class TreeConfig {

	public static final int PX_TO_LEN = 10;
	private final TreeStudio frame;

	public TreeConfig(TreeStudio frame) {
		this.frame = frame;
	}

	public double getSfM() {
		return frame.sfM.getValue() / 100.0;
	}

	public double getSfS() {
		return frame.sfS.getValue() / 100.0;
	}

	public double getSthM() {
		return frame.sthM.getValue() / 100.0;
	}

	public double getSthS() {
		return frame.sthS.getValue() / 100.0;
	}

	public double getSgamma() {
		return frame.sgam.getValue() / 100.0;
	}

	public double getTrunkLength() {
		return frame.sL.getValue() / 100.0;
	}

	public double getTrunkThickness() {
		return frame.sd.getValue() / 100.0;
	}

	public double getLeafCutoff() {
		return frame.sC.getValue() / 1000.0;
	}

	public double getLeafDensity() {
		return frame.lC.getValue() / 100.0;
	}

	public int getLeafSize() {
		return frame.lS.getValue();
	}

	public int getLeafThickness() {
		return frame.lT.getValue();
	}

	public double getFlowerDensity() {
		return frame.sFlowerDensity.getValue() / 100.0;
	}

	public double getWindStrength() {
		return frame.sW.getValue() / 100.0;
	}

	public double getWindSpeed() {
		return frame.sS.getValue() / 100.0;
	}

	public double getTrunkHeightMult() {
		return frame.sTrunkHeight.getValue() / 100.0;
	}

	public double getTrunkWidthMult() {
		return frame.sTrunkWidth.getValue() / 100.0;
	}

	public double getBranchLengthMult() {
		return frame.sBranchLength.getValue() / 100.0;
	}

	public double getTaperMult() {
		return frame.sTaper.getValue() / 100.0;
	}

	public Color getBranchColor() {
		return frame.branchColor.getSelectedColor();
	}

	public Color getLeafBaseColor() {
		return frame.leafBaseColor.getSelectedColor();
	}

	public Color getBgColor() {
		return frame.bgColor.getSelectedColor();
	}

	public Color getFlowerColor() {
		return frame.flowerColor.getSelectedColor();
	}

	public Color getFlowerPolenColor() {
		return frame.flowerPolenColor.getSelectedColor();
	}
}
