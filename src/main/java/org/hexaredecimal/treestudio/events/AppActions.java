package org.hexaredecimal.treestudio.events;

import javax.swing.Action;
import org.hexaredecimal.treestudio.TreeStudio;

/**
 *
 * @author hexaredecimal
 */
public class AppActions {

	public static final Action EXIT
					= AppAction
									.create("Exit")
									.icon("exit")
									.tooltip("Close the application")
									.shortcut("control shift G")
									.handler(e -> System.exit(0))
									.build();

	public static final Action NEW_TREE
					= AppAction
									.create("New Tree")
									.icon("evergreen")
									.tooltip("Create a new normal tree")
									.shortcut("control N")
									.handler(e -> System.exit(0))
									.build();

	public static final Action NEW_GRASS
					= AppAction
									.create("New Grass")
									.icon("grass")
									.tooltip("Create a new grass")
									.shortcut("control shift N")
									.handler(e -> System.exit(0))
									.build();

	public static final Action NEW_FLOWER
					= AppAction
									.create("New Flower")
									.icon("cannabis")
									.tooltip("Create a new flower")
									.shortcut("control shift alt N")
									.handler(e -> System.exit(0))
									.build();

	public static final Action NEW_EMPTY
					= AppAction
									.create("Empty")
									.icon("leaf")
									.tooltip("Start from nothing")
									.shortcut("control E")
									.handler(e -> System.exit(0))
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
					.create("Close Project")
					.icon("close")
					.tooltip("Close the currently selected tree")
					.handler(e -> TreeStudio.frame.closeTreeFile())
					.build();

	public static final Action EXPORT_PNG = AppAction
					.create("Export PNG")
					.icon("png")
					.tooltip("Save the tree into a png file")
					.handler(e -> TreeStudio.frame.exportPNG())
					.build();

	public static final Action EXPORT_GIF = AppAction
					.create("Export GIF")
					.icon("gif")
					.tooltip("Save the tree as a gif")
					.handler(e -> TreeStudio.frame.exportGIF())
					.build();

	public static final Action CONFIG = AppAction
					.create("Configuration")
					.icon("options")
					.tooltip("Configure the system")
					.handler(e -> System.exit(0))
					.build();

	public static final Action DOCS = AppAction
					.create("Documentation")
					.icon("book")
					.shortcut("F1")
					.tooltip("Read the documentation")
					.handler(e -> System.exit(0))
					.build();

	public static final Action WEBSITE = AppAction
					.create("Website")
					.icon("open-in-browser")
					.shortcut("F2")
					.tooltip("Open the product site")
					.handler(e -> System.exit(0))
					.build();

	public static final Action LICENSE = AppAction
					.create("License")
					.icon("license")
					.shortcut("F4")
					.tooltip("License")
					.handler(e -> System.exit(0))
					.build();

	public static final Action REGENERATE = AppAction
					.create("Regenerate")
					.icon("undo")
					.shortcut("control U")
					.tooltip("Regenerate the tree")
					.handler(e -> TreeStudio.frame.treePanel.regenerateTree())
					.build();

	public static final Action ABOUT = AppAction
					.create("About")
					.icon("about")
					.tooltip("About the product")
					.handler(e -> System.exit(0))
					.build();

	public static final Action IMAGE_TILE = AppAction
					.create("Tile")
					.icon("small-icons")
					.tooltip("Tile the view port")
					.handler(e -> System.exit(0))
					.build();

	public static final Action SET_BG = AppAction
					.create("Change Background")
					.icon("paint-bucket")
					.tooltip("Change the viewport background color")
					.handler(e -> System.exit(0))
					.build();
	
	public static final Action GRID = AppAction
					.create("Grid")
					.icon("grid")
					.tooltip("Toogle grid")
					.shortcut("control G")
					.handler(e -> System.exit(0))
					.build();

}
