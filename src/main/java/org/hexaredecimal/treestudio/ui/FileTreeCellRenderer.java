package org.hexaredecimal.treestudio.ui;

import java.awt.Component;
import java.io.File;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.hexaredecimal.treestudio.utils.Icons;

/**
 *
 * @author hexaredecimal
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
					boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		Object userObject = node.getUserObject();

		if (userObject instanceof File file) {
			if (file.isDirectory()) {
				setIcon(Icons.getIcon("folder"));
			} else if (file.getName().endsWith(".pics")) {
				setIcon(Icons.getIcon("code-file"));
			} else if (file.getName().endsWith(".md")) {
				setIcon(Icons.getIcon("markdown"));
			} else {
				setIcon(Icons.getIcon("file"));
			}
			setText(file.getName());
		}
		return this;
	}
}
