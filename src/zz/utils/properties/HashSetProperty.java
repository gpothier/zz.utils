/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import zz.utils.ReverseIteratorWrapper;
import zz.utils.references.HardRef;
import zz.utils.references.IRef;
import zz.utils.references.RefUtils;
import zz.utils.references.WeakRef;

/**
 * @author gpothier
 */
public class HashSetProperty<E> extends AbstractProperty<Set<E>> 
implements ISetProperty<E>
{
	/**
	 * We store all the listeners here, be they collection or
	 * list listeners
	 */
	private List<IRef<ICollectionPropertyListener<E>>> itsListListeners;
	
	private Set<E> itsSet = new MySet ();
	
	public HashSetProperty(Object aContainer)
	{
		super(aContainer);
	}
	
	public HashSetProperty(Object aContainer, PropertyId<Set<E>> aPropertyId)
	{
		super(aContainer, aPropertyId);
	}
	
	public Set<E> get()
	{
		return itsSet;
	}
	
	/**
	 * Changes the set that backs the property.
	 * This should be used with care, as it will not send any notification.
	 */
	protected void set (Set<E> aSet)
	{
		if (aSet instanceof MySet) itsSet = ((MySet) aSet);
		else itsSet = aSet != null ? new MySet (aSet) : null;
	}
	
	public void addListener (ICollectionPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new WeakRef<ICollectionPropertyListener<E>>(aListener));
	}

	public void addHardListener (ICollectionPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new HardRef<ICollectionPropertyListener<E>>(aListener));
	}
	
	public void removeListener (ICollectionPropertyListener<E> aListener)
	{
		if (itsListListeners != null) 
		{
			RefUtils.remove(itsListListeners, aListener);
			if (itsListListeners.size() == 0) itsListListeners = null;
		}
	}

	public final void add(E aElement)
	{
		get().add (aElement);
	}

	public final boolean remove(E aElement)
	{
		return get().remove (aElement);
	}
	
	public boolean contains(Object aObject)
	{
		return get().contains(aObject);
	}

	public void clear()
	{
		for (Iterator<E> theIterator = iterator();theIterator.hasNext();)
		{
			theIterator.remove();
		}
	}
	
	public final int size()
	{
		Set<E> theSet = get();
		return theSet != null ? theSet.size() : 0;
	}

	public final Iterator<E> iterator()
	{
		return get().iterator();
	}
	
	/**
	 * This method is called whenever an element is added to this set.
	 * By default it does nothing, but subclasses can override it to be notified.
	 */
	protected void elementAdded (E aElement)
	{
	}
	
	/**
	 * This method is called whenever an element is removed from this set.
	 * By default it does nothing, but subclasses can override it to be notified.
	 */
	protected void elementRemoved (E aElement)
	{
	}
	

	
	protected void fireElementAdded (E aElement)
	{
		elementAdded(aElement);
	
		if (itsListListeners == null) return;
		List<ICollectionPropertyListener<E>> theListeners = 
			RefUtils.dereference(itsListListeners);
		
		for (ICollectionPropertyListener<E> theListener : theListeners)
		{
			theListener.elementAdded(this, aElement);
		}
	}
	
	protected void fireElementRemoved (E aElement)
	{
		elementRemoved(aElement);

		if (itsListListeners == null) return;
		List<ICollectionPropertyListener<E>> theListeners = 
			RefUtils.dereference(itsListListeners);
		
		for (ICollectionPropertyListener<E> theListener : theListeners)
		{
			theListener.elementRemoved(this, aElement);
		}
	}
	
	/**
	 * This is our implementation of List, which override some methods in
	 * order to send notifications. 
	 * @author gpothier
	 */
	private class MySet extends HashSet<E>
	{
		public MySet()
		{
		}
		
		public MySet(Collection<? extends E> aContent)
		{
			addAll(aContent);
		}
		
		public boolean remove(Object aO)
		{
			if (super.remove(aO))
			{
				fireElementRemoved((E) aO);
				return true;
			}
			else return false;
		}
		
		public boolean add(E aElement)
		{
			if (super.add(aElement))
			{
				fireElementAdded(aElement);
				return true;
			}
			else return false;
		}
		
		public boolean addAll(Collection<? extends E> aCollection)
		{
			boolean theResult = false;
			for (E theElement : aCollection)
				theResult |= add (theElement);
			
			return theResult;
		}
		
	}

}
