/*
 * Created on Mar 28, 2005
 */
package zz.utils.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic implementation of {@link zz.utils.notification.IEvent}
 * @author gpothier
 */
public class SimpleEvent<T> extends AbstractEvent<T>
{
	private List<IEventListener<? super T>> itsListeners;

	public void addListener(IEventListener< ? super T> aListener)
	{
		if (itsListeners == null) itsListeners = new ArrayList<IEventListener<? super T>>(3);
		itsListeners.add (aListener);
	}

	public void removeListener(IEventListener< ? super T> aListener)
	{
		if (itsListeners != null) itsListeners.remove(aListener);
	}
	
	/**
	 * Notifies all listeners that the event has been fired.
	 * @param aData An object to pass to the listeners.
	 */
	public void fire (T aData)
	{
		if (itsListeners != null) for (IEventListener<? super T> theListener : itsListeners)
		{
			theListener.fired(this, aData);
		}
	}

}
