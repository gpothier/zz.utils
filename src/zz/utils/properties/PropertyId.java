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
	private String itsName;
	
	public PropertyId(String aName)
	{
		itsName = aName;
	}
	
	public String getName()
	{
		return itsName;
	}
}
