/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
public abstract class AbstractProperty<T> extends PublicCloneable implements Property<T>
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
		new FailsafeLinkedList();
	
	private List<IRef<IPropertyListener<?>>> itsListeners = 
		new FailsafeLinkedList();
	
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
		for (Iterator theIterator = itsListeners.iterator();theIterator.hasNext();)
		{
			IRef<IPropertyListener> theRef = (IRef<IPropertyListener>) theIterator.next();
			IPropertyListener<?> theListener = theRef.get();
			if (theListener == null) theIterator.remove();
			else theListener.propertyChanged(this);
		}
	}
	
	protected boolean canChangeProperty (Object aValue)
	{
		for (Iterator theIterator = itsVetos.iterator();theIterator.hasNext();)
		{
			IRef<IPropertyVeto> theRef = (IRef<IPropertyVeto>) theIterator.next();
			IPropertyVeto<?> theVeto = theRef.get();
			if (theVeto == null) theIterator.remove();
			else if (! theVeto.canChangeProperty(this, aValue)) return false;
		}
		return true;
	}
	
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
	public void addListener (IPropertyListener<?> aListener)
	{
		itsListeners.add (new WeakRef<IPropertyListener<?>>(aListener));
	}

	/**
	 * Adds a listener that will be notified each time this
	 * property changes.
	 * The listener will be referenced through a strong reference.
	 * @see #addListener(IPropertyListener)
	 */
	public void addHardListener (IPropertyListener<?> aListener)
	{
		itsListeners.add (new HardRef<IPropertyListener<?>>(aListener));
	}
	
	/**
	 * Removes a previously added listener.
	 */
	public void removeListener (IPropertyListener aListener)
	{
		for (Iterator theIterator = itsListeners.iterator();theIterator.hasNext();)
		{
			IRef<IPropertyListener> theRef = (IRef<IPropertyListener>) theIterator.next();
			IPropertyListener theListener = theRef.get();
			if (theListener == null || theListener == aListener) theIterator.remove();
		}
	}

	/**
	 * Adds a veto that can reject a new value for this property.
	 * See the comment on {@link #addListener(IPropertyListener)}
	 * about the referencing scheme.
	 */
	public void addVeto (IPropertyVeto aVeto)
	{
		itsVetos.add (new WeakRef<IPropertyVeto<?>>(aVeto));
	}

	/**
	 * Adds a veto that can reject a new value for this property.
	 * See the comment on {@link #addListener(IPropertyListener)}
	 * about the referencing scheme.
	 */
	public void addHardVeto (IPropertyVeto aVeto)
	{
		itsVetos.add (new HardRef<IPropertyVeto<?>>(aVeto));
	}
	
	/**
	 * Removes a previously added veto.
	 */
	public void removeVeto (IPropertyVeto aVeto)
	{
		for (Iterator theIterator = itsVetos.iterator();theIterator.hasNext();)
		{
			IRef<IPropertyVeto> theRef = (IRef<IPropertyVeto>) theIterator.next();
			IPropertyVeto theVeto = theRef.get();
			if (theVeto == null || theVeto == aVeto) theIterator.remove();
		}
	}
	
	public Property<T> cloneForContainer(Object aContainer)
	{
		AbstractProperty<T> theClone = (AbstractProperty) super.clone();
		
		theClone.itsContainer = aContainer;
		theClone.itsListeners = new FailsafeLinkedList();
		theClone.itsVetos = new FailsafeLinkedList();
		
		return theClone;
	}

	
	/**
	 * Reference to a property listener. Implementations can be weak or hard references.
	 */
	private interface IRef<L>
	{
		public L get();
	}
	
	private static class HardRef<L> implements IRef<L>
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
	
	private static class WeakRef<L> extends WeakReference<L> implements IRef<L>
	{

		public WeakRef(L aValue)
		{
			super(aValue);
		}
		
	}
}
