/*
 * Created on 10-jun-2004
 */
package zz.utils.properties;


/**
 * A listener that is notified of modifications of a
 * {@link zz.utils.properties.ICollectionProperty}
 * @author gpothier
 */
public interface ICollectionPropertyListener<T>
{
	/**
	 * This method is called whenever an element is added to the collection.
	 * @param aCollectionProperty The collection property whose content changed.
	 * @param aElement The added element.
	 */
	public void elementAdded (ICollectionProperty<?, T> aCollectionProperty, T aElement);
	
	/**
	 * This method is called whenever an element is removed from the list.
	 * @param aCollectionProperty The collection property whose content changed.
	 * @param aElement The removed element.
	 */
	public void elementRemoved (ICollectionProperty<?, T> aCollectionProperty, T aElement);
}
