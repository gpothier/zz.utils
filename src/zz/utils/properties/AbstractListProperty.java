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
 * Can be used as a base for implementing {@link zz.utils.properties.IListProperty}.
 * Manages listeners.
 * @author gpothier
 */
public abstract class AbstractListProperty<E> extends AbstractProperty<List<E>> 
implements IListProperty<E>
{
	/**
	 * We store all the listeners here, be they collection or
	 * list listeners
	 */
	private List<IRef<Object>> itsListListeners;
	
	public AbstractListProperty(Object aContainer)
	{
		super(aContainer);
	}
	
	public AbstractListProperty(Object aContainer, PropertyId<List<E>> aPropertyId)
	{
		super(aContainer, aPropertyId);
	}
	
	public void addListener (IListPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new WeakRef<Object>(aListener));
	}

	public void addHardListener (IListPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new HardRef<Object>(aListener));
	}
	
	public void removeListener (IListPropertyListener<E> aListener)
	{
		if (itsListListeners != null) 
		{
			RefUtils.remove(itsListListeners, aListener);
			if (itsListListeners.size() == 0) itsListListeners = null;
		}
	}

	public void addListener (ICollectionPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new WeakRef<Object>(aListener));
	}

	public void addHardListener (ICollectionPropertyListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new HardRef<Object>(aListener));
	}
	
	public void removeListener (ICollectionPropertyListener<E> aListener)
	{
		if (itsListListeners != null) 
		{
			RefUtils.remove(itsListListeners, aListener);
			if (itsListListeners.size() == 0) itsListListeners = null;
		}
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
	protected void elementRemoved (int aIndex, E aElement)
	{
	}
	
	protected void fireElementAdded (int aIndex, E aElement)
	{
		elementAdded(aIndex, aElement);
	
		if (itsListListeners == null) return;
		List<Object> theListeners = RefUtils.dereference(itsListListeners);
		
		for (Object theListener : theListeners)
		{
			if (theListener instanceof IListPropertyListener)
			{
				IListPropertyListener<E> theListPropertyListener = 
					(IListPropertyListener) theListener;
				theListPropertyListener.elementAdded(this, aIndex, aElement);
			}
			else if (theListener instanceof ICollectionPropertyListener)
			{
				ICollectionPropertyListener<E> theCollectionPropertyListener = 
					(ICollectionPropertyListener) theListener;
				theCollectionPropertyListener.elementAdded(this, aElement);
			}
		}
	}
	
	protected void fireElementRemoved (int aIndex, E aElement)
	{
		elementRemoved(aIndex, aElement);

		if (itsListListeners == null) return;
		List<Object> theListeners = RefUtils.dereference(itsListListeners);
		
		for (Object theListener : theListeners)
		{
			if (theListener instanceof IListPropertyListener)
			{
				IListPropertyListener<E> theListPropertyListener = 
					(IListPropertyListener) theListener;
				theListPropertyListener.elementRemoved(this, aIndex, aElement);
			}
			else if (theListener instanceof ICollectionPropertyListener)
			{
				ICollectionPropertyListener<E> theCollectionPropertyListener = 
					(ICollectionPropertyListener) theListener;
				theCollectionPropertyListener.elementRemoved(this, aElement);
			}
		}
	}
}
