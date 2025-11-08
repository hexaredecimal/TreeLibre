package org.hexaredecimal.treestudio.ui;

/**
 *
 * @author hexaredecimal
 */

import javax.swing.*;
import java.awt.*;

/**
 * Self-contained slanted/striped progress bar with optional value display.
 */
public class StrippedProgressBar extends JPanel {
    private Color backgroundColor = Color.GRAY;
    private Color progressColor = Color.BLUE;
    private int value = 0; // 0-100
    private int stripeOffset = 0;
    private final Timer stripeTimer;
    private boolean showValue = false;
    private int stripeWidth = 12; // width of stripes
    private int stripeSpeed = 2;  // pixels per timer tick

    public StrippedProgressBar(int width, int height) {
        setPreferredSize(new Dimension(width, height));

        // Timer animates stripeOffset
        stripeTimer = new Timer(50, e -> {
            stripeOffset = (stripeOffset + stripeSpeed) % (stripeWidth * 2);
            repaint();
        });
        stripeTimer.start();
    }

    public void setBackgroundColor(Color c) {
        this.backgroundColor = c;
        repaint();
    }

    public void setProgressColor(Color c) {
        this.progressColor = c;
        repaint();
    }

    public void setValue(int val) {
        this.value = Math.max(0, Math.min(val, 100));
        repaint();
    }

    public void setShowValue(boolean show) {
        this.showValue = show;
        repaint();
    }

    public void setStripeWidth(int w) {
        this.stripeWidth = Math.max(2, w);
        repaint();
    }

    public void setStripeSpeed(int s) {
        this.stripeSpeed = s;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        // Draw background
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, width, height);

        // Draw progress area
        int progressWidth = (int) (width * (value / 100.0));
        g2.setClip(0, 0, progressWidth, height);

        // Fill with base progress color
        g2.setColor(progressColor);
        g2.fillRect(0, 0, width, height);

        // Draw slanted stripes
        g2.setColor(progressColor.darker());
        for (int x = -stripeWidth * 2 + stripeOffset; x < width; x += stripeWidth * 2) {
            g2.fillPolygon(
                    new int[]{x, x + stripeWidth, x + stripeWidth - height, x - height},
                    new int[]{0, 0, height, height},
                    4
            );
        }

        g2.setClip(null);

        // Draw optional value text
        if (showValue) {
            String text = value + "%";
            FontMetrics fm = g2.getFontMetrics();
            int tx = (width - fm.stringWidth(text)) / 2;
            int ty = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(text, tx, ty);
        }

        g2.dispose();
    }

    // === Demo ===
		/*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Slanted Progress Bar Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridBagLayout());
            frame.setSize(600, 400);

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(10, 10, 10, 10);
            c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;

            // Example 1: standard blue
            SlantedProgressBar bar1 = new SlantedProgressBar(400, 30);
            bar1.setBackgroundColor(Color.LIGHT_GRAY);
            bar1.setProgressColor(Color.BLUE);
            frame.add(bar1, c);

            // Example 2: red stripes, faster, thicker stripes
            c.gridy++;
            SlantedProgressBar bar2 = new SlantedProgressBar(400, 25);
            bar2.setBackgroundColor(Color.DARK_GRAY);
            bar2.setProgressColor(Color.RED);
            bar2.setStripeWidth(20);
            bar2.setStripeSpeed(4);
            frame.add(bar2, c);

            // Example 3: green, showing value text
            c.gridy++;
            SlantedProgressBar bar3 = new SlantedProgressBar(400, 35);
            bar3.setBackgroundColor(Color.GRAY);
            bar3.setProgressColor(Color.GREEN);
            bar3.setShowValue(true);
            frame.add(bar3, c);

            // Animate all progress bars
            Timer t = new Timer(100, new ActionListener() {
                private int val = 0;
                private boolean forward = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    bar1.setValue(val);
                    bar2.setValue(val / 2);
                    bar3.setValue(val);
                    if (forward) {
                        val += 2;
                        if (val >= 100) forward = false;
                    } else {
                        val -= 2;
                        if (val <= 0) forward = true;
                    }
                }
            });
            t.start();

            frame.setVisible(true);
        });
    }
*/
}

