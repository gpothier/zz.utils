/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import java.util.ArrayList;
import java.util.List;

import zz.utils.PublicCloneable;
import zz.utils.references.HardRef;
import zz.utils.references.IRef;
import zz.utils.references.RefUtils;
import zz.utils.references.WeakRef;

/**
 * Can be used as a base to implement properties.
 * Takes care of listeners and owner, only leaves
 * the handling of the value to subclasses.
 * @author gpothier
 */
public abstract class AbstractProperty<T> extends PublicCloneable implements IProperty<T>
{
	protected static Object ACCEPT = new Object();
	protected static Object REJECT = new Object();
	
	/**
	 * The object that contains the property.
	 * If present, this object will be the source of an
	 * observation through the {@link ObservationCenter}
	 */
	private Object itsOwner;
	
	/**
	 * The id of this property.
	 */
	private PropertyId<T> itsPropertyId;
	
	/**
	 * We maintain the number of veto listeners for optimization purposes.
	 * This is an approximation, as weakly referenced veto listsners can be garbage collected.
	 */
	private int itsVetoCount = 0;
	
	private List<IRef<IPropertyListener<? super T>>> itsListeners; 
	
	public AbstractProperty()
	{
	}
	
	public AbstractProperty(Object aOwner)
	{
		itsOwner = aOwner;
	}
	
	public AbstractProperty(Object aOwner, PropertyId<T> aPropertyId)
	{
		itsOwner = aOwner;
		itsPropertyId = aPropertyId;
	}
	
	public Object getOwner()
	{
		return itsOwner;
	}
	
	/**
	 * Returns the id of this property, if it exists.
	 */
	public PropertyId<T> getId()
	{
		return itsPropertyId;
	}
	
	/**
	 * This method is called whenever the value of this property changes.
	 * It does nothing by default, but subclasses can override it.
	 */
	protected void changed (T aOldValue, T aNewValue)
	{
	}

	/**
	 * This method is called whenever the value of the property internally
	 * changes.
	 */
	protected void valueChanged()
	{
	}

	
	protected void firePropertyChanged (T aOldValue, T aNewValue)
	{
		changed(aOldValue, aNewValue);
		if (itsListeners != null)
		{
			List<IPropertyListener<? super T>> theListeners = 
				RefUtils.dereference(itsListeners); 
	
			for (IPropertyListener<? super T> theListener : theListeners)
				theListener.propertyChanged((IProperty) this, aOldValue, aNewValue);
		}
		
//		ObservationCenter.getInstance().requestObservation(getOwner(), this);
	}
	
	protected void firePropertyValueChanged ()
	{
		valueChanged();
		if (itsListeners != null)
		{
			List<IPropertyListener<? super T>> theListeners = RefUtils.dereference(itsListeners); 
	
			for (IPropertyListener<? super T> theListener : theListeners)
				theListener.propertyValueChanged((IProperty) this);
		}
		
//		ObservationCenter.getInstance().requestObservation(getOwner(), this);
	}
	
	/**
	 * This method is called before the property is changed and gives
	 * the property the opportunity to refuse or adapt a change request.
	 * @return {@link #ACCEPT} if the new value should be accepted (if 
	 * also accepted by veto listeners),
	 * {@link #REJECT} if the new value should be rejected. 
	 * Another value will set the property to that value instead of the 
	 * requested value, bypassing the veto listeners.
	 */
	protected Object canChange (T aOldValue, T aNewValue)
	{
		return ACCEPT;
	}
	
	protected final Object canChangeProperty (T aOldValue, T aNewValue)
	{
		Object theCanChange = canChange(aOldValue, aNewValue);
		if (theCanChange == REJECT) return REJECT;
		if (theCanChange != ACCEPT) return theCanChange;
		
		if (itsVetoCount <= 0) return ACCEPT;
		List<IPropertyListener<? super T>> theListeners = RefUtils.dereference(itsListeners);

		for (IPropertyListener<? super T> theListener : theListeners)
		{
			if (theListener instanceof IPropertyVeto)
			{
				IPropertyVeto<? super T> theVeto = (IPropertyVeto<? super T>) theListener;
				if (! theVeto.canChangeProperty((IProperty) this, aOldValue, aNewValue)) return REJECT;
			}
		}
		
		return ACCEPT;
	}
	
	public void addListener (IPropertyListener<? super T> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList(3);
		if (aListener instanceof IPropertyVeto) itsVetoCount++;
		itsListeners.add (new WeakRef<IPropertyListener<? super T>>(aListener));
	}

	public void addHardListener (IPropertyListener<? super T> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList(3);
		if (aListener instanceof IPropertyVeto) itsVetoCount++;
		itsListeners.add (new HardRef<IPropertyListener<? super T>>(aListener));
	}
	
	public void addListener(IPropertyListener< ? super T> aListener, boolean aHard)
	{
		if (aHard) addHardListener(aListener);
		else addListener(aListener);
	}

	public void removeListener (IPropertyListener<? super T> aListener)
	{
		if (itsListeners != null) 
		{
			if (RefUtils.remove(itsListeners, aListener))
			{
				if (itsListeners.size() == 0) itsListeners = null;
				if (aListener instanceof IPropertyVeto) itsVetoCount--;				
			}
		}
	}

	public IProperty<T> cloneForOwner(Object aOwner, boolean aCloneValue)
	{
		AbstractProperty<T> theClone = (AbstractProperty<T>) super.clone();
		
		theClone.itsOwner = aOwner;
		theClone.itsListeners = null;
		theClone.itsVetoCount = 0;
		
		return theClone;
	}

	public String toString()
	{
		return String.format (
				"Property (id: %s, value: %s, owner: %s)",
				itsPropertyId,
				get(),
				getOwner());
	}
	
}
