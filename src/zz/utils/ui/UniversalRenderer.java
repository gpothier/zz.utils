/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 30 oct. 2002
 * Time: 18:09:53
 */
package zz.utils.ui;

import java.awt.*;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public abstract class UniversalRenderer implements ListCellRenderer, TreeCellRenderer
{
	private ListCellRenderer itsListCellRenderer;
	private TreeCellRenderer itsTreeCellRenderer;

	protected ListCellRenderer createListCellRenderer ()
	{
		return new DefaultListCellRenderer ();
	}

	protected TreeCellRenderer createTreeCellRenderer ()
	{
		return new DefaultTreeCellRenderer ();
	}

	public Component getListCellRendererComponent (
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
	{
		if (itsListCellRenderer == null) itsListCellRenderer = createListCellRenderer();

		JLabel theLabel = (JLabel) itsListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value != null) setupLabel(theLabel, value);

		return theLabel;
	}

	public Component getTreeCellRendererComponent (JTree tree, Object value,
												   boolean selected, boolean expanded,
												   boolean leaf, int row, boolean hasFocus)
	{
		if (itsTreeCellRenderer == null) itsTreeCellRenderer = createTreeCellRenderer();

		JLabel theLabel = (JLabel) itsTreeCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		if (value instanceof DefaultMutableTreeNode)
		{
			DefaultMutableTreeNode theNode = (DefaultMutableTreeNode) value;
			value = theNode.getUserObject();
		}
		if (value != null) setupLabel(theLabel, value);

		return theLabel;
	}

	/**
	 * Returns the text to display in the control.
	 * @param aObject The object that is being rendered
	 */
	protected abstract String getName (Object aObject);

	/**
	 * Returns an optional tooltip text
	 * @param aObject The object that is being rendered
	 */
	protected String getToolTipText (Object aObject)
	{
		return null;
	}

	/**
	 * Returns an optional icon
	 * @param aObject The object that is being rendered
	 */
	protected Icon getIcon (Object aObject)
	{
		return null;
	}

	/**
	 * Returns an optional color for the text.
	 * @param aObject The object that is being rendered
	 */
	protected Color getTextColor (Object aObject)
	{
		return null;
	}


	/**
	 *  This method sets up the specified label so that it renders properly the specified object.
	 * @param aLabel A label to set up.
	 */
	protected void setupLabel (JLabel aLabel, Object aObject)
	{
		String theName = getName(aObject);
		if (theName != null) aLabel.setText(theName);

		String theToolTipText = getToolTipText(aObject);
		if (theToolTipText != null) aLabel.setToolTipText(theToolTipText);

		Icon theIcon = getIcon(aObject);
		if (theIcon != null) aLabel.setIcon(theIcon);

		Color theTextColor = getTextColor (aObject);
		if (theTextColor != null) aLabel.setForeground(theTextColor);
	}
}
