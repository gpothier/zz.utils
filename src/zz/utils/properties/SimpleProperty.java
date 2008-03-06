/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import java.awt.geom.Rectangle2D;

import zz.utils.IPublicCloneable;
import zz.utils.notification.ObservationCenter;
import zz.utils.notification.Observer;

/**
 * Default implementation of {@link zz.utils.properties.IProperty} for simple values.
 * @author gpothier
 */
public class SimpleProperty<T> extends AbstractProperty<T> 
implements IProperty<T>, Observer
{
	
	/**
	 * The actual value of the property
	 */
	private T itsValue;
	
	public SimpleProperty()
	{
	}
	
	public SimpleProperty(Object aOwner)
	{
		super (aOwner);
	}
	
	public SimpleProperty(Object aOwner, T aValue)
	{
		super (aOwner);
		itsValue = aValue;
	}
	
	public SimpleProperty(Object aOwner, PropertyId<T> aPropertyId)
	{
		super (aOwner, aPropertyId);
	}
	
	public SimpleProperty(Object aOwner, PropertyId<T> aPropertyId, T aValue)
	{
		super (aOwner, aPropertyId);
		itsValue = aValue;
	}

	/**
	 * Standard getter for this property.
	 */
	public T get()
	{
		return get0();
	}

	
	/**
	 * Internal getter for the property.
	 */
	protected final T get0()
	{
		return itsValue;
	}

	/**
	 * Indicates if two values are equal.
	 */
	protected boolean equalValues(T aValue1, T aValue2)
	{
		return aValue1 == aValue2;
	}
	
	/**
	 * Internal setter for the property.
	 * It first checks if a veto rejects the new value. If not, it
	 * sets the current value and fires notifications.
	 * @param aValue The new value of the property.
	 */
	protected final void set0(T aValue)
	{
		T theOldValue = get0();
		if (equalValues(theOldValue, aValue)) return;
		
		Object theCanChange = canChangeProperty(theOldValue, aValue);
		if (theCanChange == REJECT) return;
		if (theCanChange != ACCEPT) aValue = (T) theCanChange;

		if (itsValue != null) ObservationCenter.getInstance().unregisterListener(itsValue, this);
		itsValue = aValue;
		if (itsValue != null) ObservationCenter.getInstance().registerListener(itsValue, this);
		
		firePropertyChanged(theOldValue, aValue);
	}
	
	public void observe(Object aObservable, Object aData)
	{
		if (aObservable == itsValue) firePropertyValueChanged();
	}


	public IProperty<T> cloneForOwner(Object aOwner, boolean aCloneValue)
	{
		SimpleProperty<T> theClone = 
			(SimpleProperty) super.cloneForOwner(aOwner, aCloneValue);
		
		if (aCloneValue) 
		{
			IPublicCloneable theValue = (IPublicCloneable) itsValue;
			theClone.itsValue = theValue != null ? (T) theValue.clone() : null;
		}
		
		return theClone;
	}
}
