/*
 * Created on 10-jun-2004
 */
package zz.utils.properties;


/**
 * A listener that is notified of modifications of a
 * {@link zz.utils.properties.IListProperty}
 * @author gpothier
 */
public interface IListPropertyListener<T>
{
	/**
	 * This method is called whenever an element is added to the list.
	 * @param aListProperty The list property whose content changed.
	 * @param aIndex The index of the newly added element
	 * @param aElement The added element.
	 */
	public void elementAdded (IListProperty<T> aListProperty, int aIndex, T aElement);
	
	/**
	 * This method is called whenever an element is removed from the list.
	 * @param aListProperty The list property whose content changed.
	 * @param aIndex The index previously occupied by the removed element
	 * @param aElement The removed element.
	 */
	public void elementRemoved (IListProperty<T> aListProperty, int aIndex, T aElement);
}
