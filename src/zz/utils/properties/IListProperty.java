/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.Iterator;
import java.util.List;

/**
 * A property whose value is a list. Additional features are:
 * <li>List manipulation methods
 * <li>List listeners.
 * <li>Implements the {@link Iterable} interface.
 * 
 * @author gpothier
 */
public interface IListProperty<E> extends IProperty<List<E>>, Iterable<E>
{
	/**
	 * Returns the number of elements in the list.
	 */
	public int size();
	
	/**
	 * Returns the element at the specified index.
	 */
	public E get (int aIndex);
	
	public void add (E aElement);
	public void add (int aIndex, E aElement);
	public void set (int aIndex, E aElement);
	
	/**
	 * Returns the index of the specified element,
	 * or -1 if it is not present in the list.
	 */
	public int indexOf (E aElement);
	
	/**
	 * Removes the specified element
	 * @return Whether the element was in the list.
	 */
	public boolean remove (E aElement);
	
	/**
	 * Removes the element at the specified index.
	 * @return The removed element. 
	 */
	public E remove (int aIndex);
	
	/**
	 * Removes all the elements of this list.
	 */
	public void clear();
	
	/**
	 * Returns an iterator over all the elements of this  container,
	 * in reverse order.
	 */
	public Iterator<E> reverseIterator ();


	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * The property will maintains a weak reference to the listener,
	 * so the programmer should ensure that the listener is strongly
	 * referenced somewhere.
	 * In particular, this kind of construct should not be used:
	 * <pre>
	 * prop.addListener (new MyListener());
	 * </pre>
	 * In this case, use {@link #addHardListener(IListPropertyListener)}
	 * instead.
	 */
	public void addListener (IListPropertyListener<E> aListener);

	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * The listener will be referenced through a strong reference.
	 * @see #addListener(IPropertyListener)
	 */
	public void addHardListener (IListPropertyListener<E> aListener);
	
	/**
	 * Removes a previously added listener.
	 */
	public void removeListener (IListPropertyListener<E> aListener);


}
