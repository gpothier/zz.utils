/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * A read-write property.
 * @author gpothier
 */
public class StdRWProperty<T> extends StdProperty<T> implements RWProperty<T>
{
	
	
	
	public StdRWProperty(Object aContainer, PropertyId<T> aPropertyId)
	{
		super(aContainer, aPropertyId);
	}

	public StdRWProperty(Object aContainer, PropertyId<T> aPropertyId, T aValue)
	{
		super(aContainer, aPropertyId, aValue);
	}

	public StdRWProperty(Object aContainer)
	{
		super(aContainer);
	}

	public StdRWProperty(Object aContainer, T aValue)
	{
		super(aContainer, aValue);
	}

	public void set (T aValue)
	{
		set0(aValue);
	}
}
