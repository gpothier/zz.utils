/*
 * Created on Mar 30, 2005
 */
package zz.utils.notification;

/**
 * This subinterface of {@link zz.utils.notification.IEvent} exposes the 
 * {@link #fire(T)} method
 * @author gpothier
 */
public interface IFireableEvent<T> extends IEvent<T>
{
	/**
	 * Notifies all listeners that the event has been fired.
	 * @param aData An object to pass to the listeners.
	 */
	public void fire (T aData);

}
