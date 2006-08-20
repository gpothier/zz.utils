/*
 * Created on Aug 10, 2006
 */
package zz.utils.cache;

import java.util.HashMap;
import java.util.Map;

import zz.utils.list.NakedLinkedList;
import zz.utils.list.NakedLinkedList.Entry;

/**
 * A buffer that keeps the most recently used items in memory.
 * Subclasses can be notified of dropped items by overriding
 * the {@link #drop(Object)} method.
 * @author gpothier
 */
public abstract class MRUBuffer<K, V>
{
	private int itsCacheSize;
	
	private Map<K, Entry<V>> itsCache = new HashMap<K, Entry<V>>();
	
	/**
	 * Most recently used items list
	 */
	private NakedLinkedList<V> itsItemsList = new NakedLinkedList<V>();
	
	public MRUBuffer(int aCacheSize)
	{
		itsCacheSize = aCacheSize;
	}

	/**
	 * Returns the key that corresponds to the given value.
	 */
	protected abstract K getKey(V aValue);
	
	private void use(Entry<V> aEntry)
	{
		if (aEntry.isAttached()) itsItemsList.remove(aEntry);
		itsItemsList.addLast(aEntry);
		
		if (shouldDrop(itsItemsList.size()))
		{
			Entry<V> theFirst = itsItemsList.getFirstEntry();
			itsItemsList.remove(theFirst);
			
			V theValue = theFirst.getValue();
			drop(theValue);
			itsCache.remove(getKey(theValue));
		}			
	}

	/**
	 * Determines if the oldest element should be dropped, given the current number
	 * of cached items.
	 */
	protected boolean shouldDrop(int aCachedItems)
	{
		return aCachedItems >= itsCacheSize;
	}
	
	/**
	 * This method is called when a dirty item is about to
	 * be dropped. 
	 * This method does nothing by default.
	 */
	protected void drop(V aValue)
	{
	}

	/**
	 * Called when a value that was not previously in the cache
	 * is now included in thecache.
	 * This method does nothing by default.
	 */
	protected void added(V aValue)
	{
	}
	
	/**
	 * Retrieves an item from this buffer. If the item is not in
	 * the buffer, it is fetched with the {@link #fetch(Object)} method.
	 */
	public V get(K aKey)
	{
		return get(aKey, true);
	}

	/**
	 * Retrieves an item from this buffer. If the item is not in the buffer
	 * and aFetch is true, then the item is fetched with the {@link #fetch(Object)}
	 * method. Otherwise, the method returns null.
	 */
	public V get(K aKey, boolean aFetch)
	{
//		System.out.println("get node: "+aId);
		Entry<V> theEntry = itsCache.get(aKey);
		if (theEntry == null && aFetch)
		{
			V theValue = fetch(aKey);
			theEntry = new Entry<V>(theValue);
			itsCache.put(aKey, theEntry);
			added(theValue);
		}
		
		if (theEntry != null) use(theEntry);
		
		return theEntry != null ? theEntry.getValue() : null;
	}
	
	/**
	 * Forcefully adds an item to this cache
	 */
	public void add(V aValue)
	{
		Entry<V> theEntry = new Entry<V>(aValue);
		itsCache.put(getKey(aValue), theEntry);
		added(aValue);
		use(theEntry);
	}
	
	/**
	 * Fetches the value identified by the given key.
	 */
	protected abstract V fetch(K aId);
}
