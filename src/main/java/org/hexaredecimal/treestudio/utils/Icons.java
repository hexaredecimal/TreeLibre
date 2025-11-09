package org.hexaredecimal.treestudio.utils;

import java.util.HashMap;
import java.util.function.BiConsumer;

import javax.swing.ImageIcon;

/**
 *
 * @author hexaredecimal
 */
public class Icons {

	private static String[] icons = {
		"about",
		"add",
		"add-file",
		"add-row",
		"add-property",
		"appicon",
		"automatic",
		"book",
		"bookmark",
		"brush",
		"brush-fat",
		"cannabis",
		"cancel",
		"chatbot",
		"clear-symbol",
		"close",
		"code-file",
		"code-folder",
		"color",
		"compare-git",
		"copy-to-clipboard",
		"cut",
		"delete-document",
		"delete-file",
		"delete-folder",
		"delete-row",
		"delete-trash",
		"edit-property",
		"edit-file",
		"edit-property",
		"evergreen",
		"exit",
		"export",
		"file",
		"find",
		"find-and-replace",
		"folder",
		"folder-tree",
		"gif",
		"graph",
		"grass",
		"grid",
		"group-objects",
		"hot-article",
		"image-file",
		"layout",
		"leaf",
		"license",
		"line",
		"logout",
		"markdown",
		"navigation-toolbar-bottom",
		"navigation-toolbar-left",
		"navigation-toolbar-top",
		"new-window",
		"oak-tree",
		"open-in-browser",
		"options",
		"paint",
		"paint-bucket",
		"paint-palete",
		"palm-tree",
		"paste",
		"panorama",
		"png",
		"pi",
		"plugin",
		"point",
		"pointer",
		"project-setup",
		"redo",
		"remove",
		"rename",
		"restore-page",
		"restore-window",
		"right-navigation-toolbar",
		"ruler",
		"run",
		"save",
		"save-all",
		"save-as",
		"save-close",
		"search",
		"select-all",
		"settings",
		"shortcut",
		"show-property",
		"sigma",
		"small-icons",
		"software-installer",
		"store",
		"system-task",
		"time-machine",
		"tools",
		"undo",
		"visual-effects",
		"wind",
		"xxx-folder",
		"zoom-in",
		"zoom-to-extents",
		"zoom-out",
	};

	private static HashMap<String, ImageIcon> map = new HashMap<>();
	
	public static void loadIcons() {
		loadIcons((a, b, c) -> { });
	}
	
	public static void loadIcons(IconsProgressFunc<Integer, Integer, String> func) {
		for (int i = 0; i < icons.length; ++i) {
			var icon = icons[i];
			var path = String.format("/icons/%s.png", icon);
			var icn = Icons.class.getResource(path);
			func.execute(i, icons.length, icon);
			if (map.containsKey(icon)) {
				System.err.println("Icon " + icon + " is alread loaded. Skipping");
				continue;
			}

			if (icn == null) {
				map.put(icon, null);
				continue;
			}
			var _icon = new ImageIcon(icn);
			map.put(icon, _icon);
			System.err.println("Icon " + icon + " loaded successfully [" + path + "]");
		}
	}
	

	public static ImageIcon getIcon(String icon) {
		var ico = map.get(icon);
		if (ico == null) {
			// return the first available
			for (var kv : map.entrySet()) {
				return kv.getValue();
			}
		}
		return ico;
	}
}
