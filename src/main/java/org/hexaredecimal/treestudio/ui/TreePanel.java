package org.hexaredecimal.treestudio.ui;

/**
 *
 * @author hexaredecimal
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import org.hexaredecimal.treestudio.config.TreeConfig;
import org.hexaredecimal.treestudio.models.Tree;

public class TreePanel extends JPanel {

	private Tree tree;
	private double time = 0;
	public javax.swing.Timer timer;
	private boolean mouseWind = false;
	private Point mousePos = new Point(0, 0);
	private BufferedImage buffer;
	private boolean needsRedraw = true;
	private BufferedImage tileImage;
	private final TreeConfig config;
	private final Random random = new Random();

	public TreePanel(TreeConfig config) {
		this.config = config;
		setBackground(config.getBgColor());
		tree = new Tree(new double[]{450, 500}, new double[]{0, -1}, config.getTrunkLength(), config.getTrunkThickness(), config, random);
		tileImage = loadAndResize("./PNG/Dark/texture_07.png", 32, 32);

		timer = new javax.swing.Timer(16, e -> {
			nextFrame();
			repaint();
		});
		timer.start();

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos = e.getPoint();
			}
		});
	}

	public void nextFrame() {
		time += 0.01;
		double windStrength = config.getWindStrength();
		double windSpeed = config.getWindSpeed();
		double fX = 0.02 * windStrength * Math.sin(time * 0.8);
		double fY = 0.005 * windStrength * Math.cos(time * 1.2);
		if (mouseWind) {
			fX += 0.0004 * (mousePos.x - getWidth() / 2.0);
			fY += 0.0004 * (mousePos.y - getHeight() / 2.0);
		}
		tree.applyForce(new double[]{fX * windSpeed * 2, fY * windSpeed * 2});
		needsRedraw = true;
	}

	public static BufferedImage loadAndResize(String path, int width, int height) {
		try {
			BufferedImage original = ImageIO.read(new File(path));
			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = resized.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(original, 0, 0, width, height, null);
			g2.dispose();
			return resized;
		} catch (IOException ex) {
			// don't exit the app from a utility in refactor; return null and continue
		}
		return null;
	}

	public void regenerateTree() {
		tree = new Tree(new double[]{getWidth() / 2.0, getHeight() * 0.85}, new double[]{0, -1}, config.getTrunkLength(), config.getTrunkThickness(), config, random);
		needsRedraw = true;
	}

	public void toggleMouseWind() {
		mouseWind = !mouseWind;
	}

	public void updateParameters() {
		needsRedraw = true;
		repaint();
	}

	public void regenerateImage() {
		needsRedraw = true;
		int width = getWidth() > 0 ? getWidth() : 900;
		int height = getHeight() > 0 ? getHeight() : 600;
		if (buffer == null || buffer.getWidth() != width || buffer.getHeight() != height) {
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D g2d = buffer.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setColor(config.getBgColor());
		g2d.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		java.awt.geom.AffineTransform originalTransform = g2d.getTransform();
		tree.display(g2d);
		g2d.setTransform(originalTransform);
		g2d.dispose();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(config.getBgColor());
		if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
			buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			needsRedraw = true;
		}
		if (needsRedraw) {
			regenerateImage();
			needsRedraw = false;
		}
		g.drawImage(buffer, 0, 0, null);
	}

	public BufferedImage getBuffer() {
		return buffer;
	}
}
