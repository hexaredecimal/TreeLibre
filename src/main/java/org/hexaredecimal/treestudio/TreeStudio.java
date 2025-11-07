package org.hexaredecimal.treestudio;

/**
 *
 * @author hexaredecimal
 */
import org.hexaredecimal.treestudio.utils.Icons;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.hexaredecimal.treestudio.config.TreeConfig;
import org.hexaredecimal.treestudio.events.AppActions;
import org.hexaredecimal.treestudio.export.GifSequenceWriter;
import org.hexaredecimal.treestudio.ui.FileTree;
import org.hexaredecimal.treestudio.ui.TreePanel;

public class TreeStudio extends JFrame {

	public TreePanel treePanel;
	private JPanel controlPanel;
	private JPanel colorsPanel;

	JLabel lastSaved = new JLabel("Saved: Never");
	JLabel exportLabel = new JLabel("Export: ");
	JLabel currentFilePath = new JLabel("Path");
	JLabel MemoryUsage = new JLabel("MemoryUsage");

// Slider references
	public JSlider sfM;
	public JSlider sfS;
	public JSlider sthM;
	public JSlider sthS;
	public JSlider sgam;
	public JSlider sL;
	public JSlider sd;
	public JSlider sC;
	public JSlider lC;
	public JSlider lS;
	public JSlider lT;
	public JSlider sW;
	public JSlider sS;
	public JSlider sTrunkHeight;
	public JSlider sTrunkWidth;
	public JSlider sBranchLength;
	public JSlider sTaper;
	public JSlider sFlowerDensity;

	// Color settings
	public Color branchColor = new Color(38, 25, 13);
	public Color leafBaseColor = new Color(59, 145, 15);
	public Color bgColor = new Color(167, 213, 242);
	public Color flowerColor = new Color(255, 105, 180);
	public Color flowerPolenColor = new Color(255, 220, 0);

	// Config object holding references to controls
	private TreeConfig config;

	public static TreeStudio frame;

	public TreeStudio() {
		setTitle("TreeStudio");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		setIconImage(Icons.getIcon("appicon").getImage());
		
		createControlPanel();
		createBottomPanel();
		JScrollPane scrollPane = new JScrollPane(controlPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(350, 0));

		JScrollPane colorScrollPane = new JScrollPane(colorsPanel);
		colorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		colorScrollPane.setPreferredSize(new Dimension(350, 0));

		// construct config AFTER sliders/colors created
		config = new TreeConfig(this);

		treePanel = new TreePanel(config);
		treePanel.setPreferredSize(new Dimension(900, 600));

		var props = new FileTree();
		props.setPreferredSize(new Dimension(900, 200));

		JSplitPane propsSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, props, colorScrollPane);
		propsSplit.setDividerLocation(350);

