package org.hexaredecimal.treestudio.ui;

/**
 *
 * @author hexaredecimal
 */
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import org.hexaredecimal.treestudio.TreeStudio;
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
	private final TreeConfig config;
	private final Random random = new Random();
	private double scaleFactor = 1;
	public boolean blur = false;
	public boolean fillBg = true;

	public TreePanel(TreeConfig config) {
		this.config = config;
		setBackground(config.getBgColor());
		tree = new Tree(new double[]{450, 500}, new double[]{0, -1}, config.getTrunkLength(), config.getTrunkThickness(), config, random);

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

		setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.stringFlavor);
			}

			@Override
			public boolean importData(TransferSupport support) {
				if (!canImport(support)) {
					return false;
				}
				try {
					String filePath = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
					File droppedFile = new File(filePath);

					TreeStudio.frame.openTreeFile(droppedFile);

					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});

		var self = this;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}

			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					var popup = TreeStudio.frame.getPopUp();
					popup.show(self, e.getX(), e.getY());
				}
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
		if (fillBg) {
			g2d.setColor(config.getBgColor());
			g2d.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		} else {
			g2d.setComposite(AlphaComposite.Clear);
			g2d.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
			g2d.setComposite(AlphaComposite.SrcOver);
		}
		java.awt.geom.AffineTransform originalTransform = g2d.getTransform();
		tree.display(g2d);
		g2d.setTransform(originalTransform);
		g2d.dispose();
	}

	@Override
	protected void paintComponent(Graphics g) {
		setBackground(config.getBgColor());
		super.paintComponent(g);
		
		if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
			buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			needsRedraw = true;
		}
		if (needsRedraw) {
			regenerateImage();
			needsRedraw = false;
		}

		var graphics = (Graphics2D) g.create();

		if (blur) {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		}

		if (TreeStudio.frame.zoomSeek != null) {
			double minScale = 0.0;
			double maxScale = 2.0;

			int value = TreeStudio.frame.zoomSeek.getValue(); // assume 0-200
			int min = 0;
			int max = 200;

			scaleFactor = minScale + (value - min) * (maxScale - minScale) / (double) (max - min);
		}

		int panelWidth = getWidth();
		int panelHeight = getHeight();

		int imgWidth = buffer.getWidth();
		int imgHeight = buffer.getHeight();
		double x = (panelWidth - imgWidth * scaleFactor) / 2.0;
		double y = (panelHeight - imgHeight * scaleFactor) / 2.0;

		if (!fillBg) {
			graphics.setComposite(AlphaComposite.Clear);
			graphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
			graphics.setComposite(AlphaComposite.SrcOver);
		} 

		
		graphics.translate(x, y);
		graphics.scale(scaleFactor, scaleFactor);
		graphics.drawImage(buffer, 0, 0, null);
	}

	public BufferedImage getBuffer() {
		return buffer;
	}

}
