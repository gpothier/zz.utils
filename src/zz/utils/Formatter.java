/**
 * Created by IntelliJ IDEA.
 * User: Guillaume
 * Date: 30 oct. 2002
 * Time: 17:59:10
 */
package zz.utils;

/**
 * Serves to give a textual representation for an object.
 */
public interface Formatter
{
	/**
	 * Returns a plain text representation of the object.
	 */
	public String getPlainText (Object aObject);
	
	/**
	 * Returns an html representation of this object.
	 */
	public String getHtmlText (Object aObject);
}
