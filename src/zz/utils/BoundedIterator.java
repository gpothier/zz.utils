/**
 * Created by IntelliJ IDEA.
 * User: g1
 * Date: 11 mars 2004
 * Time: 22:29:44
 * To change this template use File | Settings | File Templates.
 */
package zz.utils;

import java.util.Iterator;

/**
 * Wraps an iterator so that will iterate over a fixed number of elements.
 * The wrapped iterator should be able to produce at least the specified
 * number of elements.
 * @author gpothier
 */
public class BoundedIterator implements Iterator
{
	/**
	 * The wrapped iterator.
	 */
	private Iterator itsIterator;

	/**
	 * The number of elements to produce
	 */
	private int itsCount;

	public BoundedIterator (Iterator aIterator, int aCount)
	{
		itsIterator = aIterator;
		itsCount = aCount;
	}

	public void remove()
	{
		throw new UnsupportedOperationException ();
	}

	public boolean hasNext()
	{
		assert itsIterator.hasNext() || (itsCount == 0);
		return itsCount > 0;
	}

	public Object next()
	{
		assert hasNext();
		itsCount--;
		return itsIterator.next();
	}
}
