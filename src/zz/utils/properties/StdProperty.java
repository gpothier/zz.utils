/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import zz.utils.notification.ObservationCenter;
import zz.utils.notification.Observer;

/**
 * Default implementation of {@link zz.utils.properties.Property}
 * @author gpothier
 */
public class StdProperty<T> extends AbstractProperty<T> implements Property<T>, Observer
{
	
	/**
	 * The actual value of the property
	 */
	private T itsValue;
	
	public StdProperty(Object aContainer)
	{
		super (aContainer);
	}
	
	public StdProperty(Object aContainer, T aValue)
	{
		super (aContainer);
		itsValue = aValue;
	}
	
	public StdProperty(Object aContainer, PropertyId<T> aPropertyId)
	{
		super (aContainer, aPropertyId);
	}
	
	public StdProperty(Object aContainer, PropertyId<T> aPropertyId, T aValue)
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
		if (get0() != aValue && canChangeProperty(aValue))
		{
			if (itsValue != null) ObservationCenter.getInstance().unregisterListener(itsValue, this);
			itsValue = aValue;
			if (itsValue != null) ObservationCenter.getInstance().registerListener(itsValue, this);
			firePropertyChanged();
			
			ObservationCenter.getInstance().requestObservation(getContainer(), this);
		}
	}
	
	public void observe(Object aObservable, Object aData)
	{
		if (aObservable == itsValue) firePropertyChanged();
	}
	
}
