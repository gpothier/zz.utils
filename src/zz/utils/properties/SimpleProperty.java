/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

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
	
	public SimpleProperty(Object aContainer)
	{
		super (aContainer);
	}
	
	public SimpleProperty(Object aContainer, T aValue)
	{
		super (aContainer);
		itsValue = aValue;
	}
	
	public SimpleProperty(Object aContainer, PropertyId<T> aPropertyId)
	{
		super (aContainer, aPropertyId);
	}
	
	public SimpleProperty(Object aContainer, PropertyId<T> aPropertyId, T aValue)
	{
		super (aContainer, aPropertyId);
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
	 * Internal setter for the property.
	 * It first checks if a veto rejects the new value. If not, it
	 * sets the current value and fires notifications.
	 * @param aValue The new value of the property.
	 */
	protected final void set0(T aValue)
	{
		T theOldValue = get0();
		if (theOldValue != aValue && canChangeProperty(aValue))
		{
			if (itsValue != null) ObservationCenter.getInstance().unregisterListener(itsValue, this);
			itsValue = aValue;
			if (itsValue != null) ObservationCenter.getInstance().registerListener(itsValue, this);
			
			changed(theOldValue, aValue);
			firePropertyChanged(theOldValue, aValue);
			ObservationCenter.getInstance().requestObservation(getContainer(), this);
		}
	}
	
	public void observe(Object aObservable, Object aData)
	{
		if (aObservable == itsValue) firePropertyValueChanged();
	}

	/**
	 * This method is called whenever the value of this property changes.
	 */
	protected void changed (T aOldValue, T aNewValue)
	{
	}

	
}
