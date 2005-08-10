/*
 * Created on Nov 15, 2004
 */
package zz.utils.properties;

import zz.utils.PublicCloneable;

/**
 * @author gpothier
 */
public class PropertyId<T>
{
	private final String itsName;
	
	/**
	 * Whether values of the property should be cloned when the property is cloned.
	 */
	private final boolean itsCloneValues;
	
	public PropertyId(String aName, boolean aCloneValues)
	{
		itsName = aName;
		itsCloneValues = aCloneValues;
	}
	
	public String getName()
	{
		return itsName;
	}

	protected boolean getCloneValues()
	{
		return itsCloneValues;
	}
	
	@Override
	public String toString()
	{
		return itsName;
	}
}
