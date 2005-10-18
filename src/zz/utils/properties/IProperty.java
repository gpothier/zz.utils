/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import zz.utils.IPublicCloneable;


/**
 * Suplement the lack of language support for properties.
 * It permits to encapsulate a class's field in an object that
 * let's the programmer overload getter and setter, and that
 * provides a notification mechanism.
 * See {@link zz.utils.properties.Example} to learn how to
 * use properties.
 * @author gpothier
 */
public interface IProperty<T> 
{
	/**
	 * Returns the id of this property, if it exists.
	 */
	public PropertyId<T> getId();
	
	/**
	 * Returns the object that owns this property, if any.
	 */
	public Object getOwner();

	
	/**
	 * Standard getter for this property.
	 */
	public T get();

	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * If the listener is a {@link IPropertyVeto}, the listener is also
	 * given an opportunity to reject changes.
	 * The property will maintains a weak reference to the listener,
	 * so the programmer should ensure that the listener is strongly
	 * referenced somewhere.
	 * In particular, this kind of construct should not be used:
	 * <pre>
	 * prop.addListener (new MyListener());
	 * </pre>
	 * In this case, use {@link #addHardListener(IPropertyListener)}
	 * instead.
	 */
	public void addListener (IPropertyListener<? super T> aListener);

	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * If the listener is a {@link IPropertyVeto}, the listener is also
	 * given an opportunity to reject changes.
	 * The listener will be referenced through a strong reference.
	 * @see #addListener(IPropertyListener)
	 */
	public void addHardListener (IPropertyListener<? super T> aListener);
	
	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * If the listener is a {@link IPropertyVeto}, the listener is also
	 * given an opportunity to reject changes.
	 * @param aHard The listener will be referenced through a strong or weak reference
	 * according to the value of this parameter
	 * @see #addListener(IPropertyListener)
	 * @see #addHardListener(IPropertyListener)
	 */
	public void addListener (IPropertyListener<? super T> aListener, boolean aHard);
	
	/**
	 * Removes a previously added listener.
	 */
	public void removeListener (IPropertyListener<? super T> aListener);

	/**
	 * Creates a clone of this property, giving the cloned property the specified
	 * container.
	 * The clone has no listeners or vetoers.
	 * @param aCloneValue Whether to clone the value of the property.
	 */
	public IProperty<T> cloneForOwner (Object aOwner, boolean aCloneValue);

}
