/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import zz.utils.ReverseIteratorWrapper;

/**
 * @author gpothier
 */
public class ArrayListProperty<E> extends AbstractProperty<List<E>> 
implements IListProperty<E>
{
	private List<IRef<IListPropertyListener<E>>> itsListListeners = 
		new ArrayList<IRef<IListPropertyListener<E>>>();
	
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
	
	public void addListener (IListPropertyListener<E> aListener)
	{
		itsListListeners.add (new WeakRef<IListPropertyListener<E>>(aListener));
	}

	public void addHardListener (IListPropertyListener<E> aListener)
	{
		itsListListeners.add (new HardRef<IListPropertyListener<E>>(aListener));
	}
	
	public void removeListener (IListPropertyListener<E> aListener)
	{
		for (Iterator theIterator = itsListListeners.iterator();theIterator.hasNext();)
		{
			IRef<IListPropertyListener> theRef = (IRef<IListPropertyListener>) theIterator.next();
			IListPropertyListener theListener = theRef.get();
			if (theListener == null || theListener == aListener) theIterator.remove();
		}
	}


	
	public final void add(E aElement)
	{
		itsList.add (aElement);
		fireElementAdded(size()-1, aElement);
	}

	public final void add(int aIndex, E aElement)
	{
		itsList.add (aIndex, aElement);
		fireElementAdded(aIndex, aElement);
	}

	public void set(int aIndex, E aElement)
	{
		E theElement = itsList.set(aIndex, aElement);
		fireElementRemoved(aIndex, theElement);
		fireElementAdded(aIndex, aElement);
	}
	
	public final boolean remove(E aElement)
	{
		boolean theResult = itsList.remove (aElement);
		return theResult;
	}

	public final E remove(int aIndex)
	{
		E theResult = itsList.remove(aIndex);
		return theResult;
	}

	public void clear()
	{
		for (int i = size()-1;i>=0;i--)
			remove(i);
	}
	
	public final int size()
	{
		return itsList.size();
	}

	public final E get(int aIndex)
	{
		return itsList.get (aIndex);
	}

	public int indexOf(E aElement)
	{
		return itsList.indexOf(aElement);
	}

	public final Iterator<E> iterator()
	{
		return itsList.iterator();
	}

	public Iterator<E> reverseIterator()
	{
		return new ReverseIteratorWrapper (itsList);
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
		
		List<IListPropertyListener<E>> theListeners = dereference(itsListListeners);
		
		for (IListPropertyListener<E> theListener : theListeners)
			theListener.elementAdded(this, aIndex, aElement);
	}
	
	protected void fireElementRemoved (int aIndex, E aElement)
	{
		elementRemoved(aElement);

		List<IListPropertyListener<E>> theListeners = dereference(itsListListeners);
		
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
