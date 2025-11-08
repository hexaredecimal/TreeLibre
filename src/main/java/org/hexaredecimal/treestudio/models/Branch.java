package org.hexaredecimal.treestudio.models;

/**
 *
 * @author hexaredecimal
 */
import java.awt.*;
import java.util.Random;
import org.hexaredecimal.treestudio.config.TreeConfig;
import org.hexaredecimal.treestudio.utils.Utils;

public class Branch {

	double fd, dth, gamma;
	double[] R, D, Rp, Dp;
	double L, d;
	boolean leaf;
	Branch B1, B2;
	double th1, th2, th;
	Color leafColor;
	boolean hasFlower;
	Color flowerCol;
	private final TreeConfig config;
	private final Random random;

	public Branch(double[] R, double[] D, double L, double d, TreeConfig config, Random random) {
		this.config = config;
		this.random = random;
		this.fd = Utils.clamp(randomGaussian(config.getSfM(), config.getSfS()), 0, 1);
		this.dth = randomGaussian(config.getSthM(), config.getSthS());
		this.gamma = config.getSgamma();
		this.R = R.clone();
		this.D = D.clone();
		this.Rp = R.clone();
		this.Dp = D.clone();

		double trunkHeightMult = config.getTrunkHeightMult();
		double branchLengthMult = config.getBranchLengthMult();
		double trunkWidthMult = config.getTrunkWidthMult();
		double taperMult = config.getTaperMult();

		this.L = L * trunkHeightMult * branchLengthMult;
		this.d = d * trunkWidthMult * taperMult;

		if (this.d > config.getLeafCutoff()) {
			this.leaf = false;
			this.hasFlower = false;
			setBranchoffs();
		} else {
			this.th = Math.atan2(this.D[1], this.D[0]);
			double leafDensity = config.getLeafDensity();
			boolean shouldHaveLeaf = random.nextDouble() < leafDensity;
			this.leaf = shouldHaveLeaf;

			if (shouldHaveLeaf) {
				int cR = config.getLeafBaseColor().getRed() + random.nextInt(71) - 35;
				int cG = config.getLeafBaseColor().getGreen() + random.nextInt(71) - 35;
				int cB = config.getLeafBaseColor().getBlue() + random.nextInt(71) - 35;
				this.leafColor = new Color(
								Utils.clamp(cR, 0, 255),
								Utils.clamp(cG, 0, 255),
								Utils.clamp(cB, 0, 255),
								200
				);
			}

			double flowerDensity = config.getFlowerDensity();
			this.hasFlower = random.nextDouble() < flowerDensity;

			if (this.hasFlower) {
				int fR = config.getFlowerColor().getRed() + random.nextInt(41) - 20;
				int fG = config.getFlowerColor().getGreen() + random.nextInt(41) - 20;
				int fB = config.getFlowerColor().getBlue() + random.nextInt(41) - 20;
				this.flowerCol = new Color(
								Utils.clamp(fR, 0, 255),
								Utils.clamp(fG, 0, 255),
								Utils.clamp(fB, 0, 255),
								230
				);
			}
		}
	}

	private void setBranchoffs() {
		double[] Rn = {R[0] + TreeConfig.PX_TO_LEN * L * D[0],
			R[1] + TreeConfig.PX_TO_LEN * L * D[1]};

		double d1, d2, L1, L2;
		double taperMult = config.getTaperMult();

		d2 = fd * d * taperMult;
		d1 = (1 - fd) * d * taperMult;
		L2 = Math.pow(fd, gamma) * L;
		L1 = Math.pow(1 - fd, gamma) * L;

		double th0 = Math.atan((sq(L1 * d1) - sq(L2 * d2))
						/ (sq(L1 * d1) + sq(L2 * d2)) * Math.tan(dth));

		th1 = th0 - dth;
		th2 = th0 + dth;

		double[] D1 = Utils.rotateVec(D, th1);
		double[] D2 = Utils.rotateVec(D, th2);

		B1 = new Branch(Rn, D1, L1, d1, config, random);
		B2 = new Branch(Rn, D2, L2, d2, config, random);
	}

	public void applyForce(double[] Rn, double[] Dn, double[] F) {
		double[] dD = {F[0] / Math.sqrt(d), F[1] / Math.sqrt(d)};

		Rp[0] = Rn[0];
		Rp[1] = Rn[1];

		double lenDnew = Math.sqrt(sq(Dn[0] + dD[0]) + sq(Dn[1] + dD[1]));
		Dp[0] = (Dn[0] + dD[0]) / lenDnew;
		Dp[1] = (Dn[1] + dD[1]) / lenDnew;

		if (!leaf && B1 != null && B2 != null) {
			double RnX = Rp[0] + TreeConfig.PX_TO_LEN * L * Dp[0];
			double RnY = Rp[1] + TreeConfig.PX_TO_LEN * L * Dp[1];
			double[] Rnn = {RnX, RnY};
			double[] D1 = Utils.rotateVec(Dp, th1);
			double[] D2 = Utils.rotateVec(Dp, th2);
			B1.applyForce(Rnn, D1, F);
			B2.applyForce(Rnn, D2, F);
		} else if (leaf) {
			th = Math.atan2(Dp[1], Dp[0]);
		}
	}

	public void display(Graphics2D g) {
		g.setColor(config.getBranchColor());
		g.setStroke(new BasicStroke((float) (d + 0.3f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		double xF = Rp[0] + TreeConfig.PX_TO_LEN * L * Dp[0];
		double yF = Rp[1] + TreeConfig.PX_TO_LEN * L * Dp[1];

		g.drawLine((int) Rp[0], (int) Rp[1], (int) xF, (int) yF);

		if (leaf) {
			g.setColor(leafColor);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(xF + 4 * Dp[0], yF + 4 * Dp[1]);
			g2.rotate(th);
			g2.fillOval(-4, -1, 8 * config.getLeafSize(), 3 * config.getLeafThickness());
			g2.dispose();
		}

		if (hasFlower) {
			drawFlower(g, xF + 4 * Dp[0], yF + 4 * Dp[1]);
		}

		if (B1 != null && B2 != null) {
			B1.display(g);
			B2.display(g);
		}
	}

	private void drawFlower(Graphics2D g, double x, double y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		g2.setColor(config.getFlowerPolenColor());
		g2.fillOval(-2, -2, 4, 4);
		g2.setColor(flowerCol != null ? flowerCol : config.getFlowerColor());
		int numPetals = 5;
		double petalSize = 3.5;
		for (int i = 0; i < numPetals; i++) {
			double angle = (2 * Math.PI * i) / numPetals;
			double petalX = Math.cos(angle) * 3;
			double petalY = Math.sin(angle) * 3;
			g2.fillOval((int) (petalX - petalSize / 2), (int) (petalY - petalSize / 2), (int) petalSize, (int) petalSize);
		}
		g2.dispose();
	}

	private double sq(double x) {
		return x * x;
	}

	private double randomGaussian(double mean, double std) {
		return mean + random.nextGaussian() * std;
	}
}
