/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * @author gpothier
 */
public interface IPropertyVeto<T>
{
	public boolean canChangeProperty (Property<T> aProperty, Object aValue);
}
