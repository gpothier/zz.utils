/*
 * Created on Dec 21, 2004
 */
package zz.utils.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import zz.utils.Formatter;
import zz.utils.properties.ITree;

/**
 * Builds a swing menu based on a {@link zz.utils.properties.ITree}
 * @author gpothier
 */
public class MenuBuilder
{
	/**
	 * Builds a popup menu based on the structure of the given tree.
	 * The selection of a menu item calls {@link IMenuListener#menuItemSelected(V)} on
	 * the specified listener.
	 * <p>
	 * Only leaf nodes are selectable.
	 * 
	 * @param <N> Type of node in the tree
	 * @param <V> Type of values.
	 * @param aTree The tree whose structure is used to build the menu
	 * @param aListener The listener that is notified when a menu item is selected.
	 * @param aFormatter The formatter that provide texts for the menu items.
	 * @return The created menu.
	 */
	public static <N, V> JPopupMenu buildPopupMenu (
			ITree<N, V> aTree, 
			IMenuListener<V> aListener,
			Formatter<V> aFormatter)
	{
		JPopupMenu thePopupMenu = new JPopupMenu();
		N theRoot = aTree.getRoot();
		
		if (! aTree.isLeaf(theRoot)) for (N theChild : aTree.getChildren(theRoot))
			buildNode(aTree, thePopupMenu, theChild, aListener, aFormatter);
		
		return thePopupMenu;
	}
	
	/**
	 * Adds a new child menu item to the specified parent component,
	 * representing the specified child node.
	 */
	private static <N, V> void buildNode (
			ITree<N, V> aTree, 
			JComponent aParentComponent,
			N aChildNode,
			final IMenuListener<V> aListener,
			Formatter<V> aFormatter)
	{
		JMenuItem theMenuItem;
		final V theValue = aTree.getValue(aChildNode);
		
		if (aTree.isLeaf(aChildNode))
		{
			theMenuItem = new JMenuItem();
			theMenuItem.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent aE)
						{
							aListener.menuItemSelected(theValue);
						}
					});
		}
		else
		{
			theMenuItem = new JMenu();
			for (N theChild : aTree.getChildren(aChildNode))
				buildNode(aTree, theMenuItem, theChild, aListener, aFormatter);
		}
		
		theMenuItem.setText(aFormatter.getHtmlText(theValue));
		
		aParentComponent.add (theMenuItem);
	}
	
	/**
	 * Interface for objects that listen for selection in menus.
	 * @author gpothier
	 */
	public interface IMenuListener<V> 
	{
		public void menuItemSelected (V aItemValue);
	}
}
