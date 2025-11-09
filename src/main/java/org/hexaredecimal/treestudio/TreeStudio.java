package org.hexaredecimal.treestudio;

/**
 *
 * @author hexaredecimal
 */
import org.hexaredecimal.treestudio.utils.Icons;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import de.cerus.jgif.GifImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.hexaredecimal.treestudio.config.TreeConfig;
import org.hexaredecimal.treestudio.events.AppAction;
import org.hexaredecimal.treestudio.events.AppActions;
import org.hexaredecimal.treestudio.ui.ColorPickerButton;
import org.hexaredecimal.treestudio.ui.ExportGifDialogPanel;
import org.hexaredecimal.treestudio.ui.ExportPngDialogPanel;
import org.hexaredecimal.treestudio.ui.FileTree;
import org.hexaredecimal.treestudio.ui.StrippedProgressBar;
import org.hexaredecimal.treestudio.ui.TreePanel;
import org.hexaredecimal.treestudio.utils.Binary;
import org.hexaredecimal.treestudio.utils.FileFilter;

public final class TreeStudio extends JFrame {

	public TreePanel treePanel;
	public String selectedTreeFile = null;
	private JPanel controlPanel;
	private JPanel colorsPanel;

	JLabel sizeLoaded = new JLabel("0 bytes");
	JLabel exportLabel = new JLabel("Export: ");
	JLabel currentFilePath = new JLabel("[N/A]");
	JLabel MemoryUsage = new JLabel("MemoryUsage");

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
	public JSlider zoomSeek;

	public ColorPickerButton branchColor;
	public ColorPickerButton leafBaseColor;
	public ColorPickerButton bgColor;
	public ColorPickerButton flowerColor;
	public ColorPickerButton flowerPolenColor;

	private StrippedProgressBar exportBar;
	private final FileTree fileTree;
	private final Runtime runtime = Runtime.getRuntime();

	private final TreeConfig config;

	public static TreeStudio frame;

	public TreeStudio() {
		closeTree();
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

		config = new TreeConfig(this);

		treePanel = new TreePanel(config);
		treePanel.setPreferredSize(new Dimension(900, 600));

		fileTree = new FileTree();
		fileTree.setPreferredSize(new Dimension(900, 200));

		JSplitPane propsSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fileTree, colorScrollPane);
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