		JSplitPane controlSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, scrollPane);
		controlSplit.setDividerLocation(900);

		JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, propsSplit, controlSplit);
		mainSplit.setDividerLocation(180);

		add(mainSplit, BorderLayout.CENTER);


		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					treePanel.regenerateTree();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					treePanel.toggleMouseWind();
				}
			}
		});

		setFocusable(true);
		setSize(1400, 800);
		setLocationRelativeTo(null);
	}

	public void createMenuAndToolbar() {
		createMenuBar();
		createToolBar();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu appMenu = new JMenu();
		menuBar.add(appMenu);
		appMenu.setIcon(Icons.getIcon("palm-tree"));

		appMenu.add(new JMenuItem(AppActions.CONFIG));
		appMenu.add(new JSeparator());
		appMenu.add(new JMenuItem(AppActions.EXIT));

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenu newTreeMenu = new JMenu("New Tree");
		newTreeMenu.setIcon(Icons.getIcon("oak-tree"));
		fileMenu.add(newTreeMenu);
		newTreeMenu.add(new JMenuItem(AppActions.NEW_TREE));
		newTreeMenu.add(new JMenuItem(AppActions.NEW_GRASS));
		newTreeMenu.add(new JMenuItem(AppActions.NEW_FLOWER));
		newTreeMenu.add(new JSeparator());
		newTreeMenu.add(new JMenuItem(AppActions.NEW_EMPTY));

		fileMenu.add(new JSeparator());
		fileMenu.add(new JMenuItem(AppActions.OPEN_TREE));
		fileMenu.add(new JSeparator());
		fileMenu.add(new JMenuItem(AppActions.SAVE));
		fileMenu.add(new JMenuItem(AppActions.SAVEAS));
		fileMenu.add(new JSeparator());
		fileMenu.add(new JMenuItem(AppActions.CLOSE_TREE));

		JMenu treeMenu = new JMenu("Tree");
		menuBar.add(treeMenu);

		treeMenu.add(new JMenuItem(AppActions.REGENERATE));

		JMenu viewMenu = new JMenu("Viewport");
		menuBar.add(viewMenu);

		viewMenu.add(new JMenuItem(AppActions.IMAGE_TILE));
		viewMenu.add(new JMenuItem(AppActions.SET_BG));
		viewMenu.add(new JSeparator());
		viewMenu.add(new JMenuItem(AppActions.GRID));

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.add(new JMenuItem(AppActions.DOCS));
		helpMenu.add(new JMenuItem(AppActions.WEBSITE));
		helpMenu.add(new JMenuItem(AppActions.LICENSE));
		helpMenu.add(new JSeparator());
		helpMenu.add(new JMenuItem(AppActions.ABOUT));

	}

	private void createToolBar() {
		JToolBar toolBar = new JToolBar("Main Toolbar");
		toolBar.setFloatable(true);
		add(toolBar, BorderLayout.PAGE_START);

		Action[] buttons = {
			AppActions.NEW_EMPTY, null, AppActions.NEW_TREE, AppActions.NEW_GRASS, AppActions.NEW_FLOWER, null,
			AppActions.OPEN_TREE, null, AppActions.SAVE, AppActions.SAVEAS, null, AppActions.REGENERATE, AppActions.DOCS, null,
			AppActions.CLOSE_TREE
		};

		for (var action : buttons) {
			if (action == null) {
				toolBar.add(new JSeparator(JSeparator.VERTICAL));
				continue;
			}

			JButton btn = new JButton(action);
			toolBar.add(btn);
		}
	}

	private void createControlPanel() {
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		addSectionLabel("Fractal Tree Parameters");

		// Original fractal controls
		sfM = addSlider("Mean mass splitting (f)", 1, 100, 60, 100.0);
		sfS = addSlider("Std mass splitting (f)", 0, 10, 0, 100.0);
		sthM = addSlider("Mean branching angle", 0, 157, 26, 100.0);
		sthS = addSlider("Std branching angle", 0, 157, 13, 100.0);
		sgam = addSlider("Branch length scaling (γ)", 0, 100, 38, 100.0);

		controlPanel.add(new JSeparator());
		addSectionLabel("Tree Properties");
		sL = addSlider("Trunk Length", 0, 2000, 600, 100.0);
		sd = addSlider("Trunk Thickness", 1, 4000, 1000, 100.0);

		controlPanel.add(new JSeparator());
		addSectionLabel("Leaf Properties");
		sC = addSlider("Leaf Cutoff", 1, 100, 10, 1000.0);
		lC = addSlider("Leaf Density (%)", 0, 100, 100, 1000.0);
		lS = addSlider("Leaf Size(%)", 1, 50, 1, 1000.0);
		lT = addSlider("Leaf Thickness (%)", 1, 50, 1, 1000.0);

		controlPanel.add(new JSeparator());
		addSectionLabel("Flower Properties");
		sFlowerDensity = addSlider("Flower Density %", 0, 100, 30, 100.0);

		controlPanel.add(new JSeparator());
		addSectionLabel("Wind Properties");
		sW = addSlider("Wind Strength", 0, 200, 100, 100.0);
		sS = addSlider("Wind Speed", 0, 200, 100, 100.0);

		controlPanel.add(new JSeparator());
		addSectionLabel("Extended Controls");

		// New controls
		sTrunkHeight = addSlider("Trunk Height Multiplier", 50, 200, 100, 100.0);
		sTrunkWidth = addSlider("Trunk Width Multiplier", 50, 200, 100, 100.0);
		sBranchLength = addSlider("Branch Length Multiplier", 50, 200, 100, 100.0);
		sTaper = addSlider("Taper Effect", 50, 109, 105, 100.0);

		// Color controls
		colorsPanel = new JPanel();
		colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.Y_AXIS));
		colorsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addSectionLabel("Colors", colorsPanel);

		colorsPanel.add(createColorButton("Branch Color", branchColor, c -> {
			branchColor = c;
			treePanel.regenerateTree();
		}));
		colorsPanel.add(Box.createVerticalStrut(5));

		colorsPanel.add(createColorButton("Leaf Base Color", leafBaseColor, c -> {
			leafBaseColor = c;
			treePanel.regenerateTree();
		}));

		colorsPanel.add(createColorButton("Flower Color", flowerColor, c -> {
			flowerColor = c;
			treePanel.regenerateTree();
		}));

		colorsPanel.add(createColorButton("Flower Polen Color", flowerPolenColor, c -> {
			flowerPolenColor = c;
			treePanel.regenerateTree();
		}));

		colorsPanel.add(Box.createVerticalStrut(5));

		colorsPanel.add(createColorButton("Background Color", bgColor, c -> {
			bgColor = c;
			if (treePanel != null) {
				treePanel.repaint();
			}
		}));
	}

	private void createBottomPanel() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false); // Make the toolbar fixed

		// Create the left label
		// Create the two right labels
		// Use a Box for right alignment of the labels
		this.add(toolBar, BorderLayout.SOUTH);

		Box leftBox = Box.createHorizontalBox();
		leftBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels
		leftBox.add(currentFilePath);
		leftBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels
		leftBox.add(new JSeparator(JSeparator.VERTICAL));
		leftBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels
		leftBox.add(MemoryUsage);
		leftBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels
		leftBox.add(new JSeparator(JSeparator.VERTICAL));

		Box rightBox = Box.createHorizontalBox();
		rightBox.add(Box.createHorizontalGlue()); // Push components to the right
		rightBox.add(lastSaved);
		rightBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels
		rightBox.add(new JSeparator(JSeparator.VERTICAL));
		rightBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels
		rightBox.add(exportLabel);
		rightBox.add(new JProgressBar(0, 100));
		rightBox.add(Box.createHorizontalStrut(10)); // Add spacing between labels

		// Add the components to the toolbar
		toolBar.add(leftBox); // Left-aligned label
		toolBar.add(Box.createHorizontalGlue()); // Filler to push subsequent components to the right
		toolBar.add(rightBox);
		add(toolBar, BorderLayout.SOUTH);
	}
	
	private void addSectionLabel(String text) {
		JLabel label = new JLabel(text);
		label.putClientProperty("FlatLaf.styleClass", "h2");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(label);
		controlPanel.add(Box.createVerticalStrut(5));
	}

	private void addSectionLabel(String text, JPanel where) {
		JLabel label = new JLabel(text);
		label.putClientProperty("FlatLaf.styleClass", "h2");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		where.add(label);
		where.add(Box.createVerticalStrut(5));
	}

	private JSlider addSlider(String label, int min, int max, int value, double scale) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMaximumSize(new Dimension(350, 60));

		JLabel jLabel = new JLabel(label + ": " + String.format("%.4f", value / scale));
		jLabel.putClientProperty("FlatLaf.styleClass", "light");
		jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(jLabel);

		JSlider slider = new JSlider(min, max, value);
		slider.setAlignmentX(Component.LEFT_ALIGNMENT);
		slider.addChangeListener(e -> {
			jLabel.setText(label + ": " + String.format("%.4f", slider.getValue() / scale));
			if (treePanel != null) {
				// Regenerate tree structure when sliders change (except wind)
				if (!label.contains("Wind")) {
					treePanel.regenerateTree();
				} else {
					treePanel.updateParameters();
				}
			}
		});
		panel.add(slider);

		controlPanel.add(panel);
		controlPanel.add(Box.createVerticalStrut(5));

		return slider;
	}

	private JButton createColorButton(String label, Color initial, java.util.function.Consumer<Color> listener) {
		JButton button = new JButton(label);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(300, 30));
		button.addActionListener(e -> {
			Color chosen = JColorChooser.showDialog(this, label, initial);
			if (chosen != null) {
				listener.accept(chosen);
			}
		});
		return button;
	}

	public void exportPNG() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Save Tree as PNG");
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				java.io.File file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".png")) {
					file = new java.io.File(file.getAbsolutePath() + ".png");
				}
				javax.imageio.ImageIO.write(treePanel.getBuffer(), "PNG", file);
				JOptionPane.showMessageDialog(this, "Tree exported successfully!");
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "Error exporting: " + ex.getMessage());
			}
		}
	}

	public void exportGIF() {
		// Simplified: write currently buffered image frames into a GIF using GifSequenceWriter
		new Thread(() -> {
			try {
				java.io.File file = new java.io.File("anim.gif");
				if (!file.getName().toLowerCase().endsWith(".gif")) {
					file = new java.io.File(file.getAbsolutePath() + ".gif");
				}
				int delay = 200;
				javax.imageio.stream.ImageOutputStream output = new javax.imageio.stream.FileImageOutputStream(file);
				GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_INT_ARGB, delay, true);
				writer.writeToSequence(treePanel.getBuffer());
				writer.close();
				output.close();
				JOptionPane.showMessageDialog(this, "GIF exported successfully!");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error exporting GIF: " + e.getMessage());
			}
		}).start();
	}

	public static void start(String[] args) {
		SwingUtilities.invokeLater(() -> {

			try {
				if (false) {
					FlatDarkLaf.setup();
					FlatDarkLaf.updateUI();
				} else {
					FlatLightLaf.setup();
					FlatLightLaf.updateUI();
				}
			} catch (Exception ex) {
				System.err.println("Failed to initialize LaF");
			}

			Icons.loadIcons();
			frame = new TreeStudio();
			frame.createMenuAndToolbar();
			frame.setVisible(true);
		});
	}
}
