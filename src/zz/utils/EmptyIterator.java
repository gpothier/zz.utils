/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: 21 févr. 02
 * Time: 15:10:18
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An empty iterator.
 */
public class EmptyIterator implements Iterator
{

	public static final EmptyIterator SINGLETON = new EmptyIterator();

	private EmptyIterator ()
	{
	}

	public boolean hasNext ()
	{
		return false;
	}

	public Object next ()
	{
		throw new NoSuchElementException ();
	}

	public void remove ()
	{
		throw new UnsupportedOperationException ();
	}
}
