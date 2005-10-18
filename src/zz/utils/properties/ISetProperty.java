/*
 * Created on Dec 14, 2004
 */
package zz.utils.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A property whose value is a {@link java.util.Set}. Additional features are:
 * <li>Set manipulation methods
 * 
 * @author gpothier
 */
public interface ISetProperty<E> extends ICollectionProperty<Set<E>, E>
{
	/**
	 * Indicates if this set property contains the specified object.
	 */
	public boolean contains (Object aObject);
	
	/**
	 * For a set property, cloning values mean that each element in the set
	 * is cloned. Each element must implement {@link zz.utils.IPublicCloneable}.
	 * The backing set is always cloned. 
	 */
	public ISetProperty<E> cloneForOwner(Object aOwner,boolean aCloneValue);
}
