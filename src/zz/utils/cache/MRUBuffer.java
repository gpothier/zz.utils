/*
 * Created on Aug 10, 2006
 */
package zz.utils.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * A buffer that keeps the most recently used items in memory.
 * Items can also be marked as dirty, in which case they are
 * saved prior to being dropped.
 * @author gpothier
 */
public abstract class MRUBuffer<K, V>
{
	private int itsCacheSize;
	
	private Map<K, V> itsCachedItems = new HashMap<K, V>();
	private Set<V> itsDirtyItems = new HashSet<V>();
	
	/**
	 * Most recently used items list
	 */
	private LinkedList<V> itsItemsList = new LinkedList<V>();
	
	public MRUBuffer(int aCacheSize)
	{
		itsCacheSize = aCacheSize;
	}

	/**
	 * Returns the key that corresponds to the given value.
	 */
	protected abstract K getKey(V aValue);
	
	public void markNode(V aValue)
	{
//		System.out.println("mark node: "+aNode.getPageId());
		itsDirtyItems.add(aValue);
		itsCachedItems.put(getKey(aValue), aValue);
		use(aValue);
	}
	
	private void use(V aValue)
	{
		itsItemsList.remove(aValue);
		itsItemsList.addLast(aValue);
		
		if (itsItemsList.size() > itsCacheSize)
		{
			V theValue = itsItemsList.removeFirst();
			if (itsDirtyItems.remove(theValue)) saveNode(theValue);
			itsCachedItems.remove(getKey(theValue));
		}			
	}

	
	/**
	 * Saves an item. This method is called when a dirty item is about to
	 * be dropped.
	 */
	protected void saveNode(V aValue)
	{
		throw new UnsupportedOperationException();
	}
	
	public V get(K aKey)
	{
//		System.out.println("get node: "+aId);
		V theValue = itsCachedItems.get(aKey);
		if (theValue == null)
		{
			theValue = fetch(aKey);
			itsCachedItems.put(aKey, theValue);
		}
		use(theValue);
		return theValue;
	}
	
	/**
	 * Fetches the value identified by the given key.
	 */
	protected abstract V fetch(K aId);
}
