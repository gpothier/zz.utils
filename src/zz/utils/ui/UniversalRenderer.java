/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 30 oct. 2002
 * Time: 18:09:53
 */
package zz.utils.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public abstract class UniversalRenderer<V> implements ListCellRenderer, TreeCellRenderer, TableCellRenderer
{
	private ListCellRenderer itsListCellRenderer;
	private TreeCellRenderer itsTreeCellRenderer;
	private TableCellRenderer itsTableCellRenderer;
	
	private boolean itsOpaque;
	private int itsHorizontalAlignment;
	private int itsVerticalAlignment;

	public UniversalRenderer()
	{
		this (true);
	}

	/**
	 * Creates a renderer.
	 * @param aOpaque Indicates if this renderer should produce opaque components.
	 */
	public UniversalRenderer(boolean aOpaque)
	{
		this (aOpaque, JLabel.LEADING, JLabel.CENTER);
	}
	
	public UniversalRenderer(
			boolean aOpaque, 
			int aHorizontalAlignment, 
			int aVerticalAlignment)
	{
		itsOpaque = aOpaque;
		itsHorizontalAlignment = aHorizontalAlignment;
		itsVerticalAlignment = aVerticalAlignment;
	}
	
	/**
	 * Initial setup of the renderers.
	 */
	private void setupRenderer(JLabel aRenderer)
	{
		aRenderer.setHorizontalAlignment(itsHorizontalAlignment);
		aRenderer.setVerticalAlignment(itsVerticalAlignment);
	}
	
	protected ListCellRenderer createListCellRenderer ()
	{
		DefaultListCellRenderer theRenderer = new DefaultListCellRenderer ();
		setupRenderer(theRenderer);
		theRenderer.setOpaque(itsOpaque);
		return theRenderer;
	}

	protected TreeCellRenderer createTreeCellRenderer ()
	{
		DefaultTreeCellRenderer theRenderer = new DefaultTreeCellRenderer ();
		setupRenderer(theRenderer);
		// Setting the opacity on tree renderers causes probles with selection.
		return theRenderer;
	}

	protected TableCellRenderer createTableCellRenderer ()
	{
		DefaultTableCellRenderer theRenderer = new DefaultTableCellRenderer ();
		setupRenderer(theRenderer);
		theRenderer.setOpaque(itsOpaque);
		return theRenderer;
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
	
	public Component getTableCellRendererComponent(
			JTable aTable, 
			Object aValue, 
			boolean aIsSelected,
			boolean aHasFocus, 
			int aRow, 
			int aColumn)
	{
		if (itsTableCellRenderer == null) itsTableCellRenderer = createTableCellRenderer();

		JLabel theLabel = (JLabel) itsTableCellRenderer.getTableCellRendererComponent(aTable, aValue, aIsSelected, aHasFocus, aRow, aColumn);

		setupLabel(theLabel, aValue);

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
	protected abstract String getName (V aObject);

	/**
	 * Returns an optional tooltip text
	 * @param aObject The object that is being rendered
	 */
	protected String getToolTipText (V aObject)
	{
		return null;
	}

	/**
	 * Returns an optional icon
	 * @param aObject The object that is being rendered
	 */
	protected Icon getIcon (V aObject)
	{
		return null;
	}

	/**
	 * Returns an optional color for the text.
	 * @param aObject The object that is being rendered
	 */
	protected Color getTextColor (V aObject)
	{
		return null;
	}
	
	/**
	 * Returns an optional background color.
	 * @param aObject The object that is being rendered.
	 */
	protected Color getBackgroundColor (V aObject)
	{
		return null;
	}


	/**
	 * This method sets up the specified label so that it renders properly the specified object.
	 * @param aLabel A label to set up.
	 */
	protected void setupLabel (JLabel aLabel, Object aObject)
	{
		V theObject = (V) aObject;
		
		String theName = getName(theObject);
		aLabel.setText(theName != null ? theName : "");

		String theToolTipText = getToolTipText(theObject);
		aLabel.setToolTipText(theToolTipText);

		Icon theIcon = getIcon(theObject);
		aLabel.setIcon(theIcon);

		Color theTextColor = getTextColor (theObject);
		if (theTextColor != null) aLabel.setForeground(theTextColor);
		
		Color theBackgroundColor = getBackgroundColor(theObject);
		if (theBackgroundColor != null) aLabel.setBackground(theBackgroundColor);
	}
}
