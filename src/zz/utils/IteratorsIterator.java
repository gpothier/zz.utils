/**
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Jun 14, 2003
 * Time: 7:06:42 PM
 */
package zz.utils;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This iterator iterates over all the objects of a set of iterators.
 */
public class IteratorsIterator implements Iterator
{
	/**
	 * An iterator over iterators
	 */
	private Iterator itsIteratorsIterator;

	private Iterator itsCurrentIterator = null;

	private boolean itsDone = false;

	/**
	 * @param aIteratorsIterator An iterator over iterators.
	 */
	public IteratorsIterator (Iterator aIteratorsIterator)
	{
		itsIteratorsIterator = aIteratorsIterator;
	}

	private void seek ()
	{
		if (! itsDone && (itsCurrentIterator == null || ! itsCurrentIterator.hasNext()))
		{
			while (itsIteratorsIterator.hasNext())
			{
				itsCurrentIterator = (Iterator) itsIteratorsIterator.next();
				if (itsCurrentIterator.hasNext()) return;
			}
			itsDone = true;
		}
	}

	/**
	 * @param aIteratorsList A list of iterators.
	 */
	public IteratorsIterator (List aIteratorsList)
	{
		this (aIteratorsList.iterator());
	}

	public boolean hasNext ()
	{
		seek();
		return ! itsDone;
	}

	public Object next ()
	{
		seek ();
		if (itsDone) throw new NoSuchElementException();
		else return itsCurrentIterator.next();
	}

	public void remove ()
	{
		if (itsCurrentIterator != null) itsCurrentIterator.remove ();
		else throw new IllegalStateException();
	}
}
