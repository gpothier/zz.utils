/*
 * Created on Dec 21, 2004
 */
package zz.utils.tree;

import zz.utils.properties.AbstractTree;
import zz.utils.properties.ITree;

/**
 * Simple implementation of {@link zz.utils.properties.ITree}, based
 * on {@link zz.utils.tree.SimpleTreeNode}.
 * @author gpothier
 */
public class SimpleTree<V> extends AbstractTree<SimpleTreeNode<V>, V>
{
	private SimpleTreeNode<V> itsRootNode;

	public SimpleTree(SimpleTreeNode<V> aRootNode)
	{
		itsRootNode = aRootNode;
	}

	public SimpleTreeNode<V> getRoot()
	{
		return itsRootNode;
	}

	public SimpleTreeNode<V> getParent(SimpleTreeNode<V> aNode)
	{
		return aNode.getParent();
	}

	public int getChildCount(SimpleTreeNode<V> aParent)
	{
		return aParent.pChildren.size();
	}

	public SimpleTreeNode<V> getChild(SimpleTreeNode<V> aParent, int aIndex)
	{
		return aParent.pChildren.get(aIndex);
	}
	
	public Iterable<SimpleTreeNode<V>> getChildren(SimpleTreeNode<V> aParent)
	{
		return aParent.pChildren;
	}

	public int getIndexOfChild(SimpleTreeNode<V> aParent, SimpleTreeNode<V> aChild)
	{
		return aParent.pChildren.indexOf(aChild);
	}

	public V getValue(SimpleTreeNode<V> aNode)
	{
		return aNode.pValue.get();
	}

	/**
	 * Creates a new nod for this tree, without adding it to the tree.
	 */
	public SimpleTreeNode<V> createNode(V aValue)
	{
		return new SimpleTreeNode<V>(this);
	}
	
	/**
	 * Adds a child node to a node.
	 */
	public void addChild(SimpleTreeNode<V> aParent, int aIndex, SimpleTreeNode<V> aChild)
	{
		aParent.pChildren.add(aIndex, aChild);
	}

	/**
	 * Removes a child node from a node.
	 */
	public void removeChild(SimpleTreeNode<V> aParent, int aIndex)
	{
		aParent.pChildren.remove(aIndex);
	}
	
}
