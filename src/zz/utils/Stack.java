/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: 21 mars 02
 * Time: 15:33:57
 * To change template for new interface use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils;

import java.util.Iterator;

public interface Stack<T>
{
	public void push (T aObject);
	public T pop ();
	public T peek ();
	public void clear ();
	public int size ();
	public boolean isEmpty ();
	
	/**
	 * Returns an iterator over the elements of this stack, starting from the bottom. 
	 */
	public Iterator<T> iterator ();
}
