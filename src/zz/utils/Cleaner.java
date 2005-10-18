/*
 * Created on Oct 11, 2005
 */
package zz.utils;

import javax.swing.SwingUtilities;

import zz.utils.list.ICollection;
import zz.utils.list.ICollectionListener;
import zz.utils.properties.IProperty;
import zz.utils.properties.IPropertyListener;

/**
 * This class provides a mechanism for lazy cleaning of dirty items.
 * A concrete {@link Cleaner} subclass must be created for each cleanable
 * item. When marked dirty for the first time, the item posts
 * a Swing execution request that will cause its {@link #clean()}
 * method to be called when the current event processing is completed.
 * <br/>
 * A {@link Cleaner} can be added as a listener to a property, in which
 * case it is marked dirty whenever the property changes.   
 * @author gpothier
 */
public abstract class Cleaner 
implements Runnable, IPropertyListener, ICollectionListener
{
	private boolean itsDirty = false;
	private boolean itsScheduled = false;

	/**
	 * Schedules this cleaner for execution.
	 */
	public void markDirty()
	{
		if (! itsDirty)
		{
			itsDirty = true;
			if (! itsScheduled)
			{
				itsScheduled = true;
				SwingUtilities.invokeLater(this);
			}
		}
	}
	
	/**
	 * Executes this cleaner now, cancelling any pending execution.
	 */
	public void cleanNow()
	{
		clean();
		itsDirty = false;
	}
	
	public void run()
	{
		if (itsDirty)
		{
			cleanNow();
		}
		itsScheduled = false;
	}
	
	protected abstract void clean();

	public void propertyChanged(IProperty aProperty, Object aOldValue, Object aNewValue)
	{
		markDirty();
	}

	public void propertyValueChanged(IProperty aProperty)
	{
		markDirty();
	}

	public void elementAdded(ICollection aCollection, Object aElement)
	{
		markDirty();
	}

	public void elementRemoved(ICollection aCollection, Object aElement)
	{
		markDirty();
	}	
}
