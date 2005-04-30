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
import zz.utils.list.ICollectionListener;
import zz.utils.references.HardRef;
import zz.utils.references.IRef;
import zz.utils.references.RefUtils;
import zz.utils.references.WeakRef;

/**
 * Can be used as a base for building a {@link zz.utils.properties.ISetProperty}.
 * Manages listeners.
 * @author gpothier
 */
public abstract class AbstractSetProperty<E> extends AbstractProperty<Set<E>> 
implements ISetProperty<E>
{
	/**
	 * We store all the listeners here, be they collection or
	 * list listeners
	 */
	private List<IRef<ICollectionListener<E>>> itsListListeners;
	
	public AbstractSetProperty(Object aContainer)
	{
		super(aContainer);
	}
	
	public AbstractSetProperty(Object aContainer, PropertyId<Set<E>> aPropertyId)
	{
		super(aContainer, aPropertyId);
	}
	
	public void addListener (ICollectionListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new WeakRef<ICollectionListener<E>>(aListener));
	}

	public void addHardListener (ICollectionListener<E> aListener)
	{
		if (itsListListeners == null) itsListListeners = new ArrayList(3);
		itsListListeners.add (new HardRef<ICollectionListener<E>>(aListener));
	}
	
	public void removeListener (ICollectionListener<E> aListener)
	{
		if (itsListListeners != null) 
		{
			RefUtils.remove(itsListListeners, aListener);
			if (itsListListeners.size() == 0) itsListListeners = null;
		}
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
		List<ICollectionListener<E>> theListeners = 
			RefUtils.dereference(itsListListeners);
		
		for (ICollectionListener<E> theListener : theListeners)
		{
			theListener.elementAdded(this, aElement);
		}
	}
	
	protected void fireElementRemoved (E aElement)
	{
		elementRemoved(aElement);

		if (itsListListeners == null) return;
		List<ICollectionListener<E>> theListeners = 
			RefUtils.dereference(itsListListeners);
		
		for (ICollectionListener<E> theListener : theListeners)
		{
			theListener.elementRemoved(this, aElement);
		}
	}
	
	public ISetProperty<E> cloneForContainer(Object aContainer, boolean aCloneValue)
	{
		// Note: we don't tell super to clone value, we handle it ourselves.
		// As we are an abstract class, we don't do anything special.
		AbstractSetProperty<E> theClone = 
			(AbstractSetProperty<E>) super.cloneForContainer(aContainer, false); 
		
		theClone.itsListListeners = null;
		
		return theClone;

	}
}
