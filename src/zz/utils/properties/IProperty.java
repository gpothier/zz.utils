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
	 * Standard getter for this property.
	 */
	public T get();

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
	 * In this case, use {@link #addHardListener(IPropertyListener)}
	 * instead.
	 */
	public void addListener (IPropertyListener<? super T> aListener);

	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * The listener will be referenced through a strong reference.
	 * @see #addListener(IPropertyListener)
	 */
	public void addHardListener (IPropertyListener<? super T> aListener);
	
	/**
	 * Removes a previously added listener.
	 */
	public void removeListener (IPropertyListener<? super T> aListener);

	/**
	 * Adds a veto that can reject a new value for this property.
	 * See the comment on {@link #addListener(IPropertyListener)}
	 * about the referencing scheme.
	 */
	public void addVeto (IPropertyVeto<? super T> aVeto);

	/**
	 * Adds a veto that can reject a new value for this property.
	 * See the comment on {@link #addListener(IPropertyListener)}
	 * about the referencing scheme.
	 */
	public void addHardVeto (IPropertyVeto<? super T> aVeto);
	
	/**
	 * Removes a previously added veto.
	 */
	public void removeVeto (IPropertyVeto<? super T> aVeto);
	
	/**
	 * Creates a clone of this property, giving the cloned property the specified
	 * container.
	 * The value of the property is not deeply cloned.
	 * The clone has no listeners or vetoers.
	 */
	public IProperty<T> cloneForContainer (Object aContainer);

}
