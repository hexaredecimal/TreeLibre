package org.hexaredecimal.treestudio.ui;

import javax.swing.JTree;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author hexaredecimal
 */
public class FileTree extends JPanel{
	
	private JTree tree;
	private DefaultTreeModel treeModel;

	public FileTree() {
		super(new BorderLayout());
		File rootFile = new File(System.getProperty("user.dir"));
		DefaultMutableTreeNode rootNode = createTreeNodes(rootFile);
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.setCellRenderer(new FileTreeCellRenderer());

		tree.setComponentPopupMenu(createContextMenu());
		tree.addMouseListener(new TreeMouseListener());

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane, BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode createTreeNodes(File fileRoot) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileRoot);
		if (fileRoot.isDirectory()) {
			File[] files = fileRoot.listFiles();
			if (files != null) {
				for (File file : files) {
					node.add(createTreeNodes(file));
				}
			}
		}
		return node;
	}

	private void refreshTree() {
		File rootFile = new File(System.getProperty("user.dir"));
		DefaultMutableTreeNode newRoot = createTreeNodes(rootFile);
		treeModel.setRoot(newRoot);
		treeModel.reload();
	}

	private JPopupMenu createContextMenu() {
		return new JPopupMenu(); // dummy, context is built dynamically
	}

	private class TreeMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isRightMouseButton(e)) {
				int selRow = tree.getClosestRowForLocation(e.getX(), e.getY());
				tree.setSelectionRow(selRow);
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

				if (selPath == null) {
					return;
				}
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
				var file = (File)selectedNode.getUserObject();

				JPopupMenu menu = new JPopupMenu();
				if (file.isDirectory()) {
					JMenuItem addFolder = new JMenuItem("Add Folder");
					JMenuItem addFile = new JMenuItem("Add File");
					JMenuItem delete = new JMenuItem("Delete Folder");

					addFolder.addActionListener(ev -> {
						String name = JOptionPane.showInputDialog("Enter folder name:");
						if (name != null && !name.isBlank()) {
							File newDir = new File(file, name);
							if (!newDir.exists()) {
								newDir.mkdir();
							}
							refreshTree();
						}
					});

					addFile.addActionListener(ev -> {
						String name = JOptionPane.showInputDialog("Enter file name:");
						if (name != null && !name.isBlank()) {
							File newFile = new File(file, name);
							try {
								if (!newFile.exists()) {
									newFile.createNewFile();
								}
								refreshTree();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
						}
					});

					delete.addActionListener(ev -> {
						int confirm = JOptionPane.showConfirmDialog(null, "Delete this folder?", "Confirm", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							deleteRecursively(file);
							refreshTree();
						}
					});

					menu.add(addFolder);
					menu.add(addFile);
					menu.addSeparator();
					menu.add(delete);
				} else {
					JMenuItem open = new JMenuItem("Open File");
					JMenuItem delete = new JMenuItem("Delete File");

					open.addActionListener(ev -> System.out.println("Opening file: " + file.getAbsolutePath()));
					delete.addActionListener(ev -> {
						int confirm = JOptionPane.showConfirmDialog(null, "Delete this file?", "Confirm", JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							file.delete();
							refreshTree();
						}
					});

					menu.add(open);
					menu.addSeparator();
					menu.add(delete);
				}

				menu.show(tree, e.getX(), e.getY());
			}
		}
	}

	private void deleteRecursively(File file) {
		if (file.isDirectory()) {
			File[] contents = file.listFiles();
			if (contents != null) {
				for (File f : contents) {
					deleteRecursively(f);
				}
			}
		}
		file.delete();
	}
}
