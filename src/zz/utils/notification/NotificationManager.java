/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: Feb 18, 2002
 * Time: 2:15:59 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils.notification;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

import zz.utils.FailsafeLinkedList;
import zz.utils.Filter;
import zz.utils.FilterIterator;
import zz.utils.Utils;
import zz.utils.YesFilter;

/**
 * Registers a set of {@link Notifiable notifiables} whose {@link Notifiable#processMessage(Message) } method
 * will be called when this {@link #notify(Message) } is invoked.
 */
public class NotificationManager
{
	private List itsNotifiables = new FailsafeLinkedList();

	private boolean itsActive = true;

	public NotificationManager()
	{
	}


	/**
	 * Activates or deactivates the notification manager.
	 * When deactivated, it doesn't dispatch any message.
	 */
	public void setActive(boolean aActive)
	{
		itsActive = aActive;
	}

	public boolean isActive()
	{
		return itsActive;
	}

	/**
	 * Removes all Notifiables from the notification manager
	 */
	public void clear()
	{
		itsNotifiables.clear();
	}

	/**
	 * Adds a notifiable to the list through a weak reference
	 * <p>
	 * IMPORTANT: As the notifiables are held in weak references, constructs such as
	 * <code>
	 * theNotificationManager.addNotifiable (new Notifiable ()
	 * {
	 * 		...
	 * });
	 * </code>
	 * are incorrect, as the notifiable will be immediately garbage collected.
	 */
	public void addNotifiableByReference(Notifiable aNotifiable)
	{
		itsNotifiables.add(new NotifiableReference(aNotifiable));
	}
	
	public void addNotifiable (Notifiable aNotifiable)
	{
		itsNotifiables.add (aNotifiable);
	}

	public void removeNotifiable(Notifiable aNotifiable)
	{
		Utils.remove(aNotifiable, itsNotifiables);
	}

	public void notify(Message aMessage)
	{
		notify (YesFilter.getInstance(), aMessage);
	}
	
	/**
	 * Notifies all the notifiables that pass the specified filter, sending them the given message.
	 */
	public void notify(Filter aFilter, Message aMessage)
	{
		if (!isActive()) return;

		for (Iterator theIterator = new FilterIterator (itsNotifiables.iterator(), aFilter); theIterator.hasNext();)
		{
			Notifiable theNotifiable = (Notifiable) theIterator.next();

			try
			{
				theNotifiable.processMessage(aMessage);
			}
			catch (EmptyReferenceException e)
			{
				theIterator.remove();
			}
		}
	}

	private static class NotifiableReference extends WeakReference implements Notifiable
	{
		public NotifiableReference(Notifiable aNotifiable)
		{
			super(aNotifiable);
		}

		public void processMessage(Message aMessage)
		{
			Notifiable theNotifiable = (Notifiable) get();
			if (theNotifiable == null) throw new EmptyReferenceException ();
			else theNotifiable.processMessage(aMessage);
		}
		
		public boolean equals(Object obj)
		{
			if (obj == this) return true;

			if (obj instanceof Notifiable)
			{
				Notifiable theNotifiable = (Notifiable) obj;
				return theNotifiable == get();
			}

			return false;
		}

	}
	
	private static class EmptyReferenceException extends RuntimeException
	{	
	}

}
