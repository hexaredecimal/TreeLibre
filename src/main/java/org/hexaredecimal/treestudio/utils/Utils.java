package org.hexaredecimal.treestudio.utils;

/**
 *
 * @author hexaredecimal
 */
// File: Utils.java
public class Utils {

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public static int clamp(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}

	public static double[] rotateVec(double[] R, double t) {
		return new double[]{R[0] * Math.cos(t) + R[1] * Math.sin(t), R[1] * Math.cos(t) - R[0] * Math.sin(t)};
	}
}
