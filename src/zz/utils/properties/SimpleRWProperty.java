/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

/**
 * A read-write property with a simple value
 * @author gpothier
 */
public class SimpleRWProperty<T> extends SimpleProperty<T> implements IRWProperty<T>
{
	public SimpleRWProperty()
	{
	}
	
	public SimpleRWProperty(Object aContainer, PropertyId<T> aPropertyId)
	{
		super(aContainer, aPropertyId);
	}

	public SimpleRWProperty(Object aContainer, PropertyId<T> aPropertyId, T aValue)
	{
		super(aContainer, aPropertyId, aValue);
	}

	public SimpleRWProperty(Object aContainer)
	{
		super(aContainer);
	}

	public SimpleRWProperty(Object aContainer, T aValue)
	{
		super(aContainer, aValue);
	}

	public void set (T aValue)
	{
		set0(aValue);
	}
}
