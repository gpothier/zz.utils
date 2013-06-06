package zz.utils.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import zz.utils.ReverseIteratorWrapper;
import zz.utils.list.ICollectionListener;
import zz.utils.list.IListListener;
import zz.utils.references.HardRef;
import zz.utils.references.IRef;
import zz.utils.references.RefUtils;
import zz.utils.references.WeakRef;

public abstract class ComputedListProperty<E> extends ComputedProperty<List<E>>
implements IListProperty<E>
{
	/**
	 * We store all the listeners here, be they collection or
	 * list listeners
	 */
	private List<IRef<Object>> itsListListeners;

	public void addListener(IListListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new WeakRef<Object>(aListener));
	}

	public void addHardListener(IListListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new HardRef<Object>(aListener));
	}
	
	public void removeListener(IListListener<E> aListener)
	{
		if (itsListListeners != null) 
		{
			RefUtils.remove(itsListListeners, aListener);
			if (itsListListeners.size() == 0) itsListListeners = null;
		}
	}

	public void addListener(ICollectionListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new WeakRef<Object>(aListener));
	}

	public void addHardListener (ICollectionListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new HardRef<Object>(aListener));
	}
	
	public void removeListener (ICollectionListener<E> aListener)
	{
		if (itsListListeners != null) 
		{
			RefUtils.remove(itsListListeners, aListener);
			if (itsListListeners.size() == 0) itsListListeners = null;
		}
	}


	@Override
	public int size()
	{
		return get().size();
	}

	@Override
	public boolean isEmpty()
	{
		return get().isEmpty();
	}

	@Override
	public boolean contains(Object aO)
	{
		return get().contains(aO);
	}

	@Override
	public Iterator<E> iterator()
	{
		return get().iterator();
	}

	@Override
	public Object[] toArray()
	{
		return get().toArray();
	}

	@Override
	public <E> E[] toArray(E[] aA)
	{
		return get().toArray(aA);
	}

	@Override
	public boolean add(E aE)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object aO)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection< ? > aC)
	{
		return get().containsAll(aC);
	}

	@Override
	public boolean addAll(Collection< ? extends E> aC)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection< ? > aC)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection< ? > aC)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public E get(int aIndex)
	{
		return get().get(aIndex);
	}

	@Override
	public void add(int aIndex, E aElement)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int aIndex, E aElement)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Iterable< ? extends E> aCollection)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object aElement)
	{
		return get().indexOf(aElement);
	}

	@Override
	public E remove(int aIndex)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> reverseIterator()
	{
		return new ReverseIteratorWrapper<E>(get());
	}

	@Override
	public boolean addAll(int aIndex, Collection< ? extends E> aC)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object aO)
	{
		return get().lastIndexOf(aO);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return get().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int aIndex)
	{
		return get().listIterator(aIndex);
	}

	@Override
	public List<E> subList(int aFromIndex, int aToIndex)
	{
		return get().subList(aFromIndex, aToIndex);
	}

	@Override
	protected void fireChanges(List<E> aOldValue, List<E> aNewValue)
	{
		super.fireChanges(aOldValue, aNewValue);
	}

}
