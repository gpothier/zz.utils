/*
 * Created on Jan 17, 2004
 */
package zz.utils;


/**
 * A generic interface for filtering obejcts.
 * @author gpothier
 */
public interface Filter
{
	public boolean accept (Object aObject);
}