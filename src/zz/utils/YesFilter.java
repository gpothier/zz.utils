/*
 * Created on Jan 17, 2004
 */
package zz.utils;


/**
 * A filter that accepts everything
 * @author gpothier
 */
public class YesFilter implements Filter
{
	private static final YesFilter INSTANCE = new YesFilter();
	public static YesFilter getInstance()
	{
		return INSTANCE;
	}
	private YesFilter()
	{
	}
	
	public boolean accept(Object aObject)
	{
		return true;
	}
}
