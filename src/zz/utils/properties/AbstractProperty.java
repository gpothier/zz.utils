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
	
	private List<IRef<IPropertyVeto<?>>> itsVetos; 
	
	private List<IRef<IPropertyListener<?>>> itsListeners; 
	
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
		if (itsListeners == null) return;
		List<IPropertyListener<?>> theListeners = RefUtils.dereference(itsListeners); 

		for (IPropertyListener theListener : theListeners)
			theListener.propertyChanged(this, aOldValue, aNewValue);
	}
	
	protected void firePropertyValueChanged ()
	{
		if (itsListeners == null) return;
		List<IPropertyListener<?>> theListeners = RefUtils.dereference(itsListeners); 

		for (IPropertyListener theListener : theListeners)
			theListener.propertyValueChanged(this);
	}
	
	protected boolean canChangeProperty (Object aValue)
	{
		if (itsVetos == null) return true;
		List<IPropertyVeto<?>> theVetos = RefUtils.dereference(itsVetos);
		
		for (IPropertyVeto theVeto : theVetos)
			if (! theVeto.canChangeProperty(this, aValue)) return false;
		
		return true;
	}
	
	public void addListener (IPropertyListener<?> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList(3);
		itsListeners.add (new WeakRef<IPropertyListener<?>>(aListener));
	}

	public void addHardListener (IPropertyListener<?> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList(3);
		itsListeners.add (new HardRef<IPropertyListener<?>>(aListener));
	}
	
	public void removeListener (IPropertyListener<?> aListener)
	{
		RefUtils.remove(itsListeners, aListener);
		if (itsListeners.size() == 0) itsListeners = null;
	}

	public void addVeto (IPropertyVeto<?> aVeto)
	{
		if (itsVetos == null) itsVetos = new ArrayList(3);
		itsVetos.add (new WeakRef<IPropertyVeto<?>>(aVeto));
	}

	public void addHardVeto (IPropertyVeto<?> aVeto)
	{
		if (itsVetos == null) itsVetos = new ArrayList(3);
		itsVetos.add (new HardRef<IPropertyVeto<?>>(aVeto));
	}
	
	public void removeVeto (IPropertyVeto<?> aVeto)
	{
		RefUtils.remove(itsVetos, aVeto);
		if (itsVetos.size() == 0) itsVetos = null;
	}
	
	public IProperty<T> cloneForContainer(Object aContainer)
	{
		AbstractProperty<T> theClone = (AbstractProperty) super.clone();
		
		theClone.itsContainer = aContainer;
		theClone.itsListeners = new FailsafeLinkedList();
		theClone.itsVetos = new FailsafeLinkedList();
		
		return theClone;
	}

	
}
