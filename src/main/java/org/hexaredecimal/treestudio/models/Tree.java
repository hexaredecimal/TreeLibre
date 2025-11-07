package org.hexaredecimal.treestudio.models;

/**
 *
 * @author hexaredecimal
 */
import java.awt.*;
import java.util.Random;
import org.hexaredecimal.treestudio.config.TreeConfig;

public class Tree {

	public Branch root;
	private final TreeConfig config;
	private final Random random;

	public Tree(double[] R0, double[] D, double L, double d, TreeConfig config, Random random) {
		this.config = config;
		this.random = random;
		root = new Branch(R0, D, L, d, config, random);
	}

	public void applyForce(double[] F) {
		root.applyForce(root.R, root.D, F);
	}

	public void display(Graphics2D g) {
		root.display(g);
	}
}
