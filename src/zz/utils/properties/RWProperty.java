/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * A read-write property.
 * @author gpothier
 */
public interface RWProperty<T> extends Property<T>
{
	public void set (T aValue);
}
