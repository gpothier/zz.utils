/*
 * Created by IntelliJ IDEA.
 * User: gpothier
 * Date: 21 mars 02
 * Time: 15:33:57
 * To change template for new interface use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package zz.utils;

public interface Stack
{
	public void push (Object aObject);
	public Object pop ();
	public Object peek ();
	public void clear ();
	public int size ();
	public boolean isEmpty ();
}
