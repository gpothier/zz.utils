/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * A read-write property.
 * @author gpothier
 */
public class RWProperty<T> extends Property<T>
{
	
	
	public RWProperty(IPropertyContainer aContainer, PropertyId aPropertyId)
	{
		super(aContainer, aPropertyId);
	}
	
	public RWProperty(IPropertyContainer aContainer, PropertyId aPropertyId, T aValue)
	{
		super(aContainer, aPropertyId, aValue);
	}
	
	public T get()
	{
		return get0();
	}
	
	public void set (T aValue)
	{
		set0(aValue);
	}
}
