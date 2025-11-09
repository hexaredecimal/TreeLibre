package org.hexaredecimal.treestudio.ui;

/**
 *
 * @author hexaredecimal
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ColorPickerButton extends JPanel {

	private final JLabel label;
	private final SpectrumPanel spectrumPanel;
	private Color selectedColor = Color.WHITE;
	private final String labelText;
	private Consumer<Color> onChange;

	public ColorPickerButton(String labelText, Color defaultColor) {
		this.labelText = labelText;
		this.selectedColor = defaultColor;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);

		onChange = (c) -> {
			return;
		};

		label = new JLabel(labelText);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 0));

		spectrumPanel = new SpectrumPanel();
		spectrumPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(label);
		add(spectrumPanel);
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
		this.repaint();
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void addOnChangeListerner(Consumer<Color> fx) {
		onChange = fx;
	}

	private class SpectrumPanel extends JPanel {

		private BufferedImage gradientImage;

		private SpectrumPanel() {
			setPreferredSize(new Dimension(220, 25));
			generateGradient();

			var self = this;

			MouseAdapter mouseHandler = new MouseAdapter() {
				private void updateColor(MouseEvent e) {
					int panelW = getWidth();
					int panelH = getHeight();
					int imgW = gradientImage.getWidth();
					int imgH = gradientImage.getHeight();

					// Map mouse coordinates to image coordinates
					int x = (int) ((double) e.getX() / panelW * imgW);
					int y = (int) ((double) e.getY() / panelH * imgH);

					x = Math.max(0, Math.min(x, imgW - 1));
					y = Math.max(0, Math.min(y, imgH - 1));

					selectedColor = new Color(gradientImage.getRGB(x, y));
					onChange.accept(selectedColor);
					repaint();
				}

				@Override
				public void mousePressed(MouseEvent e) {
					if (SwingUtilities.isLeftMouseButton(e)) {
						updateColor(e);
					} else if (SwingUtilities.isRightMouseButton(e)) {
						Color c = JColorChooser.showDialog(SpectrumPanel.this, "Pick a color", selectedColor);
						if (c != null) {
							selectedColor = c;
							onChange.accept(selectedColor);
							repaint();
						}
					}
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					if (SwingUtilities.isLeftMouseButton(e)) {
						updateColor(e);
					}
				}
			};

			addMouseListener(mouseHandler);
			addMouseMotionListener(mouseHandler); // enable drag
		}

		private void generateGradient() {
			int w = 300;
			int h = 25;
			gradientImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < w; x++) {
				float hue = (float) x / (float) w;
				Color base = Color.getHSBColor(hue, 1f, 1f);
				for (int y = 0; y < h; y++) {
					float blend = (float) y / (float) h;
					int rgb = blendColors(Color.WHITE, base, Color.BLACK, blend, y, h).getRGB();
					gradientImage.setRGB(x, y, rgb);
				}
			}
		}

		private Color blendColors(Color top, Color middle, Color bottom, float ratio, int y, int height) {
			float mid = height / 2f;
			if (y < mid) {
				float t = y / mid;
				return interpolate(top, middle, t);
			} else {
				float t = (y - mid) / mid;
				return interpolate(middle, bottom, t);
			}
		}

		private Color interpolate(Color c1, Color c2, float t) {
			int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
			int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
			int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
			return new Color(r, g, b);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(gradientImage, 0, 0, getWidth(), getHeight(), null);

			// Current color preview box
			int previewWidth = 30;
			g.setColor(selectedColor);
			g.fillRect(getWidth() - previewWidth, 0, previewWidth, getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(getWidth() - previewWidth, 0, previewWidth, getHeight());
		}
	}
}
