/*
 * Created on Apr 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package zz.utils;

import java.util.Iterator;
import java.util.List;


/**
 * Makes the {@link #clone()} method public.
 * Also provides utility methods to deep-clone collections
 * @author gpothier
 */
public class PublicCloneable implements IPublicCloneable
{
	public Object clone ()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}
	
	/**
	 * Deeply clones a list.
	 * @param aList A list whose elements must implement {@link IPublicCloneable}
	 */
	public static List cloneList (List aList)
	{
		try
		{
			List theResult = (List) aList.getClass().newInstance();
			for (Iterator theIterator = aList.iterator(); theIterator.hasNext();)
			{
				IPublicCloneable theElement = (IPublicCloneable) theIterator.next();
				theResult.add (theElement.clone());
			}
			
			return theResult;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
