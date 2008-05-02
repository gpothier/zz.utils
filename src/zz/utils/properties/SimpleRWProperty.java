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
	
	public SimpleRWProperty(Object aOwner, PropertyId<T> aPropertyId)
	{
		super(aOwner, aPropertyId);
	}

	public SimpleRWProperty(Object aOwner, PropertyId<T> aPropertyId, T aValue)
	{
		super(aOwner, aPropertyId, aValue);
	}

	public SimpleRWProperty(Object aOwner)
	{
		super(aOwner);
	}

	public SimpleRWProperty(Object aOwner, T aValue)
	{
		super(aOwner, aValue);
	}

	public T set (T aValue)
	{
		return set0(aValue);
	}
	
	/**
	 * Overriding this method is not recommanded (it would bypass the veto listeners mechanism).
	 * Override {@link #canChange(Object, Object)} instead.
	 */
	public boolean canSet(T aValue)
	{
		return canChangeProperty(get(), aValue) != REJECT;
	}
}
