/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * @author gpothier
 */
public interface IPropertyListener<T>
{
	public void propertyChanged (IProperty<T> aProperty);
}
