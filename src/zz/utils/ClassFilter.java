/*
 * Created on Jan 17, 2004
 */
package zz.utils;

/**
 * An implemention of {@link Filter} that only accepts objects of a specific class.
 * @author gpothier
 */
public class ClassFilter implements Filter
{
	private Class itsClass;
	
	public ClassFilter (Class aClass)
	{
		itsClass = aClass;
	}
	
	public boolean accept(Object aObject)
	{
		return aObject == null || itsClass.isAssignableFrom(aObject.getClass());
	}
}