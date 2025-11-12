package org.hexaredecimal.treestudio.events;

import javax.swing.Action;
import org.hexaredecimal.treestudio.TreeStudio;

/**
 *
 * @author hexaredecimal
 */
public class AppActions {

	public static final Action EXIT = AppAction
					.create("Exit")
					.icon("exit")
					.tooltip("Close the application")
					.shortcut("control shift Q")
					.handler(e -> System.exit(0))
					.build();

	public static final Action NEW_TREE = AppAction
					.create("New Tree")
					.icon("evergreen")
					.tooltip("Create a new normal tree")
					.shortcut("control N")
					.handler(e -> TreeStudio.frame.openInternalTreeFile("trees/default.tree"))
					.build();

	public static final Action NEW_GRASS = AppAction
					.create("New Grass")
					.icon("grass")
					.tooltip("Create a new grass")
					.shortcut("control shift N")
					.handler(e -> TreeStudio.frame.openInternalTreeFile("trees/grass.tree"))
					.build();

	public static final Action NEW_FLOWER = AppAction
					.create("New Flower")
					.icon("cannabis")
					.tooltip("Create a new flower")
					.shortcut("control shift alt N")
					.handler(e -> TreeStudio.frame.openInternalTreeFile("trees/flower.tree"))
					.build();

	public static final Action NEW_EMPTY = AppAction
					.create("Empty")
					.icon("leaf")
					.tooltip("Start from nothing")
					.shortcut("control E")
					.handler(e -> TreeStudio.frame.openInternalTreeFile("trees/empty.tree"))
					.build();

	public static final Action SAVE = AppAction
					.create("Save")
					.icon("save")
					.tooltip("Save")
					.shortcut("control S")
					.handler(e -> TreeStudio.frame.saveTree())
					.build();

	public static final Action SAVEAS = AppAction
					.create("Save As...")
					.icon("save-as")
					.tooltip("Save")
					.shortcut("control shift S")
					.handler(e -> TreeStudio.frame.saveTreeAs())
					.build();

	public static final Action OPEN_TREE = AppAction
					.create("Open Tree")
					.icon("file")
					.tooltip("Open a tree file")
					.shortcut("control O")
					.handler(e -> TreeStudio.frame.openTreeFile())
					.build();

	public static final Action CLOSE_TREE = AppAction
					.create("Close Tree")
					.icon("close")
					.shortcut("control x")
					.tooltip("Close the currently selected tree")
					.handler(e -> TreeStudio.frame.closeTreeFile())
					.build();

	public static final Action EXPORT_PNG = AppAction
					.create("Export PNG")
					.icon("png")
					.shortcut("control P")
					.tooltip("Save the tree into a png file")
					.handler(e -> TreeStudio.frame.exportPNG())
					.build();

	public static final Action EXPORT_GIF = AppAction
					.create("Export GIF")
					.icon("gif")
					.shortcut("control G")
					.tooltip("Save the tree as a gif")
					.handler(e -> TreeStudio.frame.exportGIF())
					.build();

	public static final Action CONFIG = AppAction
					.create("Configuration")
					.icon("options")
					.tooltip("Configure the system")
					.handler(e -> TreeStudio.frame.showConfig())
					.build();

	public static final Action DOCS = AppAction
					.create("Documentation")
					.icon("book")
					.shortcut("F1")
					.tooltip("Read the documentation")
					.handler(e -> TreeStudio.frame.showDocs())
					.build();

	public static final Action WEBSITE = AppAction
					.create("Website")
					.icon("open-in-browser")
					.shortcut("F2")
					.tooltip("Open the product site")
					.handler(e -> TreeStudio.frame.showWebSite())
					.build();

	public static final Action LICENSE = AppAction
					.create("License")
					.icon("license")
					.shortcut("F4")
					.tooltip("License")
					.handler(e -> TreeStudio.frame.showLicense())
					.build();

	public static final Action REGENERATE = AppAction
					.create("Regenerate")
					.icon("undo")
					.shortcut("control R")
					.tooltip("Regenerate the tree")
					.handler(e -> TreeStudio.frame.treePanel.regenerateTree())
					.build();

	public static final Action ABOUT = AppAction
					.create("About")
					.icon("about")
					.tooltip("About the product")
					.handler(e -> TreeStudio.frame.showAbout())
					.build();

	public static final Action SET_BG = AppAction
					.create("Change Background")
					.icon("paint-bucket")
					.tooltip("Change the viewport background color")
					.handler(e -> TreeStudio.frame.pickBgColor())
					.build();

	public static final Action BLUR = AppAction
					.create("Toggle Blur")
					.icon("paint")
					.tooltip("Toggle blur to reduce pixelaration")
					.handler(e -> TreeStudio.frame.treePanel.blur = !TreeStudio.frame.treePanel.blur)
					.build();
	
	public static final Action TRANSPARENT = AppAction
					.create("Toggle Transparency")
					.icon("paint")
					.tooltip("Toggle transparency")
					.handler(e -> TreeStudio.frame.treePanel.fillBg = !TreeStudio.frame.treePanel.fillBg)
					.build();

	public static final Action MOUSE_WIND = AppAction
					.create("Toggle Pointer Wind")
					.icon("pointer")
					.tooltip("Toggle wind controlled by the pointer")
					.handler(e -> TreeStudio.frame.treePanel.toggleMouseWind())
					.build();
	
	public static final Action ZOOM_IN = AppAction
					.create("Zoom In (+)")
					.icon("zoom-in")
					.tooltip("Zoom in into the view")
          .shortcut("control ADD")
					.handler(e -> {
						var value = TreeStudio.frame.zoomSeek.getValue() + 50;
						if (value > TreeStudio.frame.zoomSeek.getMaximum()) {
							return;
						}
						TreeStudio.frame.zoomSeek.setValue(value);
						TreeStudio.frame.treePanel.repaint();
					})
					.build();

	public static final Action ZOOM_OUT = AppAction
					.create("Zoom Out (-)")
					.icon("zoom-out")
          .shortcut("control SUBTRACT")
					.tooltip("Zoom out into the view")
					.handler(e -> {
						var value = TreeStudio.frame.zoomSeek.getValue() - 50;
						if (value < TreeStudio.frame.zoomSeek.getMinimum()) {
							return;
						}
						TreeStudio.frame.zoomSeek.setValue(value);
						TreeStudio.frame.treePanel.repaint();
					})
					.build();
	
	public static final Action ZOOM_RESET = AppAction
					.create("Reset Zoom(100%)")
					.icon("zoom-to-extents")
					.tooltip("Zoom out into the view")
					.handler(e -> {
						TreeStudio.frame.zoomSeek.setValue(100);
						TreeStudio.frame.treePanel.repaint();
					})
					.build();

}
