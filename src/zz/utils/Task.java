/*
 * Created on Dec 19, 2006
 */
package zz.utils;

/**
 * This interface is similar to {@link Runnable} but takes
 * an argument.
 * @author gpothier
 */
public interface Task<T>
{
	public void run(T aParameter);
}
