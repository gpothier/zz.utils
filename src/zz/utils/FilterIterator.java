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
public class FilterIterator<T> implements Iterator<T>, Iterable<T>
{

	private Iterator<T> itsIterator;
	private T itsNext;
	private Filter<T> itsFilter;

	/**
	 *
	 * @param aCollection A collection from which to retrieve the underlying iterator
	 * @param aFilter The filter.
	 */
	public FilterIterator (Collection<T> aCollection, Filter<T> aFilter)
	{
		this (aCollection.iterator(), aFilter);
	}

	public FilterIterator (Iterator<T> aIterator, Filter<T> aFilter)
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
			T t = itsIterator.next();
			if (! itsFilter.accept(t)) continue;
			itsNext = t;
			break;
		}
	}

	public boolean hasNext ()
	{
		return itsNext != null;
	}

	public T next ()
	{
		if (hasNext())
		{
			T theResult = itsNext;
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

	public static <T> int size (Collection<T> aCollection, Filter<T> aFilter)
	{
		return new FilterIterator<T> (aCollection, aFilter).getRemainingItems();
	}

	public Iterator<T> iterator()
	{
		return this;
	}
	
	public interface Filter<T>
	{
		public boolean accept (T aObject);
	}
	
	public static class ClassFilter implements Filter<Object>
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
