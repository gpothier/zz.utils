/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * @author gpothier
 */
public interface IPropertyVeto 
{
	public boolean canChangeProperty (Property<?> aProperty, Object aValue);
}
