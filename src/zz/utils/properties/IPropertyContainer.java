/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * This interface should be implemented by classes that use 
 * {@link Property properties}.
 * @see zz.utils.properties.Example
 * @author gpothier
 */
public interface IPropertyContainer
{
	public IPropertyManager getPropertyManager();
}
