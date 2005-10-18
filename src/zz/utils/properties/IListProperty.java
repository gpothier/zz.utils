/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.Iterator;
import java.util.List;

import zz.utils.list.IList;
import zz.utils.list.IListListener;

/**
 * A property whose value is a list. Additional features are:
 * <li>List manipulation methods
 * <li>List listeners.
 * <li>Implements the {@link Iterable} interface.
 * 
 * @author gpothier
 */
public interface IListProperty<E> 
extends ICollectionProperty<List<E>, E>, IList<E>
{
	/**
	 * For a list property, cloning value means cloning each element
	 * of the list.
	 * Each element must implement {@link zz.utils.IPublicCloneable} 
	 * The backing list itself is always cloned.
	 */
	public IListProperty<E> cloneForOwner(Object aOwner, boolean aCloneValue);
}
