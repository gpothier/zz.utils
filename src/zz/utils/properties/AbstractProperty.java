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
	
	private List<IRef<IPropertyVeto<?>>> itsVetos = 
		new ArrayList();
	
	private List<IRef<IPropertyListener<?>>> itsListeners = 
		new ArrayList();
	
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
	
	
	protected void firePropertyChanged ()
	{
		List<IPropertyListener<?>> theListeners = dereference(itsListeners); 

		for (IPropertyListener theListener : theListeners)
			theListener.propertyChanged(this);
	}
	
	protected boolean canChangeProperty (Object aValue)
	{
		List<IPropertyVeto<?>> theVetos = dereference(itsVetos);
		
		for (IPropertyVeto theVeto : theVetos)
			if (! theVeto.canChangeProperty(this, aValue)) return false;
		
		return true;
	}
	
	public void addListener (IPropertyListener<?> aListener)
	{
		itsListeners.add (new WeakRef<IPropertyListener<?>>(aListener));
	}

	public void addHardListener (IPropertyListener<?> aListener)
	{
		itsListeners.add (new HardRef<IPropertyListener<?>>(aListener));
	}
	
	public void removeListener (IPropertyListener aListener)
	{
		for (Iterator theIterator = itsListeners.iterator();theIterator.hasNext();)
		{
			IRef<IPropertyListener> theRef = (IRef<IPropertyListener>) theIterator.next();
			IPropertyListener theListener = theRef.get();
			if (theListener == null || theListener == aListener) theIterator.remove();
		}
	}

	public void addVeto (IPropertyVeto aVeto)
	{
		itsVetos.add (new WeakRef<IPropertyVeto<?>>(aVeto));
	}

	public void addHardVeto (IPropertyVeto aVeto)
	{
		itsVetos.add (new HardRef<IPropertyVeto<?>>(aVeto));
	}
	
	public void removeVeto (IPropertyVeto aVeto)
	{
		for (Iterator theIterator = itsVetos.iterator();theIterator.hasNext();)
		{
			IRef<IPropertyVeto> theRef = (IRef<IPropertyVeto>) theIterator.next();
			IPropertyVeto theVeto = theRef.get();
			if (theVeto == null || theVeto == aVeto) theIterator.remove();
		}
	}
	
	public IProperty<T> cloneForContainer(Object aContainer)
	{
		AbstractProperty<T> theClone = (AbstractProperty) super.clone();
		
		theClone.itsContainer = aContainer;
		theClone.itsListeners = new FailsafeLinkedList();
		theClone.itsVetos = new FailsafeLinkedList();
		
		return theClone;
	}

	/**
	 * Returns a list containing dereferenced elements from the given
	 * list. This method also remove GCed elements from the references list
	 */
	protected static <E> List<E> dereference (List<IRef<E>> aList)
	{
		if (aList.isEmpty()) return Collections.EMPTY_LIST;
		
		List<E> theResult = new ArrayList<E>(aList.size());
		
		for (Iterator<IRef<E>> theIterator = aList.iterator();theIterator.hasNext();)
		{
			IRef<E> theRef = theIterator.next();
			E theElement = theRef.get();
			if (theElement == null) theIterator.remove();
			else theResult.add (theElement);
		}
		
		return theResult;
	}
	
	/**
	 * Reference to a property listener. Implementations can be weak or hard references.
	 */
	protected interface IRef<L>
	{
		public L get();
	}
	
	protected static class HardRef<L> implements IRef<L>
	{
		private L itsValue;
		
		public HardRef(L aValue)
		{
			itsValue = aValue;
		}
		
		public L get()
		{
			return itsValue;
		}
	}
	
	protected static class WeakRef<L> extends WeakReference<L> implements IRef<L>
	{

		public WeakRef(L aValue)
		{
			super(aValue);
		}
		
	}
}
