/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Jan 14, 2002
 * Time: 4:21:53 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Wraps a ListIterator to act as a reverse Iterator.
 */
public class ReverseIteratorWrapper implements Iterator
{
	protected ListIterator itsListIterator;

	public ReverseIteratorWrapper (ListIterator aListIterator)
	{
		itsListIterator = aListIterator;
	}

	/**
	 * Creates a reverse iterator on the specified list.
	 */
	public ReverseIteratorWrapper (List aList)
	{
		this (aList.listIterator(aList.size()));
	}

	public boolean hasNext ()
	{
		return itsListIterator.hasPrevious();
	}

	public Object next ()
	{
		return itsListIterator.previous();
	}

	public void remove ()
	{
		itsListIterator.remove();
	}
}
