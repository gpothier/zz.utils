/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Feb 18, 2002
 * Time: 2:54:33 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * A Map in which all values are Lists, so that a single key
 * can map to any number of values.
 */
public class ListMap extends HashMap
{
	/**
	 * Adds aValue the the list of the specified key.
	 * @return The list into which the value was added.
	 */
	public List add (Object aKey, Object aValue)
	{
		List theList = getList (aKey);
		if (theList == null)
		{
			theList = new AwareList (aKey);
			put (aKey, theList);
		}

		theList.add (aValue);
		return theList;
	}

	/**
	 * Tries to remove aValue from the list of the specified key.
	 * @return True if aValue was present in the list.
	 */
	public boolean remove (Object aKey, Object aValue)
	{
		List theList = getList (aKey);
		if (theList == null)
			return false;
		else
			return theList.remove (aValue);
	}


	/**
	 * Removes all occurences of aValue. Returns the number of occurences removed
	 */
	public int removeAll (Object aValue)
	{
		int theResult = 0;
		for (Iterator theIterator = values ().iterator (); theIterator.hasNext ();)
		{
			List theList = (List) theIterator.next ();
			while (theList.remove (aValue)) theResult++;
		}
		return theResult;
	}

	public List getList (Object aKey)
	{
		return (List) get (aKey);
	}

	public Iterator iterator (Object aKey)
	{
		List theList = getList (aKey);
		if (theList == null) return EmptyIterator.SINGLETON;
		else return theList.iterator ();
	}

	/**
	 * A list that knows which key references it.
	 * It removes the corresponding entry when its size reaches 0.
	 */
	private class AwareList extends ArrayList
	{
		private Object itsKey;

		public AwareList (Object aKey)
		{
			itsKey = aKey;
		}

		public boolean remove (Object o)
		{
			boolean theResult = super.remove (o);
			checkEmpty();
			return theResult;
		}

		public boolean removeAll (Collection c)
		{
			boolean theResult = super.removeAll (c);
			checkEmpty();
			return theResult;
		}

		public Object remove (int index)
		{
			Object theResult = super.remove (index);
			checkEmpty();
			return theResult;
		}

		protected void removeRange (int fromIndex, int toIndex)
		{
			super.removeRange (fromIndex, toIndex);
			checkEmpty();
		}

		public boolean retainAll (Collection c)
		{
			boolean theResult = super.retainAll (c);
			checkEmpty();
			return theResult;
		}

		public void clear ()
		{
			super.clear ();
			checkEmpty();
		}

		private void checkEmpty ()
		{
			if (size() == 0) ListMap.this.remove (itsKey);
		}
	}
}
