/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * @author gpothier
 */
public interface IPropertyVeto<T>
{
	public boolean canChangeProperty (IProperty<T> aProperty, T aValue);
}
