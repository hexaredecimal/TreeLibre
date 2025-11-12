package org.hexaredecimal.treestudio.ui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import org.hexaredecimal.treestudio.TreeStudio;

public class FileTree extends JPanel {

	private JTree tree;
	private DefaultTreeModel treeModel;

	public FileTree() {
		super(new BorderLayout());
		File rootFile = new File(System.getProperty("user.dir"));
		DefaultMutableTreeNode rootNode = createTreeNodes(rootFile);
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.setCellRenderer(new FileTreeCellRenderer());

		tree.setComponentPopupMenu(new JPopupMenu());
		tree.addMouseListener(new TreeMouseListener());

		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON);
		tree.setTransferHandler(new TreeFileTransferHandler());

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane, BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode createTreeNodes(File fileRoot) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileRoot);
		if (fileRoot.isDirectory()) {
			File[] files = fileRoot.listFiles(f -> f.isDirectory() || f.getName().endsWith(".tree"));
			if (files != null) {
				for (File file : files) {
					node.add(createTreeNodes(file));
				}
			}
		}
		return node;
	}

	public void refreshTree() {
		File rootFile = new File(System.getProperty("user.dir"));
		DefaultMutableTreeNode newRoot = createTreeNodes(rootFile);
		treeModel.setRoot(newRoot);
		treeModel.reload();
	}

	private class TreeMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			handleMouseEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			handleMouseEvent(e);
		}

		private void handleMouseEvent(MouseEvent e) {
			var selPath = tree.getPathForLocation(e.getX(), e.getY());
			if (selPath == null) {
				selPath = new TreePath(treeModel.getRoot());
			}
			var selectedNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
			var file = (File) selectedNode.getUserObject();

			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
				if (!file.isDirectory()) {
					TreeStudio.frame.openTreeFile(file);
				}
			} else if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
				showPopupMenu(selectedNode, file, e.getX(), e.getY());
			}
		}
	}

	private void showPopupMenu(DefaultMutableTreeNode node, File file, int x, int y) {
		JPopupMenu menu = new JPopupMenu();
		boolean isRoot = node.getParent() == null;

		if (file.isDirectory()) {
			JMenuItem addFolder = new JMenuItem("Add Folder");
			JMenuItem addFile = new JMenuItem("Add File");
			JMenuItem rename = new JMenuItem("Rename");

			addFolder.addActionListener(ev -> createFileOrFolder(node, file, true, x, y));
			addFile.addActionListener(ev -> createFileOrFolder(node, file, false, x, y));
			rename.addActionListener(ev -> renameNode(node, file, x, y));

			menu.add(addFolder);
			menu.add(addFile);
			menu.addSeparator();
			menu.add(rename);

			if (!isRoot) {
				JMenuItem delete = new JMenuItem("Delete Folder");
				delete.addActionListener(ev -> {
					int confirm = JOptionPane.showConfirmDialog(null, "Delete this folder?", "Confirm", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						deleteRecursively(file);
						if (file.getAbsolutePath().equals(TreeStudio.frame.selectedTreeFile)) {
							TreeStudio.frame.closeTreeFile();
						}
						refreshTree();
					}
				});
				menu.addSeparator();
				menu.add(delete);
			}

		} else {
			JMenuItem open = new JMenuItem("Open File");
			JMenuItem rename = new JMenuItem("Rename");
			JMenuItem delete = new JMenuItem("Delete File");

			open.addActionListener(ev -> TreeStudio.frame.openTreeFile(file));
			rename.addActionListener(ev -> renameNode(node, file, x, y));
			delete.addActionListener(ev -> {
				int confirm = JOptionPane.showConfirmDialog(null, "Delete this file?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					file.delete();
					refreshTree();
				}
			});

			menu.add(open);
			menu.addSeparator();
			menu.add(rename);
			menu.addSeparator();
			menu.add(delete);
		}

		menu.show(tree, x, y);
	}

	private void createFileOrFolder(DefaultMutableTreeNode parentNode, File parentFile, boolean folder, int x, int y) {
		String name = showFloatingInput("Enter " + (folder ? "folder" : "file") + " name:", x, y, null);
		if (name == null || name.isBlank()) {
			return;
		}

		File newFile = new File(parentFile, name);
		try {
			boolean created;
			if (folder) {
				created = newFile.mkdir();
			} else {
				created = newFile.createNewFile();
			}

			if (created) {
				refreshTree();
			} else {
				JOptionPane.showMessageDialog(this, "Failed to create " + (folder ? "folder" : "file"));
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
		}
	}

	private void renameNode(DefaultMutableTreeNode node, File file, int x, int y) {
		String newName = showFloatingInput("Rename to:", x, y, file);
		if (newName == null || newName.isBlank()) {
			return;
		}

		File renamedFile = new File(file.getParentFile(), newName);
		var filePath = file.getAbsolutePath();
		boolean success = file.renameTo(renamedFile);
		if (success) {
			node.setUserObject(renamedFile);
			treeModel.nodeChanged(node);

			if (filePath.equals(TreeStudio.frame.selectedTreeFile)) {
				TreeStudio.frame.selectedTreeFile = renamedFile.getAbsolutePath();
				TreeStudio.frame.setTitle("TreeLibre - " + renamedFile.getName());
			}
		} else {
			JOptionPane.showMessageDialog(this, "Failed to rename file/folder");
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

	private String showFloatingInput(String message, int x, int y, File file) {
		JDialog dialog = new JDialog(TreeStudio.frame, "Input", true);
		dialog.setUndecorated(true);
		dialog.setLayout(new BorderLayout());

		JTextField textField = new JTextField(20);
		if (file != null) {
			textField.setText(file.getName());
		}

		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");

		JPanel panel = new JPanel();
		panel.add(ok);
		panel.add(cancel);

		dialog.add(new JLabel(message), BorderLayout.NORTH);
		dialog.add(textField, BorderLayout.CENTER);
		dialog.add(panel, BorderLayout.SOUTH);

		final String[] result = new String[1];

		ok.addActionListener(e -> {
			result[0] = textField.getText();
			dialog.dispose();
		});
		cancel.addActionListener(e -> dialog.dispose());

		dialog.pack();
		Point loc = tree.getLocationOnScreen();
		dialog.setLocation(loc.x + x, loc.y + y);
		dialog.setVisible(true);

		return result[0];
	}

	private class TreeFileTransferHandler extends TransferHandler {

		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop() || !support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return false;
			}

			JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
			TreePath path = dl.getPath();
			if (path == null) {
				return false;
			}

			DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) path.getLastPathComponent();
			File targetFile = (File) targetNode.getUserObject();
			return targetFile.isDirectory();
		}

		@Override
		public boolean importData(TransferSupport support) {
			if (!canImport(support)) {
				return false;
			}

			try {
				String draggedPath = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
				File draggedFile = new File(draggedPath);

				JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
				DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) dl.getPath().getLastPathComponent();
				File targetFile = (File) targetNode.getUserObject();

				File destFile = new File(targetFile, draggedFile.getName());
				boolean success = draggedFile.renameTo(destFile);
				if (success) {
					refreshTree();
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			TreePath[] paths = tree.getSelectionPaths();
			if (paths == null || paths.length == 0) {
				return null;
			}

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
			File file = (File) node.getUserObject();
			return new StringSelection(file.getAbsolutePath());
		}

		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.MOVE;
		}
	}
}
