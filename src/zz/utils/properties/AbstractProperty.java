/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import zz.utils.FailsafeLinkedList;
import zz.utils.PublicCloneable;
import zz.utils.notification.ObservationCenter;
import zz.utils.notification.Observer;
import zz.utils.references.HardRef;
import zz.utils.references.IRef;
import zz.utils.references.RefUtils;
import zz.utils.references.WeakRef;

/**
 * Can be used as a base to implement properties.
 * Takes care of listeners and container, only leaves
 * the handling of the value to subclasses
 * @author gpothier
 */
public abstract class AbstractProperty<T> extends PublicCloneable implements IProperty<T>
{
	/**
	 * The object that contains the property.
	 * If present, this object will be the source of an
	 * observation through the {@link ObservationCenter}
	 */
	private Object itsContainer;
	
	/**
	 * The id of this property.
	 */
	private PropertyId<T> itsPropertyId;
	
	private List<IRef<IPropertyVeto<? super T>>> itsVetos; 
	
	private List<IRef<IPropertyListener<? super T>>> itsListeners; 
	
	public AbstractProperty()
	{
	}
	
	public AbstractProperty(Object aContainer)
	{
		itsContainer = aContainer;
	}
	
	public AbstractProperty(Object aContainer, PropertyId<T> aPropertyId)
	{
		itsContainer = aContainer;
		itsPropertyId = aPropertyId;
	}
	
	public Object getContainer()
	{
		return itsContainer;
	}
	
	/**
	 * Returns the id of this property, if it exists.
	 */
	public PropertyId<T> getId()
	{
		return itsPropertyId;
	}
	
	protected void firePropertyChanged (T aOldValue, T aNewValue)
	{
		if (itsListeners != null)
		{
			List<IPropertyListener<? super T>> theListeners = 
				RefUtils.dereference(itsListeners); 
	
			for (IPropertyListener<? super T> theListener : theListeners)
				theListener.propertyChanged((IProperty) this, aOldValue, aNewValue);
		}
		
		ObservationCenter.getInstance().requestObservation(getContainer(), this);
	}
	
	protected void firePropertyValueChanged ()
	{
		if (itsListeners != null)
		{
			List<IPropertyListener<? super T>> theListeners = RefUtils.dereference(itsListeners); 
	
			for (IPropertyListener<? super T> theListener : theListeners)
				theListener.propertyValueChanged((IProperty) this);
		}
		
		ObservationCenter.getInstance().requestObservation(getContainer(), this);
	}
	
	protected boolean canChangeProperty (T aValue)
	{
		if (itsVetos == null) return true;
		List<IPropertyVeto<? super T>> theVetos = RefUtils.dereference(itsVetos);
		
		for (IPropertyVeto<? super T> theVeto : theVetos)
			if (! theVeto.canChangeProperty((IProperty) this, aValue)) return false;
		
		return true;
	}
	
	public void addListener (IPropertyListener<? super T> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList(3);
		itsListeners.add (new WeakRef<IPropertyListener<? super T>>(aListener));
	}

	public void addHardListener (IPropertyListener<? super T> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList(3);
		itsListeners.add (new HardRef<IPropertyListener<? super T>>(aListener));
	}
	
	public void removeListener (IPropertyListener<? super T> aListener)
	{
		if (itsListeners != null) 
		{
			RefUtils.remove(itsListeners, aListener);
			if (itsListeners.size() == 0) itsListeners = null;
		}
	}

	public void addVeto (IPropertyVeto<? super T> aVeto)
	{
		if (itsVetos == null) itsVetos = new ArrayList(3);
		itsVetos.add (new WeakRef<IPropertyVeto<? super T>>(aVeto));
	}

	public void addHardVeto (IPropertyVeto<? super T> aVeto)
	{
		if (itsVetos == null) itsVetos = new ArrayList(3);
		itsVetos.add (new HardRef<IPropertyVeto<? super T>>(aVeto));
	}
	
	public void removeVeto (IPropertyVeto<? super T> aVeto)
	{
		if (itsVetos != null) 
		{
			RefUtils.remove(itsVetos, aVeto);
			if (itsVetos.size() == 0) itsVetos = null;
		}
	}
	
	public IProperty<T> cloneForContainer(Object aContainer)
	{
		AbstractProperty<T> theClone = (AbstractProperty<T>) super.clone();
		
		theClone.itsContainer = aContainer;
		theClone.itsListeners = null;
		theClone.itsVetos = null;
		
		return theClone;
	}

	
}
