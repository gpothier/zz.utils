/*
 * Created on Mar 28, 2005
 */
package zz.utils.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of {@link zz.utils.notification.IEvent}
 * @author gpothier
 */
public abstract class AbstractEvent<T> implements IEvent<T>
{
	/**
	 * Notifies all listeners that the event has been fired.
	 * @param aData An object to pass to the listeners.
	 */
	public abstract void fire (T aData);
}