	public JPopupMenu getPopUp() {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(AppActions.REGENERATE);
		popupMenu.add(new JSeparator());
		popupMenu.add(AppActions.SAVE);
		popupMenu.add(AppActions.SAVEAS);
		popupMenu.add(new JSeparator());
		popupMenu.add(AppActions.EXPORT_PNG);
		popupMenu.add(AppActions.EXPORT_GIF);
		popupMenu.add(new JSeparator());
		popupMenu.add(AppActions.CLOSE_TREE);
		return popupMenu;
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
		fileMenu.add(new JMenuItem(AppActions.EXPORT_PNG));
		fileMenu.add(new JMenuItem(AppActions.EXPORT_GIF));
		fileMenu.add(new JSeparator());
		fileMenu.add(new JMenuItem(AppActions.CLOSE_TREE));

		JMenu treeMenu = new JMenu("Tree");
		menuBar.add(treeMenu);

		treeMenu.add(new JMenuItem(AppActions.REGENERATE));

		JMenu viewMenu = new JMenu("Viewport");
		menuBar.add(viewMenu);

		viewMenu.add(new JMenuItem(AppActions.SET_BG));
		viewMenu.add(new JSeparator());
		viewMenu.add(new JCheckBoxMenuItem(AppActions.BLUR));

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
		sS = addSlider("Wind Speed", 0, 4000, 100, 100.0);

		controlPanel.add(new JSeparator());
		addSectionLabel("Extended Controls");

		sTrunkHeight = addSlider("Trunk Height Multiplier", 50, 200, 100, 100.0);
		sTrunkWidth = addSlider("Trunk Width Multiplier", 50, 200, 100, 100.0);
		sBranchLength = addSlider("Branch Length Multiplier", 50, 200, 100, 100.0);
		sTaper = addSlider("Taper Effect", 50, 109, 105, 100.0);

		colorsPanel = new JPanel();
		colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.Y_AXIS));
		colorsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		addSectionLabel("Colors", colorsPanel);

		branchColor = new ColorPickerButton("Branch Color", new Color(38, 25, 13));
		leafBaseColor = new ColorPickerButton("Leaf Base Color", new Color(59, 145, 15));
		flowerColor = new ColorPickerButton("Flower Color", new Color(200, 200, 242));
		flowerPolenColor = new ColorPickerButton("Flower Polen Color", new Color(150, 130, 30));
		bgColor = new ColorPickerButton("Background Color", new Color(167, 213, 242));

		branchColor.addOnChangeListerner(c -> {
			treePanel.regenerateTree();
		});

		leafBaseColor.addOnChangeListerner(c -> {
			treePanel.regenerateTree();
		});

		flowerColor.addOnChangeListerner(c -> {
			treePanel.regenerateTree();
		});

		flowerPolenColor.addOnChangeListerner(c -> {
			treePanel.regenerateTree();
		});

		bgColor.addOnChangeListerner(c -> {
			if (treePanel != null) {
				treePanel.regenerateTree();
			}
		});

		colorsPanel.add(branchColor);
		colorsPanel.add(leafBaseColor);
		colorsPanel.add(flowerColor);
		colorsPanel.add(flowerPolenColor);
		colorsPanel.add(Box.createVerticalStrut(5));
		colorsPanel.add(bgColor);
	}

	private void createBottomPanel() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		this.add(toolBar, BorderLayout.SOUTH);

		Box leftBox = Box.createHorizontalBox();
		leftBox.add(Box.createHorizontalStrut(10));
		leftBox.add(currentFilePath);
		leftBox.add(Box.createHorizontalStrut(10));
		leftBox.add(new JSeparator(JSeparator.VERTICAL));
		leftBox.add(Box.createHorizontalStrut(10));
		leftBox.add(MemoryUsage);

		var timer = new Timer(1000, e -> {
			long total = runtime.totalMemory();
			long free = runtime.freeMemory();
			long used = total - free;
			MemoryUsage.setText("Memory: " + byteScaling(used) + " / " + byteScaling(runtime.maxMemory()));
		});
		timer.start();

		leftBox.add(Box.createHorizontalStrut(10));
		leftBox.add(new JSeparator(JSeparator.VERTICAL));

		Box rightBox = Box.createHorizontalBox();
		rightBox.add(Box.createHorizontalStrut(150));
		rightBox.add(sizeLoaded);
		rightBox.add(Box.createHorizontalStrut(10));
		rightBox.add(new JSeparator(JSeparator.VERTICAL));
		rightBox.add(Box.createHorizontalStrut(10));
		rightBox.add(exportLabel);
		rightBox.add(new JSeparator(JSeparator.VERTICAL));

		exportBar = new StrippedProgressBar(20, rightBox.getHeight());
		exportBar.setBackgroundColor(Color.DARK_GRAY);
		exportBar.setProgressColor(new Color(167, 213, 242));
		exportBar.setStripeWidth(20);
		exportBar.setStripeSpeed(4);

		rightBox.add(exportBar);
		rightBox.add(Box.createHorizontalStrut(10));

		var zoomLabel = new JLabel("Zoom:");
		zoomSeek = new JSlider(0, 200, 100);
		zoomSeek.setSize(50, zoomSeek.getHeight());
		rightBox.add(zoomLabel);
		rightBox.add(zoomSeek);

		
		toolBar.add(leftBox);
		toolBar.add(Box.createHorizontalGlue());
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

	public void loadTreeBinary(String path) {
		try (Binary bin = new Binary(path, true, true)) {
			var t = (char) bin.readByte();
			var r = (char) bin.readByte();
			var e = (char) bin.readByte();
			var e2 = (char) bin.readByte();

			if (t != 't' || r != 'r' || e != 'e' || e2 != 'e') {
				JOptionPane.showMessageDialog(this, "Invalid file header", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			sfM.setValue(bin.readInt());
			sfS.setValue(bin.readInt());
			sthM.setValue(bin.readInt());
			sthS.setValue(bin.readInt());
			sgam.setValue(bin.readInt());
			sL.setValue(bin.readInt());
			sd.setValue(bin.readInt());
			sC.setValue(bin.readInt());
			lC.setValue(bin.readInt());
			lS.setValue(bin.readInt());
			lT.setValue(bin.readInt());
			sW.setValue(bin.readInt());
			sS.setValue(bin.readInt());
			sTrunkHeight.setValue(bin.readInt());
			sTrunkWidth.setValue(bin.readInt());
			sBranchLength.setValue(bin.readInt());
			sTaper.setValue(bin.readInt());
			sFlowerDensity.setValue(bin.readInt());

			branchColor.setSelectedColor(new Color(bin.readInt()));
			leafBaseColor.setSelectedColor(new Color(bin.readInt()));
			bgColor.setSelectedColor(new Color(bin.readInt()));
			flowerColor.setSelectedColor(new Color(bin.readInt()));
			flowerPolenColor.setSelectedColor(new Color(bin.readInt()));
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void saveTreeBinary(String path) {
		try (Binary bin = new Binary(path, false, true)) {
			bin.write((byte) 't');
			bin.write((byte) 'r');
			bin.write((byte) 'e');
			bin.write((byte) 'e');

			bin.write(sfM.getValue());
			bin.write(sfS.getValue());
			bin.write(sthM.getValue());
			bin.write(sthS.getValue());
			bin.write(sgam.getValue());
			bin.write(sL.getValue());
			bin.write(sd.getValue());
			bin.write(sC.getValue());
			bin.write(lC.getValue());
			bin.write(lS.getValue());
			bin.write(lT.getValue());
			bin.write(sW.getValue());
			bin.write(sS.getValue());
			bin.write(sTrunkHeight.getValue());
			bin.write(sTrunkWidth.getValue());
			bin.write(sBranchLength.getValue());
			bin.write(sTaper.getValue());
			bin.write(sFlowerDensity.getValue());

			bin.write(branchColor.getSelectedColor().getRGB());
			bin.write(leafBaseColor.getSelectedColor().getRGB());
			bin.write(bgColor.getSelectedColor().getRGB());
			bin.write(flowerColor.getSelectedColor().getRGB());
			bin.write(flowerPolenColor.getSelectedColor().getRGB());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void pickBgColor() {
		Color c = JColorChooser.showDialog(this, "Pick a color",bgColor.getSelectedColor());
		if (c != null) {
			bgColor.setSelectedColor(c);
		}
	}

	public void saveTree() {
		if (selectedTreeFile == null) {
			var chooser = new JFileChooser(".");
			chooser.setFileFilter(FileFilter.treeFilter);
			chooser.setDialogTitle("Save Tree ");
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				var file = chooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".tree")) {
					file = new File(file.getAbsolutePath() + ".tree");
				}
				selectedTreeFile = file.getAbsolutePath();
				saveTreeBinary(selectedTreeFile);
				this.setTitle("TreeStudio - " + selectedTreeFile);
				currentFilePath.setText("[" + selectedTreeFile + "]");
				fileTree.refreshTree();
			}
			return;
		}

		saveTreeBinary(selectedTreeFile);
	}

	public void saveTreeAs() {
		var chooser = new JFileChooser(".");
		chooser.setFileFilter(FileFilter.treeFilter);
		chooser.setDialogTitle("Save Tree As");
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			var file = chooser.getSelectedFile();
			if (!file.getName().toLowerCase().endsWith(".tree")) {
				file = new File(file.getAbsolutePath() + ".tree");
			}
			selectedTreeFile = file.getAbsolutePath();
			saveTreeBinary(selectedTreeFile);
			treePanel.regenerateTree();
			this.setTitle("TreeStudio - " + selectedTreeFile);
			currentFilePath.setText("[" + selectedTreeFile + "]");
			fileTree.refreshTree();
		}
	}

	public void openTreeFile() {
		var chooser = new JFileChooser(".");
		chooser.setFileFilter(FileFilter.treeFilter);
		chooser.setDialogTitle("Open Tree file");
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			var file = chooser.getSelectedFile();
			openTreeFile(file);
			sizeLoaded.setText(byteScaling(file.length()));
		}
	}

	public void openTreeFile(File file) {
		selectedTreeFile = file.getAbsolutePath();
		loadTreeBinary(selectedTreeFile);
		treePanel.regenerateTree();
		this.setTitle("TreeStudio - " + selectedTreeFile);
		currentFilePath.setText("[" + selectedTreeFile + "]");
		sizeLoaded.setText(byteScaling(file.length()));
	}

	public void closeTree() {
		selectedTreeFile = null;
		this.setTitle("TreeStudio");
		currentFilePath.setText("[No Tree file loaded]");
		sizeLoaded.setText("0 bytes loaded");
	}

	public void closeTreeFile() {
		closeTree();
		defaultTree();
		treePanel.regenerateTree();
	}

	public static String byteScaling(long bytes) {
		if (bytes < 1024) {
			return bytes + " B";
		}

		double kb = bytes / 1024.0;
		if (kb < 1024) {
			return String.format("%.2f KB", kb);
		}

		double mb = kb / 1024.0;
		if (mb < 1024) {
			return String.format("%.2f MB", mb);
		}

		double gb = mb / 1024.0;
		return String.format("%.2f GB", gb);
	}

	private void defaultTree() {
		sfM.setValue(60);
		sthM.setValue(26);
		sthS.setValue(13);
		sgam.setValue(38);
		sL.setValue(600);
		sd.setValue(1000);
		sC.setValue(10);
		lC.setValue(100);
		lS.setValue(1);
		lT.setValue(1);
		sFlowerDensity.setValue(30);
		sW.setValue(100);
		sS.setValue(100);
		sTrunkHeight.setValue(100);
		sTrunkWidth.setValue(100);
		sBranchLength.setValue(100);
		sTaper.setValue(105);

		branchColor.setSelectedColor(new Color(38, 25, 13));
		leafBaseColor.setSelectedColor(new Color(59, 145, 15));
		flowerColor.setSelectedColor(new Color(200, 200, 242));
		flowerPolenColor.setSelectedColor(new Color(150, 130, 30));
		bgColor.setSelectedColor(new Color(167, 213, 242));
	}

	public void exportPNG() {

		var export = new ExportPngDialogPanel();
		var dialog = new JDialog(this, "Export Png", true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLayout(new BorderLayout());
		dialog.add(export, BorderLayout.CENTER);

		export.addOnCancelListener(e -> dialog.dispose());
		export.addOnDoneListener(e -> {
			treePanel.fillBg = !export.isSetTransparent();
			treePanel.timer.stop();
			try {
				var selectedFile = export.getSelectedPath();
				var file = new File(selectedFile);
				if (!file.getName().toLowerCase().endsWith(".png")) {
					file = new java.io.File(file.getAbsolutePath() + ".png");
				}
				treePanel.nextFrame();
				treePanel.regenerateImage();
				ImageIO.write(treePanel.getBuffer(), "PNG", file);
				dialog.dispose();
				JOptionPane.showMessageDialog(this, "Tree exported successfully!");
			} catch (IOException ex) {
			}
			treePanel.fillBg = true;
			treePanel.timer.start();
		});

		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void exportGIF() {

		var export = new ExportGifDialogPanel();
		var dialog = new JDialog(this, "Export Gif", true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLayout(new BorderLayout());
		dialog.add(export, BorderLayout.CENTER);

		export.addOnCancelListener(e -> dialog.dispose());
		export.addOnDoneListener(e -> {
			var selectedFile = export.getSelectedPath();
			new Thread(() -> {
				var file = new File(selectedFile);
				if (!file.getName().toLowerCase().endsWith(".gif")) {
					file = new File(file.getAbsolutePath() + ".gif");
				}

				GifImage gif = new GifImage();
				gif.setOutputFile(file);
				gif.repeatInfinitely(true);

				int frames = export.getFrames();
				treePanel.timer.stop();
				exportBar.setShowValue(true);
				treePanel.fillBg = !export.isSetTransparent();
				for (int i = 0; i < frames; ++i) {
					BufferedImage exportFrame = new BufferedImage(treePanel.getWidth(), treePanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics graphics = exportFrame.getGraphics();
					graphics.setColor(Color.WHITE);
					graphics.fillRect(0, 0, treePanel.getWidth(), treePanel.getHeight());
					treePanel.paint(graphics);
					graphics.dispose();

					treePanel.nextFrame();
					gif.addFrame(exportFrame);

					double percent = ((double) i) / frames;
					exportBar.setValue((int) (percent * 100));
				}
				treePanel.fillBg = true;
				treePanel.timer.start();

				gif.save();
				dialog.dispose();
				JOptionPane.showMessageDialog(this, "GIF exported successfully!");
				exportBar.setShowValue(false);
				exportBar.setValue(0);
			}).start();

		});

		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
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

			frame = new TreeStudio();
			frame.createMenuAndToolbar();
			frame.setVisible(true);
		});
	}

}
