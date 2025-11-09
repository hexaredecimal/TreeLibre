package org.hexaredecimal.treestudio.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import org.hexaredecimal.treestudio.utils.ResourceLoader;

public class Splash extends JPanel {

	private BufferedImage image;
	private int stripeOffset = 0;
	private int stripeWidth = 5; 
	private int stripeSpeed = 5;  
	private int value = 0; 
	private Color backgroundColor = Color.GRAY;
	private Color progressColor = new Color(194, 178, 128);
	private JFrame frame; 

	private String loadingText = "Starting";

	public Splash() {
		var img = ResourceLoader.loadImageResource("misc/splash");

		image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();

		Timer repaintTimer = new Timer(16, e -> repaint());
		repaintTimer.start();

		Timer stripeTimer = new Timer(50, e -> {
			stripeOffset = (stripeOffset + stripeSpeed) % (stripeWidth * 2);
			repaint();
		});
		stripeTimer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		//super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		if (image != null) {
			g2.drawImage(image, 0, 0, null);
		}

		g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
		FontMetrics fm = g2.getFontMetrics();
		int textWidth = fm.stringWidth(loadingText);
		int textX = (getWidth() - textWidth) / 2;
		int textY = getHeight() - 10;
		g2.setColor(progressColor.darker().darker());
		g2.drawString(loadingText, textX, textY);

		int barWidth = 200;
		int barHeight = 5;
		int barX = (getWidth() - barWidth) / 2;
		int barY = getHeight() - 30;

		g2.setColor(backgroundColor);
		g2.fillRect(barX, barY, barWidth, barHeight);

		int fillWidth = (int) (barWidth * (value / 100.0));
		g2.setColor(progressColor);
		g2.fillRect(barX, barY, fillWidth, barHeight);

		g2.setClip(barX, barY, fillWidth, barHeight);
		g2.setColor(progressColor.darker());

		for (int i = -stripeWidth * 2 + stripeOffset; i < barWidth; i += stripeWidth * 2) {
			Polygon p = new Polygon();
			p.addPoint(barX + i, barY);
			p.addPoint(barX + i + stripeWidth, barY);
			p.addPoint(barX + i + stripeWidth - barHeight, barY + barHeight);
			p.addPoint(barX + i - barHeight, barY + barHeight);
			g2.fillPolygon(p);
		}

		g2.setClip(null);
		g2.dispose();
	}

	public void setValue(int value) {
		this.value = Math.max(0, Math.min(100, value));
		repaint();
	}

	public void setStripeWidth(int stripeWidth) {
		this.stripeWidth = stripeWidth;
	}

	public void setStripeSpeed(int stripeSpeed) {
		this.stripeSpeed = stripeSpeed;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setProgressColor(Color progressColor) {
		this.progressColor = progressColor;
	}

	public void setLoadingText(String loadingText) {
		this.loadingText = loadingText;
	}

	
	public int getValue() {
		return value;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getProgressColor() {
		return progressColor;
	}

	public String getLoadingText() {
		return loadingText;
	}
	

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setDone() {
		frame.dispose();
	}

	public void start(Runnable func) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.add(this);
		frame.setVisible(true);

		func.run();
	}

}
