/*
 * Created on Jan 17, 2004
 */
package zz.utils;

/**
 * A filter that accepts nothing
 * @author gpothier
 */
public class NoFilter implements Filter
{
	private static final NoFilter INSTANCE = new NoFilter();
	public static NoFilter getInstance()
	{
		return INSTANCE;
	}
	private NoFilter()
	{
	}
	
	public boolean accept(Object aObject)
	{
		return false;
	}
}
