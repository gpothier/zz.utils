/**
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Dec 7, 2002
 * Time: 3:03:17 AM
 */
package zz.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that returns a subset of the elements of the underlying iterator.
 * Only elements for which {@link Filter#accept} returns true are returned by
 * this iterator's {@link #next} method.
 */
public class FilterIterator implements Iterator
{

	private Iterator itsIterator;
	private Object itsNext;
	private Filter itsFilter;

	/**
	 *
	 * @param aCollection A collection from which to retrieve the underlying iterator
	 * @param aFilter The filter.
	 */
	public FilterIterator (Collection aCollection, Filter aFilter)
	{
		this (aCollection.iterator(), aFilter);
	}

	public FilterIterator (Iterator aIterator, Filter aFilter)
	{
		itsIterator = aIterator;
		itsFilter = aFilter;
		findNext();
	}

	private void findNext ()
	{
		itsNext = null;
		while (itsIterator.hasNext())
		{
			Object o = itsIterator.next();
			if (! itsFilter.accept(o)) continue;
			itsNext = o;
			break;
		}
	}

	public boolean hasNext ()
	{
		return itsNext != null;
	}

	public Object next ()
	{
		if (hasNext())
		{
			Object theResult = itsNext;
			findNext();
			return theResult;
		}
		else throw new NoSuchElementException();
	}

	public void remove ()
	{
		itsIterator.remove();
	}

	/**
	 * Returns the number of times a call to {@link #next} would return a value.
	 */
	public int getRemainingItems ()
	{
		int theResult = 0;
		while (hasNext()) theResult++;
		return theResult;
	}

	public static int size (Collection aCollection, Filter aFilter)
	{
		return new FilterIterator (aCollection, aFilter).getRemainingItems();
	}

	public interface Filter
	{
		public boolean accept (Object aObject);
	}
	
	public static class ClassFilter implements Filter
	{
		private Class itsClass;
		
		public ClassFilter (Class aClass)
		{
			itsClass = aClass;
		}
		
		public boolean accept(Object aObject)
		{
			return aObject == null || itsClass.isAssignableFrom(aObject.getClass());
		}
	}
}
