/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A property whose value is a collection. Additional features are:
 * <li>Collection manipulation methods
 * <li>List listeners.
 * <li>Implements the {@link Iterable} interface.
 * 
 * @param <C> The collection type
 * @param <E> The element type
 * @author gpothier
 */
public interface ICollectionProperty<C extends Collection<E>, E> 
extends IProperty<C>, Iterable<E>
{
	/**
	 * Returns the number of elements in the collection.
	 */
	public int size();
	
	public void add (E aElement);
	
	/**
	 * Removes the specified element
	 * @return Whether the element was in the list.
	 */
	public boolean remove (E aElement);
	
	/**
	 * Removes all the elements of this list.
	 */
	public void clear();
	
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
	public void addListener (ICollectionPropertyListener<E> aListener);

	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * The listener will be referenced through a strong reference.
	 * @see #addListener(IPropertyListener)
	 */
	public void addHardListener (ICollectionPropertyListener<E> aListener);
	
	/**
	 * Removes a previously added listener.
	 */
	public void removeListener (ICollectionPropertyListener<E> aListener);


}
