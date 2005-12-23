/*
 * Created on Dec 21, 2004
 */
package zz.utils.tree;

import zz.utils.properties.ArrayListProperty;
import zz.utils.properties.IListProperty;
import zz.utils.properties.IRWProperty;
import zz.utils.properties.SimpleRWProperty;

/**
 * Simple tree node implementation.
 * Instances are obtained through {@link zz.utils.tree.SimpleTree#createNode(V)}
 * @author gpothier
 */
public class SimpleTreeNode<V>
{
	private SimpleTree<V> itsTree;
	private SimpleTreeNode<V> itsParent;
	
	/**
	 * The children of this node. If this property is null, there are no children.
	 */
	private final IListProperty<SimpleTreeNode<V>> pChildren;
	
	private final IRWProperty<V> pValue =
		new SimpleRWProperty<V>(this)
		{
			public void set(V aValue)
			{
				super.set(aValue); 
				//TODO: cast for compilation problem 
				getTree().fireValueChanged((SimpleTreeNode<V>) SimpleTreeNode.this, aValue);
			}
		};
	
	
	protected SimpleTreeNode(SimpleTree<V> aTree, boolean aLeaf)
	{
		itsTree = aTree;
		if (! aLeaf)
		{
			pChildren = 
				new ArrayListProperty<SimpleTreeNode<V>>(this)
				{
					protected void elementAdded(int aIndex, SimpleTreeNode<V> aElement)
					{
						if (aElement.getParent() != null) throw new RuntimeException("node already has a parent: "+aElement);
						//TODO: cast for compilation problem 
						aElement.setParent((SimpleTreeNode<V>) SimpleTreeNode.this);
						//TODO: cast for compilation problem 
						getTree().fireChildAdded((SimpleTreeNode<V>) SimpleTreeNode.this, aIndex, aElement);
					}
					
					protected void elementRemoved(int aIndex, SimpleTreeNode<V> aElement)
					{
						if (aElement.getParent() != SimpleTreeNode.this) throw new RuntimeException("node has awrong parent: "+aElement);
						aElement.setParent(null);
						//TODO: cast for compilation problem 
						getTree().fireChildRemoved((SimpleTreeNode<V>) SimpleTreeNode.this, aIndex, aElement);
					}
				};
		}
		else pChildren = null;
	}
	
	public SimpleTree<V> getTree()
	{
		return itsTree;
	}
	
	public SimpleTreeNode<V> getParent()
	{
		return itsParent;
	}
	
	public void setParent(SimpleTreeNode<V> aParent)
	{
		itsParent = aParent;
	}
	
	public IRWProperty<V> pValue()
	{
		return pValue;
	}
	
	public IListProperty<SimpleTreeNode<V>> pChildren()
	{
		return pChildren;
	}
}
