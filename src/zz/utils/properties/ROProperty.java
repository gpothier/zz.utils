/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * A read-only property.
 * @author gpothier
 */
public class ROProperty<T> extends Property<T>
{
	public ROProperty(IPropertyContainer aContainer, PropertyId aPropertyId)
	{
		super(aContainer, aPropertyId);
	}
	
	public ROProperty(IPropertyContainer aContainer, PropertyId aPropertyId, T aValue)
	{
		super(aContainer, aPropertyId, aValue);
	}
	
	public T get()
	{
		return get0();
	}
}
