/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import zz.utils.ReverseIteratorWrapper;
import zz.utils.references.HardRef;
import zz.utils.references.IRef;
import zz.utils.references.RefUtils;
import zz.utils.references.WeakRef;

/**
 * @author gpothier
 */
public class ArrayListProperty<E> extends AbstractProperty<List<E>> 
implements IListProperty<E>
{
	private List<IRef<IListPropertyListener<E>>> itsListListeners;
	
	private List<E> itsList = new MyList ();
	
	public ArrayListProperty(Object aContainer)
	{
		super(aContainer);
	}
	
	public ArrayListProperty(Object aContainer, PropertyId<List<E>> aPropertyId)
	{
		super(aContainer, aPropertyId);
	}
	
	public List<E> get()
	{
		return itsList;
	}
	
	/**
	 * Changes the list that backs the property.
	 * This should be used with care, as it will not send any notification.
	 */
	protected void set (List<E> aList)
	{
		itsList = aList;
	}
	
	public void addListener (IListPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList<IRef<IListPropertyListener<E>>>(3);
		itsListListeners.add (new WeakRef<IListPropertyListener<E>>(aListener));
	}

	public void addHardListener (IListPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList<IRef<IListPropertyListener<E>>>(3);
		itsListListeners.add (new HardRef<IListPropertyListener<E>>(aListener));
	}
	
	public void removeListener (IListPropertyListener<E> aListener)
	{
		RefUtils.remove(itsListListeners, aListener);
		if (itsListListeners.size() == 0) itsListListeners = null;
	}


	
	public final void add(E aElement)
	{
		get().add (aElement);
		fireElementAdded(size()-1, aElement);
	}

	public final void add(int aIndex, E aElement)
	{
		get().add (aIndex, aElement);
		fireElementAdded(aIndex, aElement);
	}

	public void set(int aIndex, E aElement)
	{
		E theElement = get().set(aIndex, aElement);
		fireElementRemoved(aIndex, theElement);
		fireElementAdded(aIndex, aElement);
	}
	
	public final boolean remove(E aElement)
	{
		boolean theResult = get().remove (aElement);
		return theResult;
	}

	public final E remove(int aIndex)
	{
		E theResult = get().remove(aIndex);
		return theResult;
	}

	public void clear()
	{
		for (int i = size()-1;i>=0;i--)
			remove(i);
	}
	
	public final int size()
	{
		return get().size();
	}

	public final E get(int aIndex)
	{
		return get().get (aIndex);
	}

	public int indexOf(E aElement)
	{
		return get().indexOf(aElement);
	}

	public final Iterator<E> iterator()
	{
		return get().iterator();
	}

	public Iterator<E> reverseIterator()
	{
		return new ReverseIteratorWrapper (get());
	}
	
	/**
	 * This method is called whenever an element is added to this list.
	 * By default it does nothing, but subclasses can override it to be notified.
	 */
	protected void elementAdded (int aIndex, E aElement)
	{
	}
	
	/**
	 * This method is called whenever an element is removed from this list.
	 * By default it does nothing, but subclasses can override it to be notified.
	 */
	protected void elementRemoved (E aElement)
	{
	}
	

	
	protected void fireElementAdded (int aIndex, E aElement)
	{
		elementAdded(aIndex, aElement);
	
		if (itsListListeners == null) return;
		List<IListPropertyListener<E>> theListeners = RefUtils.dereference(itsListListeners);
		
		for (IListPropertyListener<E> theListener : theListeners)
			theListener.elementAdded(this, aIndex, aElement);
	}
	
	protected void fireElementRemoved (int aIndex, E aElement)
	{
		elementRemoved(aElement);

		if (itsListListeners == null) return;
		List<IListPropertyListener<E>> theListeners = RefUtils.dereference(itsListListeners);
		
		for (IListPropertyListener<E> theListener : theListeners)
			theListener.elementRemoved(this, aIndex, aElement);
	}
	
	/**
	 * This is our implementation of List, which override some methods in
	 * order to send notifications. 
	 * @author gpothier
	 */
	private class MyList extends ArrayList<E>
	{
		public boolean remove(Object aO)
		{
			int theIndex = indexOf(aO);
			if (theIndex >= 0) 
			{
				remove(theIndex);
				return true;
			}
			else return false;
		}
		
		public E remove(int aIndex)
		{
			E theElement = super.remove(aIndex);
			fireElementRemoved(aIndex, theElement);
			return theElement;
		}
		
		public boolean add(E aElement)
		{
			boolean theResult = super.add(aElement);
			fireElementAdded(size()-1, aElement);
			return theResult;
		}
		
		public void add(int aIndex, E aElement)
		{
			super.add(aIndex, aElement);
			fireElementAdded(aIndex, aElement);
		}
		
		public boolean addAll(Collection<? extends E> aCollection)
		{
			return addAll(0, aCollection);
		}
		
		public boolean addAll(int aIndex, Collection<? extends E> aCollection)
		{
			int theIndex = aIndex;
			for (E theElement : aCollection)
				add (theIndex++, theElement);
			
			return aCollection.size() > 0;
		}
	}

}
